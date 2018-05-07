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

            final float[] floatBuffer = new float[16];
            final Band atb_r1_vap = product.getBand("ATB_R1_VAP");
            assertEquals(0, atb_r1_vap.getNoDataValue(), 1e-8);
            atb_r1_vap.readPixels(3000, 3000, 4, 4, floatBuffer);
            assertEquals(10.25, floatBuffer[0], 1e-8);
            assertEquals(10.300000190734863, floatBuffer[1], 1e-8);
            assertEquals(10.300000190734863, floatBuffer[2], 1e-8);
            assertEquals(10.300000190734863, floatBuffer[3], 1e-8);

            final Band atb_r1_aot = product.getBand("ATB_R1_AOT");
            assertEquals(0, atb_r1_aot.getNoDataValue(), 1e-8);
            atb_r1_aot.readPixels(3010, 3010, 4, 4, floatBuffer);
            assertEquals(0.12999999523162842, floatBuffer[0], 1e-8);
            assertEquals(0.12999999523162842, floatBuffer[1], 1e-8);
            assertEquals(0.12999999523162842, floatBuffer[2], 1e-8);
            assertEquals(0.12999999523162842, floatBuffer[3], 1e-8);

            final int[] intBuffer = new int[16];
            final Band msk_r1 = product.getBand("MSK_R1");
            msk_r1.readPixels(3050, 3050, 4, 4, intBuffer);
            assertEquals(0, intBuffer[12]);
            assertEquals(0, intBuffer[13]);
            assertEquals(0, intBuffer[14]);
            assertEquals(0, intBuffer[15]);

            final Band qlt_r1_pix = product.getBand("QLT_R1_OTH");
            qlt_r1_pix.readPixels(3070, 3070, 4, 4, intBuffer);
            assertEquals(0, intBuffer[4]);
            assertEquals(0, intBuffer[5]);
            assertEquals(0, intBuffer[6]);
            assertEquals(0, intBuffer[7]);

            final Band fre_r1_b2 = product.getBand("FRE_R1_B2");
            assertEquals(-10000, fre_r1_b2.getNoDataValue(), 1e-8);
            fre_r1_b2.readPixels(3080, 3080, 4, 4, floatBuffer);
            assertEquals(0.08060000091791153, floatBuffer[8], 1e-8);
            assertEquals(0.08370000123977661, floatBuffer[9], 1e-8);
            assertEquals(0.08110000193119049, floatBuffer[10], 1e-8);
            assertEquals(0.0786999985575676, floatBuffer[11], 1e-8);

            final Band sre_r1_b8 = product.getBand("SRE_R1_B8");
            assertEquals(-10000, sre_r1_b8.getNoDataValue(), 1e-8);
            sre_r1_b8.readPixels(3110, 3110, 4, 4, floatBuffer);
            assertEquals(0.25859999656677246, floatBuffer[4], 1e-8);
            assertEquals(0.2567000091075897, floatBuffer[5], 1e-8);
            assertEquals(0.25369998812675476, floatBuffer[6], 1e-8);
            assertEquals(0.2533999979496002, floatBuffer[7], 1e-8);
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

            final float[] floatBuffer = new float[16];
            final Band atb_r2_vap = product.getBand("ATB_R2_VAP");
            assertEquals(0, atb_r2_vap.getNoDataValue(), 1e-8);
            atb_r2_vap.readPixels(3020, 3020, 4, 4, floatBuffer);
            assertEquals(10.800000190734863, floatBuffer[4], 1e-8);
            assertEquals(10.800000190734863, floatBuffer[5], 1e-8);
            assertEquals(10.800000190734863, floatBuffer[6], 1e-8);
            assertEquals(10.800000190734863, floatBuffer[7], 1e-8);

            final Band atb_r2_aot = product.getBand("ATB_R2_AOT");
            assertEquals(0, atb_r2_aot.getNoDataValue(), 1e-8);
            atb_r2_aot.readPixels(3030, 3030, 4, 4, floatBuffer);
            assertEquals(0.10000000149011612, floatBuffer[8], 1e-8);
            assertEquals(0.10000000149011612, floatBuffer[9], 1e-8);
            assertEquals(0.10000000149011612, floatBuffer[10], 1e-8);
            assertEquals(0.10000000149011612, floatBuffer[11], 1e-8);

            final int[] intBuffer = new int[16];
            final Band msk_r2 = product.getBand("MSK_R2");
            msk_r2.readPixels(3060, 3060, 4, 4, intBuffer);
            assertEquals(1, intBuffer[0]);
            assertEquals(1, intBuffer[1]);
            assertEquals(1, intBuffer[2]);
            assertEquals(1, intBuffer[3]);

            final Band qlt_r2_sat = product.getBand("QLT_R2_SAT");
            qlt_r2_sat.readPixels(3080, 3080, 4, 4, intBuffer);
            assertEquals(0, intBuffer[8]);
            assertEquals(0, intBuffer[9]);
            assertEquals(0, intBuffer[10]);
            assertEquals(0, intBuffer[11]);

            final Band fre_r1_b3 = product.getBand("FRE_R1_B3");
            assertEquals(-10000, fre_r1_b3.getNoDataValue(), 1e-8);
            fre_r1_b3.readPixels(3090, 3090, 4, 4, floatBuffer);
            assertEquals(0.010599999688565731, floatBuffer[12], 1e-8);
            assertEquals(0.008700000122189522, floatBuffer[13], 1e-8);
            assertEquals(0.007799999788403511, floatBuffer[14], 1e-8);
            assertEquals(0.010400000028312206, floatBuffer[15], 1e-8);

            final Band sre_r2_b5 = product.getBand("SRE_R2_B5");
            assertEquals(-10000, sre_r2_b5.getNoDataValue(), 1e-8);
            sre_r2_b5.readPixels(3120, 3120, 4, 4, floatBuffer);
            assertEquals(0.01360000018030405, floatBuffer[8], 1e-8);
            assertEquals(0.013100000098347664, floatBuffer[9], 1e-8);
            assertEquals(0.014299999922513962, floatBuffer[10], 1e-8);
            assertEquals(0.014000000432133675, floatBuffer[11], 1e-8);
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

            final int[] intBuffer = new int[16];
            final Band cld_r1 = product.getBand("CLD_R1");
            cld_r1.readPixels(3040, 3040, 4, 4, intBuffer);
            assertEquals(0, intBuffer[8]);
            assertEquals(0, intBuffer[9]);
            assertEquals(0, intBuffer[10]);
            assertEquals(0, intBuffer[11]);

            final Band qlt_r1_sat = product.getBand("QLT_R1_SAT");
            qlt_r1_sat.readPixels(3050, 3050, 4, 4, intBuffer);
            assertEquals(0, intBuffer[12]);
            assertEquals(0, intBuffer[13]);
            assertEquals(0, intBuffer[14]);
            assertEquals(0, intBuffer[15]);

            final Band qlt_r2_pix = product.getBand("QLT_R2_PIX");
            qlt_r2_pix.readPixels(3090, 3090, 4, 4, intBuffer);
            assertEquals(0, intBuffer[12]);
            assertEquals(0, intBuffer[13]);
            assertEquals(0, intBuffer[14]);
            assertEquals(0, intBuffer[15]);

            final float[] floatBuffer = new float[16];
            final Band fre_r1_b4 = product.getBand("FRE_R1_B4");
            assertEquals(-10000, fre_r1_b4.getNoDataValue(), 1e-8);
            fre_r1_b4.readPixels(3100, 3100, 4, 4, floatBuffer);
            assertEquals(0.2328999936580658, floatBuffer[0], 1e-8);
            assertEquals(0.2084999978542328, floatBuffer[1], 1e-8);
            assertEquals(0.22259999811649323, floatBuffer[2], 1e-8);
            assertEquals(0.19259999692440033, floatBuffer[3], 1e-8);

            final Band sre_r2_b6 = product.getBand("SRE_R2_B6");
            assertEquals(-10000, sre_r2_b6.getNoDataValue(), 1e-8);
            sre_r2_b6.readPixels(3130, 3130, 4, 4, floatBuffer);
            assertEquals(-1.0, floatBuffer[8], 1e-8);
            assertEquals(-1.0, floatBuffer[9], 1e-8);
            assertEquals(-1.0, floatBuffer[10], 1e-8);
            assertEquals(-1.0, floatBuffer[11], 1e-8);
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

            final int[] intBuffer = new int[16];
            final Band cld_r1 = product.getBand("CLD_R2");
            cld_r1.readPixels(3050, 3050, 4, 4, intBuffer);
            assertEquals(19, intBuffer[12]);
            assertEquals(19, intBuffer[13]);
            assertEquals(19, intBuffer[14]);
            assertEquals(19, intBuffer[15]);

            final Band qlt_r1_pix = product.getBand("QLT_R1_PIX");
            qlt_r1_pix.readPixels(3060, 3060, 4, 4, intBuffer);
            assertEquals(0, intBuffer[0]);
            assertEquals(0, intBuffer[1]);
            assertEquals(0, intBuffer[2]);
            assertEquals(0, intBuffer[3]);

            final Band qlt_r2_oth = product.getBand("QLT_R2_OTH");
            qlt_r2_oth.readPixels(3100, 3100, 4, 4, intBuffer);
            assertEquals(2, intBuffer[0]);
            assertEquals(2, intBuffer[1]);
            assertEquals(2, intBuffer[2]);
            assertEquals(2, intBuffer[3]);

            final float[] floatBuffer = new float[16];
            final Band sre_r2_b7 = product.getBand("SRE_R2_B7");
            assertEquals(-10000, sre_r2_b7.getNoDataValue(), 1e-8);
            sre_r2_b7.readPixels(3140, 3140, 4, 4, floatBuffer);
            assertEquals(0.1289999932050705, floatBuffer[8], 1e-8);
            assertEquals(0.155799999833107, floatBuffer[9], 1e-8);
            assertEquals(0.24889999628067017, floatBuffer[10], 1e-8);
            assertEquals(0.325300008058548, floatBuffer[11], 1e-8);
        } finally {
            product.dispose();
        }
    }
}
