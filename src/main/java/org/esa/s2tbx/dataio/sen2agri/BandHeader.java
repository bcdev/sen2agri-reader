package org.esa.s2tbx.dataio.sen2agri;

import org.jdom.Document;
import org.jdom.Element;

import java.io.IOException;

class BandHeader extends Header {

    private int rasterHeight;
    private int rasterWidth;
    private int noDataValue;

    BandHeader(Document document) throws IOException {
        rasterHeight = -1;
        rasterWidth = -1;
        noDataValue = -1;

        parse(document);
    }

    int getRasterHeight() {
        return rasterHeight;
    }

    int getRasterWidth() {
        return rasterWidth;
    }

    int getNoDataValue() {
        return noDataValue;
    }

    private void parse(Document document) throws IOException {
        final Element rootElement = document.getRootElement();
        if (!EARTH_EXPLORER_HEADER.equals(rootElement.getName())) {
            throw new IOException("Invalid input file, not of type " + EARTH_EXPLORER_HEADER);
        }

        final Element variableHeaderElement = rootElement.getChild(VARIABLE_HEADER, namespace);
        final Element specificHeaderElement = variableHeaderElement.getChild(SPECIFIC_PRODUCT_HEADER, namespace);
        final Element annexInformationElement = specificHeaderElement.getChild(ANNEX_INFORMATION, namespace);
        if (annexInformationElement != null) {
            parseWidthAndHeight(annexInformationElement);
        }

        final Element imageInformationElement = specificHeaderElement.getChild(IMAGE_INFORMATION, namespace);
        if (imageInformationElement != null) {
            final Element noDatavalueElement = imageInformationElement.getChild("Nodata_Value", namespace);
            if (noDatavalueElement != null) {
                final String noDatavalueString = noDatavalueElement.getValue();
                if (!noDatavalueString.equals("NaN")) {
                    noDataValue = Integer.parseInt(noDatavalueString);
                }
            }

            parseWidthAndHeight(imageInformationElement);
        }
    }

    private void parseWidthAndHeight(Element element) {
        final Element sizeElement = element.getChild(SIZE, namespace);
        rasterHeight = parseLinesElement(sizeElement);
        rasterWidth = parseColumnsElement(sizeElement);
    }
}
