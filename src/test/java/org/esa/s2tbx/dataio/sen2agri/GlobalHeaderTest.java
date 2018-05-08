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
        final Document document = TestUtil.createDocument(XML_HEADER);

        final GlobalHeader globalHeader = new GlobalHeader(document);
        assertEquals(10980, globalHeader.getSceneRasterHeight());
        assertEquals(10980, globalHeader.getSceneRasterWidth());
        assertEquals("S2B_OPER_SSC_L2VALD_11RMP____20171112", globalHeader.getProductName());
        assertEquals("12-NOV-2017 18:29:37.000000", globalHeader.getSensingTime().toString());
        assertEquals(0.0001, globalHeader.getReflectanceScaleFactor(), 1e-8);
        assertEquals("EPSG:32611", globalHeader.getEpsgCode());

        final CrsParameter hiRes = globalHeader.getHighResCrsParameter();
        assertEquals(10980, hiRes.width);
        assertEquals(10980, hiRes.height);
        assertEquals(399960, hiRes.easting, 1e-8);
        assertEquals(3400020, hiRes.northing, 1e-8);
        assertEquals(10, hiRes.pixelSizeX, 1e-8);
        assertEquals(-10, hiRes.pixelSizeY, 1e-8);

        final CrsParameter loRes = globalHeader.getLowResCrsParameter();
        assertEquals(5490, loRes.width);
        assertEquals(5490, loRes.height);
        assertEquals(399960, loRes.easting, 1e-8);
        assertEquals(3400020, loRes.northing, 1e-8);
        assertEquals(20, loRes.pixelSizeX, 1e-8);
        assertEquals(-20, loRes.pixelSizeY, 1e-8);
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
            "                        <Geoposition>\n" +
            "                            <ULX>399960</ULX>\n" +
            "                            <ULY>3400020</ULY>\n" +
            "                            <XDIM>10</XDIM>\n" +
            "                            <YDIM>-10</YDIM>\n" +
            "                        </Geoposition>" +
            "                    </Resolution>\n" +
            "                    <Resolution r=\"20\">\n" +
            "                        <Size>\n" +
            "                            <Lines>5490</Lines>\n" +
            "                            <Columns>5490</Columns>\n" +
            "                        </Size>\n" +
            "                        <Geoposition>\n" +
            "                            <ULX>399960</ULX>\n" +
            "                            <ULY>3400020</ULY>\n" +
            "                            <XDIM>20</XDIM>\n" +
            "                            <YDIM>-20</YDIM>\n" +
            "                        </Geoposition>" +
            "                    </Resolution>\n" +
            "                </List_of_Resolutions>" +
            "            </Image_Information>\n" +
            "            <Geo_Referencing_Information>\n" +
            "                <Product_Coverage>\n" +
            "                    <Geographic>\n" +
            "                        <Coordinate_Reference_System>\n" +
            "                            <Code>EPSG:32611</Code>" +
            "                        </Coordinate_Reference_System>\n" +
            "                    </Geographic>\n" +
            "                </Product_Coverage>\n" +
            "            </Geo_Referencing_Information>\n" +
            "        </Specific_Product_Header>\n" +
            "    </Variable_Header>\n" +
            "</Earth_Explorer_Header>\n";
}
