package org.esa.s2tbx.dataio.sen2agri;

import com.bc.ceres.core.ProgressMonitor;
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
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

public class Sen2AgriProductReader extends AbstractProductReader {

    private File inputFile;

    Sen2AgriProductReader(ProductReaderPlugIn readerPlugIn) {
        super(readerPlugIn);
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
    protected void readBandRasterDataImpl(int i, int i1, int i2, int i3, int i4, int i5, Band band, int i6, int i7, int i8, int i9, ProductData productData, ProgressMonitor progressMonitor) throws IOException {
        throw new RuntimeException("not implemented");
    }

    @Override
    public void readTiePointGridRasterData(TiePointGrid tpg, int destOffsetX, int destOffsetY, int destWidth, int destHeight, ProductData destBuffer, ProgressMonitor pm) throws IOException {
        throw new RuntimeException("not implemented");
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

    private void addBandsToProduct(Product product, GlobalHeader globalHeader) throws IOException {
        final String dataDirName = getDataDirName(globalHeader);
        final File dataDir = new File(inputFile.getParentFile(), dataDirName);
        if (!dataDir.isDirectory()) {
            throw new IOException("data directory '" + dataDirName + "' expected but not found");
        }

        final File atb_r1_tif = getFile(".*ATB_R1.DBL.TIF", dataDir);
        product.addBand("ATB_R1_VAP", ProductData.TYPE_UINT8);
        product.addBand("ATB_R1_AOT", ProductData.TYPE_UINT8);

        final File atb_r2_tif = getFile(".*ATB_R2.DBL.TIF", dataDir);
        product.addBand("ATB_R2_VAP", ProductData.TYPE_UINT8);
        product.addBand("ATB_R2_AOT", ProductData.TYPE_UINT8);

        final File cld_r1_tif = getFile(".*CLD_R1.DBL.TIF", dataDir);
        product.addBand("CLD_R1", ProductData.TYPE_UINT8);

        final File cld_r2_tif = getFile(".*CLD_R2.DBL.TIF", dataDir);
        product.addBand("CLD_R2", ProductData.TYPE_UINT8);

        final File msk_r1_tif = getFile(".*MSK_R1.DBL.TIF", dataDir);
        product.addBand("MSK_R1", ProductData.TYPE_UINT8);

        final File msk_r2_tif = getFile(".*MSK_R2.DBL.TIF", dataDir);
        product.addBand("MSK_R2", ProductData.TYPE_UINT8);

        final File qlt_r1_tif = getFile(".*QLT_R1.DBL.TIF", dataDir);
        product.addBand("QLT_R1_SAT", ProductData.TYPE_UINT8);
        product.addBand("QLT_R1_PIX", ProductData.TYPE_UINT8);
        product.addBand("QLT_R1_OTH", ProductData.TYPE_UINT8);

        final File qlt_r2_tif = getFile(".*QLT_R2.DBL.TIF", dataDir);
        product.addBand("QLT_R2_SAT", ProductData.TYPE_UINT8);
        product.addBand("QLT_R2_PIX", ProductData.TYPE_UINT8);
        product.addBand("QLT_R2_OTH", ProductData.TYPE_UINT8);

        final File fre_r1_tif = getFile(".*FRE_R1.DBL.TIF", dataDir);
        product.addBand("FRE_R1_B2", ProductData.TYPE_INT16);
        product.addBand("FRE_R1_B3", ProductData.TYPE_INT16);
        product.addBand("FRE_R1_B4", ProductData.TYPE_INT16);
        product.addBand("FRE_R1_B8", ProductData.TYPE_INT16);

        final File fre_r2_tif = getFile(".*FRE_R2.DBL.TIF", dataDir);
        product.addBand("FRE_R2_B5", ProductData.TYPE_INT16);
        product.addBand("FRE_R2_B6", ProductData.TYPE_INT16);
        product.addBand("FRE_R2_B7", ProductData.TYPE_INT16);
        product.addBand("FRE_R2_B8A", ProductData.TYPE_INT16);
        product.addBand("FRE_R2_B11", ProductData.TYPE_INT16);
        product.addBand("FRE_R2_B12", ProductData.TYPE_INT16);

        final File sre_r1_tif = getFile(".*SRE_R1.DBL.TIF", dataDir);
        product.addBand("SRE_R1_B2", ProductData.TYPE_INT16);
        product.addBand("SRE_R1_B3", ProductData.TYPE_INT16);
        product.addBand("SRE_R1_B4", ProductData.TYPE_INT16);
        product.addBand("SRE_R1_B8", ProductData.TYPE_INT16);

        final File sre_r2_tif = getFile(".*SRE_R2.DBL.TIF", dataDir);
        product.addBand("SRE_R2_B5", ProductData.TYPE_INT16);
        product.addBand("SRE_R2_B6", ProductData.TYPE_INT16);
        product.addBand("SRE_R2_B7", ProductData.TYPE_INT16);
        product.addBand("SRE_R2_B8A", ProductData.TYPE_INT16);
        product.addBand("SRE_R2_B11", ProductData.TYPE_INT16);
        product.addBand("SRE_R2_B12", ProductData.TYPE_INT16);


        //        final ImageInputStream imageInputStream = ImageIO.createImageInputStream(atb_r1_tif);
//        Iterator<ImageReader> imageReaders = ImageIO.getImageReaders(imageInputStream);
//        imageInputStream.close();


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
}
