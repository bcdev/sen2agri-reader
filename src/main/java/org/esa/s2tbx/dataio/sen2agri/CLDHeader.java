package org.esa.s2tbx.dataio.sen2agri;

import org.jdom.Document;
import org.jdom.Element;

import java.io.IOException;

class CLDHeader extends Header {

    private int rasterHeight;
    private int rasterWidth;

    CLDHeader(Document document) throws IOException {
        rasterHeight = -1;
        rasterWidth = -1;

        parse(document);
    }

    int getRasterHeight() {
        return rasterHeight;
    }

    int getRasterWidth() {
        return rasterWidth;
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
    }
}
