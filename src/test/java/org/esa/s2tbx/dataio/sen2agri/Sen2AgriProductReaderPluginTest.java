package org.esa.s2tbx.dataio.sen2agri;

import org.esa.snap.core.dataio.ProductReader;
import org.esa.snap.core.util.io.SnapFileFilter;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Locale;

import static org.junit.Assert.*;

public class Sen2AgriProductReaderPluginTest {

    private Sen2AgriProductReaderPlugIn plugIn;

    @Before
    public void setUp() {
        plugIn = new Sen2AgriProductReaderPlugIn();
    }

    @Test
    public void testInputTypes() {
        final Class[] inputTypes = plugIn.getInputTypes();
        assertNotNull(inputTypes);
        assertEquals(2, inputTypes.length);
        assertEquals(String.class, inputTypes[0]);
        assertEquals(File.class, inputTypes[1]);
    }

    @Test
    public void testCreateReaderInstance() {
        final ProductReader readerInstance = plugIn.createReaderInstance();
        assertNotNull(readerInstance);
        assertTrue(readerInstance instanceof Sen2AgriProductReader);
    }

    @Test
    public void testGetFormatNames() {
        final String[] formatNames = plugIn.getFormatNames();
        assertNotNull(formatNames);
        assertEquals(1, formatNames.length);
        assertEquals("S2_AGRI_SSC_L2VALD", formatNames[0]);
    }

    @Test
    public void testGetDefaultFileExtension() {
        final String[] defaultFileExtensions = plugIn.getDefaultFileExtensions();
        assertNotNull(defaultFileExtensions);
        assertEquals(1, defaultFileExtensions.length);
        assertEquals(".HDR", defaultFileExtensions[0]);
    }

    @Test
    public void testGetDescription() {
        String description = plugIn.getDescription(Locale.getDefault());
        assertNotNull(description);
        assertEquals("Sen2Agri SSC_L2VALD Level 2 Data Products", description);

        description = plugIn.getDescription(null);
        assertNotNull(description);
        assertEquals("Sen2Agri SSC_L2VALD Level 2 Data Products", description);
    }

    @Test
    public void testGetProductFileFilter() {
        final SnapFileFilter productFileFilter = plugIn.getProductFileFilter();
        assertNotNull(productFileFilter);

        assertEquals("S2_AGRI_SSC_L2VALD", productFileFilter.getFormatName());
        assertEquals(".HDR", productFileFilter.getDefaultExtension());
        assertEquals("Sen2Agri SSC_L2VALD Level 2 Data Products (*.HDR)", productFileFilter.getDescription());
    }

    @Test
    public void testGetInputFile_nullInput() {
        assertNull(Sen2AgriProductReaderPlugIn.getInputFile(null));
    }

    @Test
    public void testGetInputFile_stringInput() {
        String testFileName = "test.file";
        final File file = Sen2AgriProductReaderPlugIn.getInputFile(testFileName);
        assertNotNull(file);
        assertEquals(testFileName, file.getName());
    }

    @Test
    public void testGetInputFile_fileInput() {
        final File inputFile = new File("I_am_a.file");
        final File file = Sen2AgriProductReaderPlugIn.getInputFile(inputFile);
        assertNotNull(file);
        assertEquals(inputFile.getName(), file.getName());
    }

    @Test
    public void testHasHDRExtension() {
        assertFalse(Sen2AgriProductReaderPlugIn.hasHDRExtension(null));
        assertFalse(Sen2AgriProductReaderPlugIn.hasHDRExtension(new File("high_speed_hack.ing")));
        assertTrue(Sen2AgriProductReaderPlugIn.hasHDRExtension(new File("should_do_the_work.HDR")));
    }

    @Test
    public void testMatchesRegEx() {
        assertTrue(Sen2AgriProductReaderPlugIn.matchesRegEx(new File("S2A_OPER_SSC_L2VALD_14QKL____20171206.HDR")));
        assertTrue(Sen2AgriProductReaderPlugIn.matchesRegEx(new File("S2B_OPER_SSC_L2VALD_11RMP____20171112.HDR")));
        assertTrue(Sen2AgriProductReaderPlugIn.matchesRegEx(new File("S2B_OPER_SSC_L2VALD_16QBF____20180105.HDR")));

        assertFalse(Sen2AgriProductReaderPlugIn.matchesRegEx(new File("S2B_OPER_SSC_L2VALD_16QBF____20180105.DBL")));
        assertFalse(Sen2AgriProductReaderPlugIn.matchesRegEx(new File("S2B_OPER_QCK_L2VALD_11RMP____20171112.HDR")));
        assertFalse(Sen2AgriProductReaderPlugIn.matchesRegEx(new File("S2A_OPER_PMC_L2REPT_14QKL____20171206.EEF")));
    }
}
