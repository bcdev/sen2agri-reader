package org.esa.s2tbx.dataio.sen2agri;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RegExFileNameFilterTest {

    @Test
    public void testAccept() {
        final RegExFileNameFilter fileNameFilter = new RegExFileNameFilter(".*ATB_R1.DBL.TIF");

        assertTrue(fileNameFilter.accept(null, "S2A_OPER_SSC_PDTANX_L2VALD_14QKL____20171206_ATB_R1.DBL.TIF"));
        assertFalse(fileNameFilter.accept(null, "S2A_OPER_SSC_PDTANX_L2VALD_14QKL____20171206_ATB_R1.HDR"));
    }
}
