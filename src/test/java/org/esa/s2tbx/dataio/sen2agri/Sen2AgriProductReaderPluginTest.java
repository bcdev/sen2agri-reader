package org.esa.s2tbx.dataio.sen2agri;

import org.esa.snap.core.dataio.ProductReader;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
        assertEquals("S2_AGRI_SSC", formatNames[0]);
    }
}
