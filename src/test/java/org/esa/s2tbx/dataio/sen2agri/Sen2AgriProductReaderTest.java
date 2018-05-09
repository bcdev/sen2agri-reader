package org.esa.s2tbx.dataio.sen2agri;

import org.esa.snap.core.datamodel.FlagCoding;
import org.esa.snap.core.datamodel.MetadataAttribute;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

public class Sen2AgriProductReaderTest {

    @Test
    public void testGetDataDirName() {
        final GlobalHeader globalHeader = new GlobalHeader();

        globalHeader.setProductName("firlefanz_product");
        assertEquals("firlefanz_product.DBL.DIR", Sen2AgriProductReader.getDataDirName(globalHeader));

        globalHeader.setProductName("S2B_OPER_SSC_L2VALD_11RMP____20171112");
        assertEquals("S2B_OPER_SSC_L2VALD_11RMP____20171112.DBL.DIR", Sen2AgriProductReader.getDataDirName(globalHeader));
    }

    @Test
    public void testCreateCloudFlagCoding() {
        final FlagCoding flagCoding = Sen2AgriProductReader.createCloudFlagCoding();
        assertNotNull(flagCoding);

        assertEquals("CLD", flagCoding.getName());
        assertEquals("Cloud and cloud shadow mask", flagCoding.getDescription());
        assertEquals(8, flagCoding.getFlagNames().length);

        MetadataAttribute flag = flagCoding.getFlag("ALL");
        assertEquals(0x1, ((int[])flag.getDataElems())[0]);
        assertEquals("ALL", flag.getName());
        assertEquals("Summary Logical or of All cloud and shadow masks", flag.getDescription());

        flag = flagCoding.getFlag("SHADVAR");
        assertEquals(0x8, ((int[])flag.getDataElems())[0]);
        assertEquals("SHADVAR", flag.getName());
        assertEquals("Shadows mask from clouds outside image", flag.getDescription());
    }

    @Test
    public void testCreateGeophysicalFlagCoding() {
        final FlagCoding flagCoding = Sen2AgriProductReader.createGeophysicalFlagCoding();
        assertNotNull(flagCoding);

        assertEquals("MSK", flagCoding.getName());
        assertEquals("Geophysical masks", flagCoding.getDescription());
        assertEquals(6, flagCoding.getFlagNames().length);

        MetadataAttribute flag = flagCoding.getFlag("HID");
        assertEquals(0x2, ((int[])flag.getDataElems())[0]);
        assertEquals("HID", flag.getName());
        assertEquals("Hidden surfaces", flag.getDescription());

        flag = flagCoding.getFlag("STL");
        assertEquals(0x8, ((int[])flag.getDataElems())[0]);
        assertEquals("STL", flag.getName());
        assertEquals("sun too low flag", flag.getDescription());

        flag = flagCoding.getFlag("SNW");
        assertEquals(0x20, ((int[])flag.getDataElems())[0]);
        assertEquals("SNW", flag.getName());
        assertEquals("Snow", flag.getDescription());
    }
}
