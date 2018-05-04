package org.esa.s2tbx.dataio.sen2agri;

import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class GlobalHeaderTest {

    @Test
    public void testParseAndGet() throws JDOMException, IOException {
        final Document document = createDocument();

        final GlobalHeader globalHeader = new GlobalHeader(document);
        assertEquals(10980, globalHeader.getSceneRasterHeight());
        assertEquals(10980, globalHeader.getSceneRasterWidth());
    }

    private Document createDocument() throws JDOMException, IOException {
        final SAXBuilder saxBuilder = new SAXBuilder();
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(XML_HEADER.getBytes());
        return saxBuilder.build(inputStream);
    }

    private static final String XML_HEADER = "<Earth_Explorer_Header xmlns=\"http://eop-cfi.esa.int/CFI\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" schema_version=\"1.00\" xsi:schemaLocation=\"http://eop-cfi.esa.int/CFI ./SSC_PDT_L2_L2PublishedProduct.xsd\" xsi:type=\"VSC_PDT_L2_Header_Type\">\n" +
            "    <Variable_Header>\n" +
            "        <Specific_Product_Header>\n" +
            "            <Image_Information>\n" +
            "                <List_of_Resolutions count=\"2\">\n" +
            "                    <Resolution r=\"10\">\n" +
            "                        <Size>\n" +
            "                            <Lines>10980</Lines>\n" +
            "                            <Columns>10980</Columns\n>" +
            "                        </Size>\n" +
            "                    </Resolution>\n" +
            "                    <Resolution r=\"20\">\n" +
            "                        <Size>\n" +
            "                            <Lines>5490</Lines>\n" +
            "                            <Columns>5490</Columns>\n" +
            "                        </Size>\n" +
            "                    </Resolution>\n" +
            "                </List_of_Resolutions>" +
            "            </Image_Information>\n" +
            "        </Specific_Product_Header>\n" +
            "    </Variable_Header>\n" +
            "</Earth_Explorer_Header>\n";
}
