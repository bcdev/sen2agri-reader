package org.esa.s2tbx.dataio.sen2agri;

import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class GlobalHeaderTest {

    @Test
    public void testParseAndGet() throws JDOMException, IOException {
        final Document document = createDocument();

        final GlobalHeader globalHeader = new GlobalHeader(document);
    }

    private Document createDocument() throws JDOMException, IOException {
        final SAXBuilder saxBuilder = new SAXBuilder();
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(XML_HEADER.getBytes());
        return saxBuilder.build(inputStream);
    }

    private static final String XML_HEADER = "<Earth_Explorer_Header xmlns=\"http://eop-cfi.esa.int/CFI\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" schema_version=\"1.00\" xsi:schemaLocation=\"http://eop-cfi.esa.int/CFI ./SSC_PDT_L2_L2PublishedProduct.xsd\" xsi:type=\"VSC_PDT_L2_Header_Type\">\n" +
            "</Earth_Explorer_Header>";
}
