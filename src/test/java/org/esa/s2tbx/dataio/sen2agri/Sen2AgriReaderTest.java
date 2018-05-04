package org.esa.s2tbx.dataio.sen2agri;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Sen2AgriReaderTest {

    @Test
    public void testGetDataDirName() {
        final GlobalHeader globalHeader = new GlobalHeader();

        globalHeader.setProductName("firlefanz_product");
        assertEquals("firlefanz_product.DBL.DIR", Sen2AgriProductReader.getDataDirName(globalHeader));

        globalHeader.setProductName("S2B_OPER_SSC_L2VALD_11RMP____20171112");
        assertEquals("S2B_OPER_SSC_L2VALD_11RMP____20171112.DBL.DIR", Sen2AgriProductReader.getDataDirName(globalHeader));
    }
}
