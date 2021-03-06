package org.esa.s2tbx.dataio.sen2agri;

import org.jdom.Document;
import org.jdom.JDOMException;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class ATBHeaderTest {

    @Test
    public void testParseAndGet() throws JDOMException, IOException {
        final Document document = TestUtil.createDocument(XML_HEADER);

        final ATBHeader header = new ATBHeader(document);
        assertEquals(10780, header.getRasterHeight());
        assertEquals(10980, header.getRasterWidth());

        assertEquals(2, header.getVapNoDataValue());
        assertEquals(3, header.getAotNoDataValue());

        assertEquals(1.34, header.getVapScaleFactor(), 1e-8);
        assertEquals(0.086, header.getAotScaleFactor(), 1e-8);
    }

    private static final String XML_HEADER = "<Earth_Explorer_Header xmlns=\"http://eop-cfi.esa.int/CFI\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" schema_version=\"1.00\" xsi:schemaLocation=\"http://eop-cfi.esa.int/CFI ./SSC_PDTANX_L2L3_AtmosphericAndBiophysicalProduct.xsd\" xsi:type=\"PDTANX_Header_Type\">\n" +
            "    <Variable_Header>\n" +
            "        <Specific_Product_Header>\n" +
            "            <Annex_Information>\n" +
            "                <VAP_Nodata_Value>2</VAP_Nodata_Value>\n" +
            "                <VAP_Quantification_Value>1.34</VAP_Quantification_Value>\n" +
            "                <AOT_Nodata_Value>3</AOT_Nodata_Value>\n" +
            "                <AOT_Quantification_Value>0.086</AOT_Quantification_Value>" +
            "                <Size>\n" +
            "                    <Lines>10780</Lines>\n" +
            "                    <Columns>10980</Columns>\n" +
            "                </Size>\n" +
            "            </Annex_Information>\n" +
            "        </Specific_Product_Header>\n" +
            "    </Variable_Header>\n" +
            "</Earth_Explorer_Header>\n";
}
