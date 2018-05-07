package org.esa.s2tbx.dataio.sen2agri;

import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class ATBHeaderTest {

    @Test
    public void testParseAndGet() throws JDOMException, IOException {
        final Document document = createDocument();

        final ATBHeader header = new ATBHeader(document);
        assertEquals(10780, header.getRasterHeight());
        assertEquals(10980, header.getRasterWidth());
    }

    private Document createDocument() throws JDOMException, IOException {
        final SAXBuilder saxBuilder = new SAXBuilder();
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(XML_HEADER.getBytes());
        return saxBuilder.build(inputStream);
    }

    private static final String XML_HEADER = "<Earth_Explorer_Header xmlns=\"http://eop-cfi.esa.int/CFI\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" schema_version=\"1.00\" xsi:schemaLocation=\"http://eop-cfi.esa.int/CFI ./SSC_PDTANX_L2L3_AtmosphericAndBiophysicalProduct.xsd\" xsi:type=\"PDTANX_Header_Type\">\n" +
            "    <Variable_Header>\n" +
            "        <Specific_Product_Header>\n" +
            "            <Annex_Information>\n" +
            "                <Size>\n" +
            "                    <Lines>10780</Lines>\n" +
            "                    <Columns>10980</Columns>\n" +
            "                </Size>\n" +
            "            </Annex_Information>\n" +
            "        </Specific_Product_Header>\n" +
            "    </Variable_Header>\n" +
            "</Earth_Explorer_Header>\n";
}
