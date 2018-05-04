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
        assertEquals("S2B_OPER_SSC_L2VALD_11RMP____20171112", globalHeader.getProductName());
        assertEquals("12-NOV-2017 18:29:37.000000", globalHeader.getSensingTime().toString());
        assertEquals(0.0001, globalHeader.getReflectanceScaleFactor(), 1e-8);
    }

    private Document createDocument() throws JDOMException, IOException {
        final SAXBuilder saxBuilder = new SAXBuilder();
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(XML_HEADER.getBytes());
        return saxBuilder.build(inputStream);
    }

    private static final String XML_HEADER = "<Earth_Explorer_Header xmlns=\"http://eop-cfi.esa.int/CFI\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" schema_version=\"1.00\" xsi:schemaLocation=\"http://eop-cfi.esa.int/CFI ./SSC_PDT_L2_L2PublishedProduct.xsd\" xsi:type=\"VSC_PDT_L2_Header_Type\">\n" +
            "    <Fixed_Header>\n" +
            "        <File_Name>S2B_OPER_SSC_L2VALD_11RMP____20171112</File_Name>\n" +
            "    </Fixed_Header>\n" +
            "    <Variable_Header>\n" +
            "        <Specific_Product_Header>\n" +
            "            <Product_Information>\n" +
            "                <Acquisition_Date_Time>UTC=2017-11-12T18:29:37</Acquisition_Date_Time>\n" +
            "                <Reflectance_Quantification_Value>0.0001</Reflectance_Quantification_Value>\n" +
            "            </Product_Information>\n" +
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
