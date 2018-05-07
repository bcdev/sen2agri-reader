package org.esa.s2tbx.dataio.sen2agri;

import org.jdom.Document;
import org.jdom.JDOMException;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class CLDHeaderTest {

    @Test
    public void testParseAndGet() throws JDOMException, IOException {
        final Document document = TestUtil.createDocument(XML_HEADER);

        final CLDHeader header = new CLDHeader(document);

        assertEquals(10880, header.getRasterHeight());
        assertEquals(10580, header.getRasterWidth());
    }

    private static final String XML_HEADER = "<Earth_Explorer_Header xmlns=\"http://eop-cfi.esa.int/CFI\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" schema_version=\"1.00\" xsi:schemaLocation=\"http://eop-cfi.esa.int/CFI ./SSC_PDTANX_L2L3_AtmosphericAndBiophysicalProduct.xsd\" xsi:type=\"PDTANX_Header_Type\">\n" +
            "    <Variable_Header>\n" +
            "        <Specific_Product_Header>\n" +
            "            <Annex_Information>\n" +
            "                <Size>\n" +
            "                    <Lines>10880</Lines>\n" +
            "                    <Columns>10580</Columns>\n" +
            "                </Size>\n" +
            "            </Annex_Information>\n" +
            "        </Specific_Product_Header>\n" +
            "    </Variable_Header>\n" +
            "</Earth_Explorer_Header>\n";
}
