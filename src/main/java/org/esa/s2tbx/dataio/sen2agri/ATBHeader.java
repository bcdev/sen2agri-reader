package org.esa.s2tbx.dataio.sen2agri;

import org.jdom.Document;
import org.jdom.Element;

import java.io.IOException;

class ATBHeader extends Header {

    private int rasterHeight;
    private int rasterWidth;
    private byte vapNoDataValue;
    private byte aotNoDataValue;
    private double vapScaleFactor;
    private double aotScaleFactor;

    ATBHeader(Document document) throws IOException {
        rasterHeight = -1;
        rasterWidth = -1;
        vapNoDataValue = 0;
        aotNoDataValue = 0;
        vapScaleFactor = Double.NaN;
        aotScaleFactor = Double.NaN;

        parse(document);
    }

    int getRasterHeight() {
        return rasterHeight;
    }

    int getRasterWidth() {
        return rasterWidth;
    }

    byte getVapNoDataValue() {
        return vapNoDataValue;
    }

    double getVapScaleFactor() {
        return vapScaleFactor;
    }

    byte getAotNoDataValue() {
        return aotNoDataValue;
    }

    double getAotScaleFactor() {
        return aotScaleFactor;
    }

    private void parse(Document document) throws IOException {
        final Element rootElement = document.getRootElement();
        if (!EARTH_EXPLORER_HEADER.equals(rootElement.getName())) {
            throw new IOException("Invalid input file, not of type " + EARTH_EXPLORER_HEADER);
        }

        final Element variableHeaderElement = rootElement.getChild(VARIABLE_HEADER, namespace);
        final Element specificHeaderElement = variableHeaderElement.getChild(SPECIFIC_PRODUCT_HEADER, namespace);
        final Element annexInformationElement = specificHeaderElement.getChild(ANNEX_INFORMATION, namespace);
        final Element sizeElement = annexInformationElement.getChild(SIZE, namespace);
        rasterHeight = parseLinesElement(sizeElement);
        rasterWidth = parseColumnsElement(sizeElement);

        vapNoDataValue = (byte) parseIntegerElement("VAP_Nodata_Value", annexInformationElement);
        vapScaleFactor = parseDoubleElement("VAP_Quantification_Value", annexInformationElement);
        aotNoDataValue = (byte) parseIntegerElement("AOT_Nodata_Value", annexInformationElement);
        aotScaleFactor = parseDoubleElement("AOT_Quantification_Value", annexInformationElement);
    }
}
