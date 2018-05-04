package org.esa.s2tbx.dataio.sen2agri;

import org.esa.snap.core.dataio.ProductReader;
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
        } finally {
            product.dispose();
        }
    }
}
