package org.esa.s2tbx.dataio.sen2agri;

import com.bc.ceres.core.ProgressMonitor;
import it.geosolutions.imageioimpl.plugins.tiff.TIFFImageReader;
import org.esa.snap.core.dataio.AbstractProductReader;
import org.esa.snap.core.dataio.ProductReaderPlugIn;
import org.esa.snap.core.datamodel.*;
import org.geotools.referencing.CRS;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.TransformException;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.DataBuffer;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Sen2AgriProductReader extends AbstractProductReader {

    private File inputFile;
    private final List<ImageInputStream> streamList;
    private final HashMap<String, StreamContainer> tiffReaderMap;
    private CrsGeoCoding highResGeoCoding;
    private CrsGeoCoding lowResGeoCoding;

    Sen2AgriProductReader(ProductReaderPlugIn readerPlugIn) {
        super(readerPlugIn);

        streamList = new ArrayList<>();
        tiffReaderMap = new HashMap<>();
    }

    @Override
    protected Product readProductNodesImpl() throws IOException {
        final Object input = getInput();
        inputFile = Sen2AgriProductReaderPlugIn.getInputFile(input);

        final GlobalHeader globalHeader = readGlobalHeader();

        final Product product = new Product(globalHeader.getProductName(),
                "S2_AGRI_SSC_L2VALD",
                globalHeader.getSceneRasterWidth(),
                globalHeader.getSceneRasterHeight(),
                this);

        product.setStartTime(globalHeader.getSensingTime());
        product.setEndTime(globalHeader.getSensingTime());

        constructGeoCodings(globalHeader);

        addBandsToProduct(product, globalHeader);

        product.setSceneGeoCoding(highResGeoCoding);

        return product;
    }

    @Override
    protected void readBandRasterDataImpl(int sourceOffsetX, int sourceOffsetY,
                                          int sourceWidth, int sourceHeight,
                                          int sourceStepX, int sourceStepY,
                                          Band destBand,
                                          int destOffsetX, int destOffsetY,
                                          int destWidth, int destHeight,
                                          ProductData destBuffer, ProgressMonitor pm) throws IOException {
        final String bandName = destBand.getName();
        final StreamContainer streamContainer = tiffReaderMap.get(bandName);
        if (streamContainer == null) {
            throw new IOException("Requested band '" + bandName + "' not present in file");
        }

        final Raster tiffData = readRect(sourceOffsetX, sourceOffsetY, sourceStepX, sourceStepY,
                destOffsetX, destOffsetY, destWidth, destHeight, streamContainer);

        final int imageIndex = streamContainer.getImageIndex();
        final Raster bandData = tiffData.createChild(sourceOffsetX, sourceOffsetY, destWidth, destHeight, destOffsetX, destOffsetY, new int[]{imageIndex});

        final int destSize = destWidth * destHeight;
        final DataBuffer dataBuffer = bandData.getDataBuffer();
        final SampleModel sampleModel = bandData.getSampleModel();
        final int[] intermediateBuffer = new int[destSize];
        sampleModel.getSamples(0, 0, bandData.getWidth(), bandData.getHeight(), 0, intermediateBuffer, dataBuffer);
        if ((destBuffer.getElems() instanceof byte[]) || (destBuffer.getElems() instanceof short[])) {
            int i = 0;
            for (int value : intermediateBuffer) {
                destBuffer.setElemIntAt(i++, value);
            }
        } else {
            throw new IOException("Illegal destination data type");
        }
    }

    @Override
    public void readTiePointGridRasterData(TiePointGrid tpg, int destOffsetX, int destOffsetY, int destWidth, int destHeight, ProductData destBuffer, ProgressMonitor pm) throws IOException {
        throw new RuntimeException("not implemented");
    }

    @Override
    public void close() throws IOException {
        for (ImageInputStream stream : streamList) {
            stream.close();
        }
        streamList.clear();

        tiffReaderMap.clear();

        super.close();
    }

    private GlobalHeader readGlobalHeader() throws IOException {
        return new GlobalHeader(readXmlDocument(inputFile));
    }

    private ATBHeader readATBHeader(File file) throws IOException {
        return new ATBHeader(readXmlDocument(file));
    }

    private BandHeader readBandHeader(File file) throws IOException {
        return new BandHeader(readXmlDocument(file));
    }

    private Document readXmlDocument(File file) throws IOException {
        final SAXBuilder saxBuilder = new SAXBuilder();

        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            return saxBuilder.build(fileInputStream);
        } catch (JDOMException e) {
            throw new IOException(e.getMessage());
        }
    }

    private void constructGeoCodings(GlobalHeader globalHeader) throws IOException {
        final String epsgCode = globalHeader.getEpsgCode();

        try {
            final CoordinateReferenceSystem crs = CRS.decode(epsgCode);

            final CrsParameter highResParams = globalHeader.getHighResCrsParameter();
            highResGeoCoding = new CrsGeoCoding(crs, highResParams.width, highResParams.height,
                    highResParams.easting, highResParams.northing,
                    highResParams.pixelSizeX, -highResParams.pixelSizeY);

            final CrsParameter lowResParams = globalHeader.getLowResCrsParameter();
            lowResGeoCoding = new CrsGeoCoding(crs, lowResParams.width, lowResParams.height,
                    lowResParams.easting, lowResParams.northing,
                    lowResParams.pixelSizeX, -lowResParams.pixelSizeY);
        } catch (FactoryException | TransformException e) {
            throw new IOException(e.getMessage());
        }
    }

    private void addBandsToProduct(Product product, GlobalHeader globalHeader) throws IOException {
        final String dataDirName = getDataDirName(globalHeader);
        final File dataDir = new File(inputFile.getParentFile(), dataDirName);
        if (!dataDir.isDirectory()) {
            throw new IOException("data directory '" + dataDirName + "' expected but not found");
        }

        add_ATB_Bands(product, dataDir);
        add_CLD_Bands(product, dataDir);
        add_MSK_Bands(product, dataDir);
        add_QLT_Bands(product, dataDir);
        add_FRE_Bands(product, dataDir, globalHeader);
        add_SRE_Bands(product, dataDir, globalHeader);
    }

    private void add_SRE_Bands(Product product, File dataDir, GlobalHeader globalHeader) throws IOException {
        final double scaleFactor = globalHeader.getReflectanceScaleFactor();

        final File sre_r1_tif = getFile(".*SRE_R1.DBL.TIF", dataDir);
        BandHeader bandHeader = readBandHeader(getFile(".*SRE_R1.HDR", dataDir));
        ImageInputStream stream = registerInputStream(sre_r1_tif);
        TIFFImageReader tiffImageReader = getTiffImageReader(stream);

        addStreamContainer("SRE_R1_B2", 0, tiffImageReader);
        addScaledBand("SRE_R1_B2", scaleFactor, product, bandHeader);

        addStreamContainer("SRE_R1_B3", 1, tiffImageReader);
        addScaledBand("SRE_R1_B3", scaleFactor, product, bandHeader);

        addStreamContainer("SRE_R1_B4", 2, tiffImageReader);
        addScaledBand("SRE_R1_B4", scaleFactor, product, bandHeader);

        addStreamContainer("SRE_R1_B8", 3, tiffImageReader);
        addScaledBand("SRE_R1_B8", scaleFactor, product, bandHeader);

        final File sre_r2_tif = getFile(".*SRE_R2.DBL.TIF", dataDir);
        bandHeader = readBandHeader(getFile(".*SRE_R2.HDR", dataDir));
        stream = registerInputStream(sre_r2_tif);
        tiffImageReader = getTiffImageReader(stream);

        addStreamContainer("SRE_R2_B5", 0, tiffImageReader);
        addScaledBand("SRE_R2_B5", scaleFactor, product, bandHeader);

        addStreamContainer("SRE_R2_B6", 1, tiffImageReader);
        addScaledBand("SRE_R2_B6", scaleFactor, product, bandHeader);

        addStreamContainer("SRE_R2_B7", 2, tiffImageReader);
        addScaledBand("SRE_R2_B7", scaleFactor, product, bandHeader);

        addStreamContainer("SRE_R2_B8A", 3, tiffImageReader);
        addScaledBand("SRE_R2_B8A", scaleFactor, product, bandHeader);

        addStreamContainer("SRE_R2_B11", 4, tiffImageReader);
        addScaledBand("SRE_R2_B11", scaleFactor, product, bandHeader);

        addStreamContainer("SRE_R2_B12", 5, tiffImageReader);
        addScaledBand("SRE_R2_B12", scaleFactor, product, bandHeader);
    }

    private void add_FRE_Bands(Product product, File dataDir, GlobalHeader globalHeader) throws IOException {
        final double scaleFactor = globalHeader.getReflectanceScaleFactor();
        ImageInputStream stream;
        TIFFImageReader tiffImageReader;

        final File fre_r1_tif = getFile(".*FRE_R1.DBL.TIF", dataDir);
        BandHeader bandHeader = readBandHeader(getFile(".*FRE_R1.HDR", dataDir));
        stream = registerInputStream(fre_r1_tif);
        tiffImageReader = getTiffImageReader(stream);
        addStreamContainer("FRE_R1_B2", 0, tiffImageReader);
        addScaledBand("FRE_R1_B2", scaleFactor, product, bandHeader);

        addStreamContainer("FRE_R1_B3", 1, tiffImageReader);
        addScaledBand("FRE_R1_B3", scaleFactor, product, bandHeader);

        addStreamContainer("FRE_R1_B4", 2, tiffImageReader);
        addScaledBand("FRE_R1_B4", scaleFactor, product, bandHeader);

        addStreamContainer("FRE_R1_B8", 3, tiffImageReader);
        addScaledBand("FRE_R1_B8", scaleFactor, product, bandHeader);

        final File fre_r2_tif = getFile(".*FRE_R2.DBL.TIF", dataDir);
        bandHeader = readBandHeader(getFile(".*FRE_R2.HDR", dataDir));
        stream = registerInputStream(fre_r2_tif);
        tiffImageReader = getTiffImageReader(stream);

        addStreamContainer("FRE_R2_B5", 0, tiffImageReader);
        addScaledBand("FRE_R2_B5", scaleFactor, product, bandHeader);

        addStreamContainer("FRE_R2_B6", 1, tiffImageReader);
        addScaledBand("FRE_R2_B6", scaleFactor, product, bandHeader);

        addStreamContainer("FRE_R2_B7", 2, tiffImageReader);
        addScaledBand("FRE_R2_B7", scaleFactor, product, bandHeader);

        addStreamContainer("FRE_R2_B8A", 3, tiffImageReader);
        addScaledBand("FRE_R2_B8A", scaleFactor, product, bandHeader);

        addStreamContainer("FRE_R2_B11", 4, tiffImageReader);
        addScaledBand("FRE_R2_B11", scaleFactor, product, bandHeader);

        addStreamContainer("FRE_R2_B12", 5, tiffImageReader);
        addScaledBand("FRE_R2_B12", scaleFactor, product, bandHeader);
    }

    private void addScaledBand(String name, double scaleFactor, Product product, BandHeader bandHeader) {
        Band band = new Band(name, ProductData.TYPE_INT16, bandHeader.getRasterWidth(), bandHeader.getRasterHeight());
        band.setScalingFactor(scaleFactor);
        band.setNoDataValue(bandHeader.getNoDataValue());
        if (name.contains("_R1_")) {
            band.setGeoCoding(highResGeoCoding);
        } else {
            band.setGeoCoding(lowResGeoCoding);
        }
        product.addBand(band);
    }

    private void add_QLT_Bands(Product product, File dataDir) throws IOException {
        // @todo 1 tb/tb add flag coding 2018-05-07
        final File qlt_r1_tif = getFile(".*QLT_R1.DBL.TIF", dataDir);
        BandHeader bandHeader = readBandHeader(getFile(".*QLT_R1.HDR", dataDir));
        ImageInputStream stream = registerInputStream(qlt_r1_tif);
        TIFFImageReader tiffImageReader = getTiffImageReader(stream);

        addStreamContainer("QLT_R1_SAT", 0, tiffImageReader);
        Band band = new Band("QLT_R1_SAT", ProductData.TYPE_UINT8, bandHeader.getRasterWidth(), bandHeader.getRasterHeight());
        band.setGeoCoding(highResGeoCoding);
        product.addBand(band);
        addStreamContainer("QLT_R1_PIX", 1, tiffImageReader);
        band = new Band("QLT_R1_PIX", ProductData.TYPE_UINT8, bandHeader.getRasterWidth(), bandHeader.getRasterHeight());
        band.setGeoCoding(highResGeoCoding);
        product.addBand(band);
        addStreamContainer("QLT_R1_OTH", 2, tiffImageReader);
        band = new Band("QLT_R1_OTH", ProductData.TYPE_UINT8, bandHeader.getRasterWidth(), bandHeader.getRasterHeight());
        band.setGeoCoding(highResGeoCoding);
        product.addBand(band);

        final File qlt_r2_tif = getFile(".*QLT_R2.DBL.TIF", dataDir);
        stream = registerInputStream(qlt_r2_tif);
        tiffImageReader = getTiffImageReader(stream);

        addStreamContainer("QLT_R2_SAT", 0, tiffImageReader);
        band = new Band("QLT_R2_SAT", ProductData.TYPE_UINT8, bandHeader.getRasterWidth(), bandHeader.getRasterHeight());
        band.setGeoCoding(lowResGeoCoding);
        product.addBand(band);
        addStreamContainer("QLT_R2_PIX", 1, tiffImageReader);
        band = new Band("QLT_R2_PIX", ProductData.TYPE_UINT8, bandHeader.getRasterWidth(), bandHeader.getRasterHeight());
        band.setGeoCoding(lowResGeoCoding);
        product.addBand(band);
        addStreamContainer("QLT_R2_OTH", 2, tiffImageReader);
        band = new Band("QLT_R2_OTH", ProductData.TYPE_UINT8, bandHeader.getRasterWidth(), bandHeader.getRasterHeight());
        band.setGeoCoding(lowResGeoCoding);
        product.addBand(band);
    }

    private void add_MSK_Bands(Product product, File dataDir) throws IOException {
        final File msk_r1_tif = getFile(".*MSK_R1.DBL.TIF", dataDir);
        BandHeader bandHeader = readBandHeader(getFile(".*MSK_R1.HDR", dataDir));
        ImageInputStream stream = registerInputStream(msk_r1_tif);
        TIFFImageReader tiffImageReader = getTiffImageReader(stream);

        addStreamContainer("MSK_R1", 0, tiffImageReader);
        Band band = new Band("MSK_R1", ProductData.TYPE_UINT8, bandHeader.getRasterWidth(), bandHeader.getRasterHeight());
        band.setGeoCoding(highResGeoCoding);
        band.setSampleCoding(createGeophysicalFlagCoding());
        product.addBand(band);

        final File msk_r2_tif = getFile(".*MSK_R2.DBL.TIF", dataDir);
        bandHeader = readBandHeader(getFile(".*MSK_R2.HDR", dataDir));
        stream = registerInputStream(msk_r2_tif);
        tiffImageReader = getTiffImageReader(stream);

        addStreamContainer("MSK_R2", 0, tiffImageReader);
        band = new Band("MSK_R2", ProductData.TYPE_UINT8, bandHeader.getRasterWidth(), bandHeader.getRasterHeight());
        band.setGeoCoding(lowResGeoCoding);
        band.setSampleCoding(createGeophysicalFlagCoding());
        product.addBand(band);
    }

    private void add_CLD_Bands(Product product, File dataDir) throws IOException {
        final File cld_r1_tif = getFile(".*CLD_R1.DBL.TIF", dataDir);
        BandHeader bandHeader = readBandHeader(getFile(".*CLD_R1.HDR", dataDir));
        ImageInputStream stream = registerInputStream(cld_r1_tif);
        TIFFImageReader tiffImageReader = getTiffImageReader(stream);

        addStreamContainer("CLD_R1", 0, tiffImageReader);
        Band band = new Band("CLD_R1", ProductData.TYPE_UINT8, bandHeader.getRasterWidth(), bandHeader.getRasterHeight());
        band.setGeoCoding(highResGeoCoding);
        band.setSampleCoding(createCloudFlagCoding());
        product.addBand(band);

        final File cld_r2_tif = getFile(".*CLD_R2.DBL.TIF", dataDir);
        bandHeader = readBandHeader(getFile(".*CLD_R2.HDR", dataDir));
        stream = registerInputStream(cld_r2_tif);
        tiffImageReader = getTiffImageReader(stream);

        addStreamContainer("CLD_R2", 0, tiffImageReader);
        band = new Band("CLD_R2", ProductData.TYPE_UINT8, bandHeader.getRasterWidth(), bandHeader.getRasterHeight());
        band.setGeoCoding(lowResGeoCoding);
        band.setSampleCoding(createCloudFlagCoding());
        product.addBand(band);
    }

    private void add_ATB_Bands(Product product, File dataDir) throws IOException {
        final File atb_r1_tif = getFile(".*ATB_R1.DBL.TIF", dataDir);
        ATBHeader atbHdr = readATBHeader(getFile(".*ATB_R1.HDR", dataDir));

        ImageInputStream stream = registerInputStream(atb_r1_tif);
        TIFFImageReader tiffImageReader = getTiffImageReader(stream);
        addStreamContainer("ATB_R1_VAP", 0, tiffImageReader);

        Band band = new Band("ATB_R1_VAP", ProductData.TYPE_UINT8, atbHdr.getRasterWidth(), atbHdr.getRasterHeight());
        band.setScalingFactor(atbHdr.getVapScaleFactor());
        band.setNoDataValue(atbHdr.getVapNoDataValue());
        band.setUnit("g/cm2");
        band.setGeoCoding(highResGeoCoding);
        product.addBand(band);

        addStreamContainer("ATB_R1_AOT", 1, tiffImageReader);
        band = new Band("ATB_R1_AOT", ProductData.TYPE_UINT8, atbHdr.getRasterWidth(), atbHdr.getRasterHeight());
        band.setScalingFactor(atbHdr.getAotScaleFactor());
        band.setNoDataValue(atbHdr.getAotNoDataValue());
        band.setGeoCoding(highResGeoCoding);
        product.addBand(band);

        final File atb_r2_tif = getFile(".*ATB_R2.DBL.TIF", dataDir);
        atbHdr = readATBHeader(getFile(".*ATB_R2.HDR", dataDir));
        stream = registerInputStream(atb_r2_tif);
        tiffImageReader = getTiffImageReader(stream);

        addStreamContainer("ATB_R2_VAP", 0, tiffImageReader);
        band = new Band("ATB_R2_VAP", ProductData.TYPE_UINT8, atbHdr.getRasterWidth(), atbHdr.getRasterHeight());
        band.setScalingFactor(atbHdr.getVapScaleFactor());
        band.setNoDataValue(atbHdr.getVapNoDataValue());
        band.setUnit("g/cm2");
        band.setGeoCoding(lowResGeoCoding);
        product.addBand(band);

        addStreamContainer("ATB_R2_AOT", 1, tiffImageReader);
        band = new Band("ATB_R2_AOT", ProductData.TYPE_UINT8, atbHdr.getRasterWidth(), atbHdr.getRasterHeight());
        band.setScalingFactor(atbHdr.getAotScaleFactor());
        band.setNoDataValue(atbHdr.getAotNoDataValue());
        band.setGeoCoding(lowResGeoCoding);
        product.addBand(band);
    }

    private void addStreamContainer(String bandName, int imageIndex, TIFFImageReader tiffImageReader) {
        final StreamContainer streamContainer = new StreamContainer();
        streamContainer.imageIndex = imageIndex;
        streamContainer.tiffImageReader = tiffImageReader;
        tiffReaderMap.put(bandName, streamContainer);
    }

    private ImageInputStream registerInputStream(File atb_r1_tif) throws IOException {
        final ImageInputStream stream = ImageIO.createImageInputStream(atb_r1_tif);
        streamList.add(stream);
        return stream;
    }

    // package access for testing only tb 2018-05-04
    static String getDataDirName(GlobalHeader globalHeader) {
        final String productName = globalHeader.getProductName();
        return productName + ".DBL.DIR";
    }

    // package access for testing only tb 2018-05-08
    static FlagCoding createCloudFlagCoding() {
        final FlagCoding flagCoding = new FlagCoding("CLD");
        flagCoding.setDescription("Cloud and cloud shadow mask");
        flagCoding.addFlag("ALL", 0x1, "Summary Logical or of All cloud and shadow masks");
        flagCoding.addFlag("ALL_CLOUDS", 0x2, "Logical or of All cloud masks");
        flagCoding.addFlag("SHADOWS", 0x4, "Shadows mask from clouds within image");
        flagCoding.addFlag("SHADVAR", 0x8, "Shadows mask from clouds outside image");
        flagCoding.addFlag("REFL", 0x10, "Reflectance threshold");
        flagCoding.addFlag("REFL_VAR", 0x20, "Reflectance variation threshold");
        flagCoding.addFlag("EXTENSION", 0x40, "Extension of the cloud mask");
        flagCoding.addFlag("CIRRUS", 0x80, "Cirrus mask");
        return flagCoding;
    }

    // package access for testing only tb 2018-05-09
    static FlagCoding createGeophysicalFlagCoding() {
        final FlagCoding flagCoding = new FlagCoding("MSK");
        flagCoding.setDescription("Geophysical masks");
        flagCoding.addFlag("WAT", 0x1, "Water mask");
        flagCoding.addFlag("HID", 0x2, "Hidden surfaces");
        flagCoding.addFlag("SHD", 0x4, "shadowed by topography mask");
        flagCoding.addFlag("STL", 0x8, "sun too low flag");
        flagCoding.addFlag("TGS", 0x10, "tangent sun flag");
        flagCoding.addFlag("SNW", 0x20, "Snow");
        return flagCoding;
    }

    private File getFile(String regularExpression, File dataDir) throws IOException {
        final String[] dataFileList = dataDir.list(new RegExFileNameFilter(regularExpression));
        if ((dataFileList == null) || (dataFileList.length != 1)) {
            throw new IOException("Unable to find data file: " + regularExpression);
        }
        return new File(dataDir, dataFileList[0]);
    }

    private TIFFImageReader getTiffImageReader(ImageInputStream stream) throws IOException {
        Iterator<ImageReader> imageReaders = ImageIO.getImageReaders(stream);
        TIFFImageReader imageReader = null;
        while (imageReaders.hasNext()) {
            final ImageReader reader = imageReaders.next();
            if (reader instanceof TIFFImageReader) {
                imageReader = (TIFFImageReader) reader;
                break;
            }
        }
        if (imageReader == null) {
            throw new IOException("GeoTiff imageReader not found");
        }

        imageReader.setInput(stream);

        return imageReader;
    }

    private synchronized Raster readRect(int sourceOffsetX, int sourceOffsetY, int sourceStepX, int sourceStepY,
                                         int destOffsetX, int destOffsetY, int destWidth, int destHeight, StreamContainer streamContainer) throws IOException {
        final TIFFImageReader tiffImageReader = streamContainer.getTiffImageReader();
        final ImageReadParam readParam = tiffImageReader.getDefaultReadParam();

        int subsamplingXOffset = sourceOffsetX % sourceStepX;
        int subsamplingYOffset = sourceOffsetY % sourceStepY;
        readParam.setSourceSubsampling(sourceStepX, sourceStepY, subsamplingXOffset, subsamplingYOffset);
        final RenderedImage subsampledImage = tiffImageReader.readAsRenderedImage(0, readParam);

        return subsampledImage.getData(new Rectangle(destOffsetX, destOffsetY, destWidth, destHeight));
    }
}
