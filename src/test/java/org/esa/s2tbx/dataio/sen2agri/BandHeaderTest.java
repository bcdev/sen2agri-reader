package org.esa.s2tbx.dataio.sen2agri;

import org.jdom.Document;
import org.jdom.JDOMException;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class BandHeaderTest {

    @Test
    public void testParseAndGet_Annex_Info() throws JDOMException, IOException {
        final Document document = TestUtil.createDocument(XML_HEADER);

        final BandHeader header = new BandHeader(document);

        assertEquals(10880, header.getRasterHeight());
        assertEquals(10580, header.getRasterWidth());
        assertEquals(-1, header.getNoDataValue());
    }

    @Test
    public void testParseAndGet_ImageInfo_withNoDataValue() throws JDOMException, IOException {
        final Document document = TestUtil.createDocument(XML_HEADER_NO_DATA);

        final BandHeader header = new BandHeader(document);

        assertEquals(10880, header.getRasterHeight());
        assertEquals(10580, header.getRasterWidth());
        assertEquals(-10000, header.getNoDataValue());
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

    private static final String XML_HEADER_NO_DATA = "<Earth_Explorer_Header xmlns=\"http://eop-cfi.esa.int/CFI\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" schema_version=\"1.00\" xsi:schemaLocation=\"http://eop-cfi.esa.int/CFI ./SSC_PDTANX_L2L3_AtmosphericAndBiophysicalProduct.xsd\" xsi:type=\"PDTANX_Header_Type\">\n" +
            "    <Variable_Header>\n" +
            "        <Specific_Product_Header>\n" +
            "            <Image_Information>\n" +
            "                <Nodata_Value>-10000</Nodata_Value>" +
            "                <Size>\n" +
            "                    <Lines>10880</Lines>\n" +
            "                    <Columns>10580</Columns>\n" +
            "                </Size>\n" +
            "            </Image_Information>\n" +
            "        </Specific_Product_Header>\n" +
            "    </Variable_Header>\n" +
            "</Earth_Explorer_Header>\n";
}
