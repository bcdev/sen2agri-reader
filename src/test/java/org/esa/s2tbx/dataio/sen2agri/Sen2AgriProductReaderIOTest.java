package org.esa.s2tbx.dataio.sen2agri;

import org.esa.snap.core.dataio.ProductReader;
import org.esa.snap.core.datamodel.Band;
import org.esa.snap.core.datamodel.Product;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

@RunWith(IOTestRunner.class)
public class Sen2AgriProductReaderIOTest {

    private File testDataDirectory;

    @Before
    public void setUp() throws IOException {
        testDataDirectory = TestUtil.getTestDataDirectory();
    }

    @Test
    public void testReadProductNodesImpl_S2A_MSIL2A_20171206T171701() throws IOException {
        final File l2_hdr_file = new File(testDataDirectory, "S2A_MSIL2A_20171206T171701_N0206_R112_T14QKL_20171206T190908.SAFE/S2A_OPER_SSC_L2VALD_14QKL____20171206.HDR");

        final Sen2AgriProductReaderPlugIn plugIn = new Sen2AgriProductReaderPlugIn();
        final ProductReader reader = plugIn.createReaderInstance();

        final Product product = reader.readProductNodes(l2_hdr_file, null);
        try {
            assertEquals(10980, product.getSceneRasterWidth());
            assertEquals(10980, product.getSceneRasterHeight());
            assertEquals("S2_AGRI_SSC_L2VALD", product.getProductType());
            assertEquals("S2A_OPER_SSC_L2VALD_14QKL____20171206", product.getName());

            assertEquals("06-DEC-2017 17:17:04.000000", product.getStartTime().toString());
            assertEquals("06-DEC-2017 17:17:04.000000", product.getEndTime().toString());

            final String[] bandNames = product.getBandNames();
            assertEquals(34, bandNames.length);
            assertEquals("ATB_R1_VAP", bandNames[0]);
            assertEquals("QLT_R2_SAT", bandNames[11]);
            assertEquals("FRE_R2_B11", bandNames[22]);

            final float[] buffer = new float[16];
            final Band atb_r1_vap = product.getBand("ATB_R1_VAP");
            assertEquals(0, atb_r1_vap.getNoDataValue(), 1e-8);
            atb_r1_vap.readPixels(3000, 3000, 4, 4, buffer);
            assertEquals(10.25, buffer[0], 1e-8);
            assertEquals(10.300000190734863, buffer[1], 1e-8);
            assertEquals(10.300000190734863, buffer[2], 1e-8);
            assertEquals(10.300000190734863, buffer[3], 1e-8);

            final Band atb_r1_aot = product.getBand("ATB_R1_AOT");
            assertEquals(0, atb_r1_aot.getNoDataValue(), 1e-8);
            atb_r1_aot.readPixels(3010, 3010, 4, 4, buffer);
            assertEquals(0.12999999523162842, buffer[0], 1e-8);
            assertEquals(0.12999999523162842, buffer[1], 1e-8);
            assertEquals(0.12999999523162842, buffer[2], 1e-8);
            assertEquals(0.12999999523162842, buffer[3], 1e-8);
        } finally {
            product.dispose();
        }
    }

    @Test
    public void testReadProductNodesImpl_S2B_MSIL2A_20171112T182559() throws IOException {
        final File l2_hdr_file = new File(testDataDirectory, "S2B_MSIL2A_20171112T182559_N0206_R127_T11RMP_20171112T201023.SAFE/S2B_OPER_SSC_L2VALD_11RMP____20171112.HDR");

        final Sen2AgriProductReaderPlugIn plugIn = new Sen2AgriProductReaderPlugIn();
        final ProductReader reader = plugIn.createReaderInstance();

        final Product product = reader.readProductNodes(l2_hdr_file, null);
        try {
            assertEquals(10980, product.getSceneRasterWidth());
            assertEquals(10980, product.getSceneRasterHeight());
            assertEquals("S2_AGRI_SSC_L2VALD", product.getProductType());
            assertEquals("S2B_OPER_SSC_L2VALD_11RMP____20171112", product.getName());

            assertEquals("12-NOV-2017 18:29:37.000000", product.getStartTime().toString());
            assertEquals("12-NOV-2017 18:29:37.000000", product.getEndTime().toString());

            final String[] bandNames = product.getBandNames();
            assertEquals(34, bandNames.length);
            assertEquals("ATB_R1_AOT", bandNames[1]);
            assertEquals("QLT_R2_PIX", bandNames[12]);
            assertEquals("FRE_R2_B12", bandNames[23]);

            final float[] buffer = new float[16];
            final Band atb_r2_vap = product.getBand("ATB_R2_VAP");
            assertEquals(0, atb_r2_vap.getNoDataValue(), 1e-8);
            atb_r2_vap.readPixels(3020, 3020, 4, 4, buffer);
            assertEquals(10.800000190734863, buffer[4], 1e-8);
            assertEquals(10.800000190734863, buffer[5], 1e-8);
            assertEquals(10.800000190734863, buffer[6], 1e-8);
            assertEquals(10.800000190734863, buffer[7], 1e-8);

            final Band atb_r2_aot = product.getBand("ATB_R2_AOT");
            assertEquals(0, atb_r2_aot.getNoDataValue(), 1e-8);
            atb_r2_aot.readPixels(3030, 3030, 4, 4, buffer);
            assertEquals(0.10000000149011612, buffer[8], 1e-8);
            assertEquals(0.10000000149011612, buffer[9], 1e-8);
            assertEquals(0.10000000149011612, buffer[10], 1e-8);
            assertEquals(0.10000000149011612, buffer[11], 1e-8);
        } finally {
            product.dispose();
        }
    }

    @Test
    public void testReadProductNodesImpl_S2A_USER_PRD_MSIL2A_PDMC_20160504T225639() throws IOException {
        final File l2_hdr_file = new File(testDataDirectory, "S2A_USER_PRD_MSIL2A_PDMC_20160504T225639_R098_V20160504T175036_20160504T175036_T13REQ.SAFE/S2A_OPER_SSC_L2VALD_13REQ____20160504.HDR");

        final Sen2AgriProductReaderPlugIn plugIn = new Sen2AgriProductReaderPlugIn();
        final ProductReader reader = plugIn.createReaderInstance();

        final Product product = reader.readProductNodes(l2_hdr_file, null);
        try {
            assertEquals(10980, product.getSceneRasterWidth());
            assertEquals(10980, product.getSceneRasterHeight());
            assertEquals("S2_AGRI_SSC_L2VALD", product.getProductType());
            assertEquals("S2A_OPER_SSC_L2VALD_13REQ____20160504", product.getName());

            assertEquals("04-MAY-2016 17:50:36.000000", product.getStartTime().toString());
            assertEquals("04-MAY-2016 17:50:36.000000", product.getEndTime().toString());

            final String[] bandNames = product.getBandNames();
            assertEquals(34, bandNames.length);
            assertEquals("ATB_R2_VAP", bandNames[2]);
            assertEquals("QLT_R2_OTH", bandNames[13]);
            assertEquals("SRE_R1_B2", bandNames[24]);

            final int[] buffer = new int[16];
            final Band cld_r1 = product.getBand("CLD_R1");
            cld_r1.readPixels(3040, 3040, 4, 4, buffer);
            assertEquals(0, buffer[8]);
            assertEquals(0, buffer[9]);
            assertEquals(0, buffer[10]);
            assertEquals(0, buffer[11]);
        } finally {
            product.dispose();
        }
    }

    @Test
    public void testReadProductNodesImpl_S2A_USER_PRD_MSIL2A_PDMC_20160724T222448() throws IOException {
        final File l2_hdr_file = new File(testDataDirectory, "S2A_USER_PRD_MSIL2A_PDMC_20160724T222448_R112_V20160724T171302_20160724T172601_T13QGV.SAFE/S2A_OPER_SSC_L2VALD_13QGV____20160724.HDR");

        final Sen2AgriProductReaderPlugIn plugIn = new Sen2AgriProductReaderPlugIn();
        final ProductReader reader = plugIn.createReaderInstance();

        final Product product = reader.readProductNodes(l2_hdr_file, null);
        try {
            assertEquals(10980, product.getSceneRasterWidth());
            assertEquals(10980, product.getSceneRasterHeight());
            assertEquals("S2_AGRI_SSC_L2VALD", product.getProductType());
            assertEquals("S2A_OPER_SSC_L2VALD_13QGV____20160724", product.getName());

            assertEquals("24-JUL-2016 17:26:01.000000", product.getStartTime().toString());
            assertEquals("24-JUL-2016 17:26:01.000000", product.getEndTime().toString());

            final String[] bandNames = product.getBandNames();
            assertEquals(34, bandNames.length);
            assertEquals("ATB_R2_AOT", bandNames[3]);
            assertEquals("FRE_R1_B2", bandNames[14]);
            assertEquals("SRE_R1_B3", bandNames[25]);

            final int[] buffer = new int[16];
            final Band cld_r1 = product.getBand("CLD_R2");
            cld_r1.readPixels(3050, 3050, 4, 4, buffer);
            assertEquals(19, buffer[12]);
            assertEquals(19, buffer[13]);
            assertEquals(19, buffer[14]);
            assertEquals(19, buffer[15]);
        } finally {
            product.dispose();
        }
    }
}
