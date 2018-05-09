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

    @Test
    public void testCreate_SAT_R1_FlagCoding() {
        final FlagCoding flagCoding = Sen2AgriProductReader.create_SAT_R1_FlagCoding();
        assertNotNull(flagCoding);

        assertEquals("SAT_R1", flagCoding.getName());
        assertEquals("Saturation mask copied from L1", flagCoding.getDescription());
        assertEquals(4, flagCoding.getFlagNames().length);

        MetadataAttribute flag = flagCoding.getFlag("B4");
        assertEquals(0x4, ((int[])flag.getDataElems())[0]);
        assertEquals("B4", flag.getName());
        assertEquals("B4 saturated", flag.getDescription());

        flag = flagCoding.getFlag("B8");
        assertEquals(0x8, ((int[])flag.getDataElems())[0]);
        assertEquals("B8", flag.getName());
        assertEquals("B8 saturated", flag.getDescription());
    }

    @Test
    public void testCreate_SAT_R2_FlagCoding() {
        final FlagCoding flagCoding = Sen2AgriProductReader.create_SAT_R2_FlagCoding();
        assertNotNull(flagCoding);

        assertEquals("SAT_R2", flagCoding.getName());
        assertEquals("Saturation mask copied from L1", flagCoding.getDescription());
        assertEquals(6, flagCoding.getFlagNames().length);

        MetadataAttribute flag = flagCoding.getFlag("B5");
        assertEquals(0x1, ((int[])flag.getDataElems())[0]);
        assertEquals("B5", flag.getName());
        assertEquals("B5 saturated", flag.getDescription());

        flag = flagCoding.getFlag("B8A");
        assertEquals(0x8, ((int[])flag.getDataElems())[0]);
        assertEquals("B8A", flag.getName());
        assertEquals("B8A saturated", flag.getDescription());

        flag = flagCoding.getFlag("B12");
        assertEquals(0x20, ((int[])flag.getDataElems())[0]);
        assertEquals("B12", flag.getName());
        assertEquals("B12 saturated", flag.getDescription());
    }

    @Test
    public void testCreate_PIX_R1_FlagCoding() {
        final FlagCoding flagCoding = Sen2AgriProductReader.create_PIX_R1_FlagCoding();
        assertNotNull(flagCoding);

        assertEquals("PIX_R1", flagCoding.getName());
        assertEquals("aberrant pixels channel copied from level 1", flagCoding.getDescription());
        assertEquals(4, flagCoding.getFlagNames().length);

        MetadataAttribute flag = flagCoding.getFlag("B8");
        assertEquals(0x8, ((int[])flag.getDataElems())[0]);
        assertEquals("B8", flag.getName());
        assertEquals("B8 aberrant pixel", flag.getDescription());

        flag = flagCoding.getFlag("B2");
        assertEquals(0x1, ((int[])flag.getDataElems())[0]);
        assertEquals("B2", flag.getName());
        assertEquals("B2 aberrant pixel", flag.getDescription());
    }

    @Test
    public void testCreate_PIX_R2_FlagCoding() {
        final FlagCoding flagCoding = Sen2AgriProductReader.create_PIX_R2_FlagCoding();
        assertNotNull(flagCoding);

        assertEquals("PIX_R2", flagCoding.getName());
        assertEquals("aberrant pixels channel copied from level 1", flagCoding.getDescription());
        assertEquals(6, flagCoding.getFlagNames().length);

        MetadataAttribute flag = flagCoding.getFlag("B6");
        assertEquals(0x2, ((int[])flag.getDataElems())[0]);
        assertEquals("B6", flag.getName());
        assertEquals("B6 aberrant pixel", flag.getDescription());

        flag = flagCoding.getFlag("B11");
        assertEquals(0x10, ((int[])flag.getDataElems())[0]);
        assertEquals("B11", flag.getName());
        assertEquals("B11 aberrant pixel", flag.getDescription());

        flag = flagCoding.getFlag("B5");
        assertEquals(0x1, ((int[])flag.getDataElems())[0]);
        assertEquals("B5", flag.getName());
        assertEquals("B5 aberrant pixel", flag.getDescription());
    }

    @Test
    public void testCreate_OTH_FlagCoding() {
        final FlagCoding flagCoding = Sen2AgriProductReader.create_OTH_FlagCoding();
        assertNotNull(flagCoding);

        assertEquals("OTH", flagCoding.getName());
        assertEquals(2, flagCoding.getFlagNames().length);

        MetadataAttribute flag = flagCoding.getFlag("EDG");
        assertEquals(0x1, ((int[])flag.getDataElems())[0]);
        assertEquals("EDG", flag.getName());
        assertEquals("Edge mask", flag.getDescription());

        flag = flagCoding.getFlag("TAO");
        assertEquals(0x2, ((int[])flag.getDataElems())[0]);
        assertEquals("TAO", flag.getName());
        assertEquals("AOT pixel mask (0 if computed, 1 if interpolated)", flag.getDescription());
    }

}
