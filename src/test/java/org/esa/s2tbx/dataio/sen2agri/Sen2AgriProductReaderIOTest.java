package org.esa.s2tbx.dataio.sen2agri;

import org.esa.snap.core.dataio.ProductReader;
import org.esa.snap.core.datamodel.Product;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;

import static org.esa.snap.core.dataio.DecodeQualification.INTENDED;
import static org.junit.Assert.assertEquals;

@RunWith(IOTestRunner.class)
public class Sen2AgriProductReaderIOTest {

    // @todo 2 tb/tb migrate to configurable path 2018-05-03
    private final String TEST_DATA_PATH = "D:/Satellite/LC-CCI/sen2agri_L2/";

    @Test
    public void testGetDecodeQualification_S2A_MSIL2A_20171206T171701() {
        final File l2_hdr_file = new File(TEST_DATA_PATH + "S2A_MSIL2A_20171206T171701_N0206_R112_T14QKL_20171206T190908.SAFE/S2A_OPER_SSC_L2VALD_14QKL____20171206.HDR");

        final Sen2AgriProductReaderPlugIn plugIn = new Sen2AgriProductReaderPlugIn();

        assertEquals(INTENDED, plugIn.getDecodeQualification(l2_hdr_file));
    }

    @Test
    public void testGetDecodeQualification_S2B_MSIL2A_20171112T182559() {
        final File l2_hdr_file = new File(TEST_DATA_PATH + "S2B_MSIL2A_20171112T182559_N0206_R127_T11RMP_20171112T201023.SAFE/S2B_OPER_SSC_L2VALD_11RMP____20171112.HDR");

        final Sen2AgriProductReaderPlugIn plugIn = new Sen2AgriProductReaderPlugIn();

        assertEquals(INTENDED, plugIn.getDecodeQualification(l2_hdr_file));
    }

    @Test
    public void testGetDecodeQualification_S2B_MSIL2A_20180105T162649() {
        final File l2_hdr_file = new File(TEST_DATA_PATH + "S2B_MSIL2A_20180105T162649_N0206_R040_T16QBF_20180105T182334.SAFE/S2B_OPER_SSC_L2VALD_16QBF____20180105.HDR");

        final Sen2AgriProductReaderPlugIn plugIn = new Sen2AgriProductReaderPlugIn();

        assertEquals(INTENDED, plugIn.getDecodeQualification(l2_hdr_file));
    }

    @Test
    public void testReadProductNodesImpl_S2A_MSIL2A_20171206T171701() throws IOException {
        final File l2_hdr_file = new File(TEST_DATA_PATH + "S2A_MSIL2A_20171206T171701_N0206_R112_T14QKL_20171206T190908.SAFE/S2A_OPER_SSC_L2VALD_14QKL____20171206.HDR");

        final Sen2AgriProductReaderPlugIn plugIn = new Sen2AgriProductReaderPlugIn();
        final ProductReader reader = plugIn.createReaderInstance();

        final Product product = reader.readProductNodes(l2_hdr_file, null);
        try {

        } finally {
            product.dispose();
        }
    }
}
