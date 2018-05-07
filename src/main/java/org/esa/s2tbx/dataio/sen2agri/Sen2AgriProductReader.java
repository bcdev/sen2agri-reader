package org.esa.s2tbx.dataio.sen2agri;

import com.bc.ceres.core.ProgressMonitor;
import it.geosolutions.imageioimpl.plugins.tiff.TIFFImageReader;
import org.esa.snap.core.dataio.AbstractProductReader;
import org.esa.snap.core.dataio.ProductReaderPlugIn;
import org.esa.snap.core.datamodel.Band;
import org.esa.snap.core.datamodel.Product;
import org.esa.snap.core.datamodel.ProductData;
import org.esa.snap.core.datamodel.TiePointGrid;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

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

        addBandsToProduct(product, globalHeader);

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
        final SAXBuilder saxBuilder = new SAXBuilder();

        try (FileInputStream fileInputStream = new FileInputStream(inputFile)) {
            final Document document = saxBuilder.build(fileInputStream);
            return new GlobalHeader(document);
        } catch (JDOMException e) {
            throw new IOException(e.getMessage());
        }
    }

    private ATBHeader readATBHeader(File file) throws IOException {
        final SAXBuilder saxBuilder = new SAXBuilder();

        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            final Document document = saxBuilder.build(fileInputStream);
            return new ATBHeader(document);
        } catch (JDOMException e) {
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

        ImageInputStream stream;
        TIFFImageReader tiffImageReader;

        final File cld_r1_tif = getFile(".*CLD_R1.DBL.TIF", dataDir);
        stream = registerInputStream(cld_r1_tif);
        tiffImageReader = getTiffImageReader(stream);
        addStreamContainer("CLD_R1", 0, tiffImageReader);
        product.addBand("CLD_R1", ProductData.TYPE_UINT8);

        final File cld_r2_tif = getFile(".*CLD_R2.DBL.TIF", dataDir);
        stream = registerInputStream(cld_r2_tif);
        tiffImageReader = getTiffImageReader(stream);
        addStreamContainer("CLD_R2", 0, tiffImageReader);
        product.addBand("CLD_R2", ProductData.TYPE_UINT8);

        final File msk_r1_tif = getFile(".*MSK_R1.DBL.TIF", dataDir);
        stream = registerInputStream(msk_r1_tif);
        tiffImageReader = getTiffImageReader(stream);
        addStreamContainer("MSK_R1", 0, tiffImageReader);
        product.addBand("MSK_R1", ProductData.TYPE_UINT8);

        final File msk_r2_tif = getFile(".*MSK_R2.DBL.TIF", dataDir);
        stream = registerInputStream(msk_r2_tif);
        tiffImageReader = getTiffImageReader(stream);
        addStreamContainer("MSK_R2", 0, tiffImageReader);
        product.addBand("MSK_R2", ProductData.TYPE_UINT8);

        final File qlt_r1_tif = getFile(".*QLT_R1.DBL.TIF", dataDir);
        stream = registerInputStream(qlt_r1_tif);
        tiffImageReader = getTiffImageReader(stream);
        addStreamContainer("QLT_R1_SAT", 0, tiffImageReader);
        product.addBand("QLT_R1_SAT", ProductData.TYPE_UINT8);
        addStreamContainer("QLT_R1_PIX", 1, tiffImageReader);
        product.addBand("QLT_R1_PIX", ProductData.TYPE_UINT8);
        addStreamContainer("QLT_R1_OTH", 2, tiffImageReader);
        product.addBand("QLT_R1_OTH", ProductData.TYPE_UINT8);

        final File qlt_r2_tif = getFile(".*QLT_R2.DBL.TIF", dataDir);
        stream = registerInputStream(qlt_r2_tif);
        tiffImageReader = getTiffImageReader(stream);
        addStreamContainer("QLT_R2_SAT", 0, tiffImageReader);
        product.addBand("QLT_R2_SAT", ProductData.TYPE_UINT8);
        addStreamContainer("QLT_R2_PIX", 1, tiffImageReader);
        product.addBand("QLT_R2_PIX", ProductData.TYPE_UINT8);
        addStreamContainer("QLT_R2_OTH", 2, tiffImageReader);
        product.addBand("QLT_R2_OTH", ProductData.TYPE_UINT8);

        final File fre_r1_tif = getFile(".*FRE_R1.DBL.TIF", dataDir);
        stream = registerInputStream(fre_r1_tif);
        tiffImageReader = getTiffImageReader(stream);
        addStreamContainer("FRE_R1_B2", 0, tiffImageReader);
        product.addBand("FRE_R1_B2", ProductData.TYPE_INT16);
        addStreamContainer("FRE_R1_B3", 1, tiffImageReader);
        product.addBand("FRE_R1_B3", ProductData.TYPE_INT16);
        addStreamContainer("FRE_R1_B4", 2, tiffImageReader);
        product.addBand("FRE_R1_B4", ProductData.TYPE_INT16);
        addStreamContainer("FRE_R1_B8", 3, tiffImageReader);
        product.addBand("FRE_R1_B8", ProductData.TYPE_INT16);

        final File fre_r2_tif = getFile(".*FRE_R2.DBL.TIF", dataDir);
        stream = registerInputStream(fre_r2_tif);
        tiffImageReader = getTiffImageReader(stream);
        addStreamContainer("FRE_R2_B5", 0, tiffImageReader);
        product.addBand("FRE_R2_B5", ProductData.TYPE_INT16);
        addStreamContainer("FRE_R2_B6", 1, tiffImageReader);
        product.addBand("FRE_R2_B6", ProductData.TYPE_INT16);
        addStreamContainer("FRE_R2_B7", 2, tiffImageReader);
        product.addBand("FRE_R2_B7", ProductData.TYPE_INT16);
        addStreamContainer("FRE_R2_B8A", 3, tiffImageReader);
        product.addBand("FRE_R2_B8A", ProductData.TYPE_INT16);
        addStreamContainer("FRE_R2_B11", 4, tiffImageReader);
        product.addBand("FRE_R2_B11", ProductData.TYPE_INT16);
        addStreamContainer("FRE_R2_B12", 5, tiffImageReader);
        product.addBand("FRE_R2_B12", ProductData.TYPE_INT16);

        final File sre_r1_tif = getFile(".*SRE_R1.DBL.TIF", dataDir);
        stream = registerInputStream(sre_r1_tif);
        tiffImageReader = getTiffImageReader(stream);
        addStreamContainer("SRE_R1_B2", 0, tiffImageReader);
        product.addBand("SRE_R1_B2", ProductData.TYPE_INT16);
        addStreamContainer("SRE_R1_B3", 1, tiffImageReader);
        product.addBand("SRE_R1_B3", ProductData.TYPE_INT16);
        addStreamContainer("SRE_R1_B4", 2, tiffImageReader);
        product.addBand("SRE_R1_B4", ProductData.TYPE_INT16);
        addStreamContainer("SRE_R1_B8", 3, tiffImageReader);
        product.addBand("SRE_R1_B8", ProductData.TYPE_INT16);

        final File sre_r2_tif = getFile(".*SRE_R2.DBL.TIF", dataDir);
        stream = registerInputStream(sre_r2_tif);
        tiffImageReader = getTiffImageReader(stream);
        addStreamContainer("SRE_R2_B5", 0, tiffImageReader);
        product.addBand("SRE_R2_B5", ProductData.TYPE_INT16);
        addStreamContainer("SRE_R2_B6", 1, tiffImageReader);
        product.addBand("SRE_R2_B6", ProductData.TYPE_INT16);
        addStreamContainer("SRE_R2_B7", 2, tiffImageReader);
        product.addBand("SRE_R2_B7", ProductData.TYPE_INT16);
        addStreamContainer("SRE_R2_B8A", 3, tiffImageReader);
        product.addBand("SRE_R2_B8A", ProductData.TYPE_INT16);
        addStreamContainer("SRE_R2_B11", 4, tiffImageReader);
        product.addBand("SRE_R2_B11", ProductData.TYPE_INT16);
        addStreamContainer("SRE_R2_B12", 5, tiffImageReader);
        product.addBand("SRE_R2_B12", ProductData.TYPE_INT16);
    }

    private void add_ATB_Bands(Product product, File dataDir) throws IOException {
        final File atb_r1_tif = getFile(".*ATB_R1.DBL.TIF", dataDir);
        ATBHeader atbHdr = readATBHeader(getFile(".*ATB_R1.HDR", dataDir));

        ImageInputStream stream = registerInputStream(atb_r1_tif);
        TIFFImageReader tiffImageReader = getTiffImageReader(stream);
        addStreamContainer("ATB_R1_VAP", 0, tiffImageReader);

        Band band = new Band("ATB_R1_VAP", ProductData.TYPE_UINT8, atbHdr.getRasterWidth(), atbHdr.getRasterHeight());
        band.setScalingFactor(0.05);
        product.addBand(band);

        addStreamContainer("ATB_R1_AOT", 1, tiffImageReader);
        band = new Band("ATB_R1_AOT", ProductData.TYPE_UINT8, atbHdr.getRasterWidth(), atbHdr.getRasterHeight());
        band.setScalingFactor(0.005);
        product.addBand(band);

        final File atb_r2_tif = getFile(".*ATB_R2.DBL.TIF", dataDir);
        atbHdr = readATBHeader(getFile(".*ATB_R2.HDR", dataDir));
        stream = registerInputStream(atb_r2_tif);
        tiffImageReader = getTiffImageReader(stream);

        addStreamContainer("ATB_R2_VAP", 0, tiffImageReader);
        band = new Band("ATB_R2_VAP", ProductData.TYPE_UINT8, atbHdr.getRasterWidth(), atbHdr.getRasterHeight());
        band.setScalingFactor(0.05);
        product.addBand(band);

        addStreamContainer("ATB_R2_AOT", 1, tiffImageReader);
        band = new Band("ATB_R2_AOT", ProductData.TYPE_UINT8, atbHdr.getRasterWidth(), atbHdr.getRasterHeight());
        band.setScalingFactor(0.005);
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
