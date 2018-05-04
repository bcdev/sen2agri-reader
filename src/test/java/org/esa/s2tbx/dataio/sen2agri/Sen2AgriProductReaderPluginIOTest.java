package org.esa.s2tbx.dataio.sen2agri;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;

import static org.esa.snap.core.dataio.DecodeQualification.INTENDED;
import static org.esa.snap.core.dataio.DecodeQualification.UNABLE;
import static org.junit.Assert.assertEquals;

@RunWith(IOTestRunner.class)
public class Sen2AgriProductReaderPluginIOTest {

    private File testDataDirectory;

    @Before
    public void setUp() throws IOException {
        testDataDirectory = TestUtil.getTestDataDirectory();
    }

    @Test
    public void testGetDecodeQualification_S2A_MSIL2A_20171206T171701() {
        final File l2_hdr_file = new File(testDataDirectory, "S2A_MSIL2A_20171206T171701_N0206_R112_T14QKL_20171206T190908.SAFE/S2A_OPER_SSC_L2VALD_14QKL____20171206.HDR");

        final Sen2AgriProductReaderPlugIn plugIn = new Sen2AgriProductReaderPlugIn();

        assertEquals(INTENDED, plugIn.getDecodeQualification(l2_hdr_file));
    }

    @Test
    public void testGetDecodeQualification_S2A_MSIL2A_20171206T171701_wrongFile() {
        final File l2_hdr_file = new File(testDataDirectory, "S2A_MSIL2A_20171206T171701_N0206_R112_T14QKL_20171206T190908.SAFE/S2A_OPER_QCK_L2VALD_14QKL____20171206.HDR");

        final Sen2AgriProductReaderPlugIn plugIn = new Sen2AgriProductReaderPlugIn();

        assertEquals(UNABLE, plugIn.getDecodeQualification(l2_hdr_file));
    }

    @Test
    public void testGetDecodeQualification_S2B_MSIL2A_20171112T182559() {
        final File l2_hdr_file = new File(testDataDirectory, "S2B_MSIL2A_20171112T182559_N0206_R127_T11RMP_20171112T201023.SAFE/S2B_OPER_SSC_L2VALD_11RMP____20171112.HDR");

        final Sen2AgriProductReaderPlugIn plugIn = new Sen2AgriProductReaderPlugIn();

        assertEquals(INTENDED, plugIn.getDecodeQualification(l2_hdr_file));
    }

    @Test
    public void testGetDecodeQualification_S2B_MSIL2A_20171112T182559_wrongFile() {
        final File l2_hdr_file = new File(testDataDirectory, "S2B_MSIL2A_20171112T182559_N0206_R127_T11RMP_20171112T201023.SAFE/S2B_OPER_PMC_L2REPT_11RMP____20171112.EEF");

        final Sen2AgriProductReaderPlugIn plugIn = new Sen2AgriProductReaderPlugIn();

        assertEquals(UNABLE, plugIn.getDecodeQualification(l2_hdr_file));
    }

    @Test
    public void testGetDecodeQualification_S2A_USER_PRD_MSIL2A_PDMC_20160504T225639() {
        final File l2_hdr_file = new File(testDataDirectory, "S2A_USER_PRD_MSIL2A_PDMC_20160504T225639_R098_V20160504T175036_20160504T175036_T13REQ.SAFE/S2A_OPER_SSC_L2VALD_13REQ____20160504.HDR");

        final Sen2AgriProductReaderPlugIn plugIn = new Sen2AgriProductReaderPlugIn();

        assertEquals(INTENDED, plugIn.getDecodeQualification(l2_hdr_file));
    }

}
