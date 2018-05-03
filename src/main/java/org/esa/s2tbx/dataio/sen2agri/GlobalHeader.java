package org.esa.s2tbx.dataio.sen2agri;

import org.jdom.Document;
import org.jdom.Element;

import java.io.IOException;

class GlobalHeader {

    private static final String EARTH_EXPLORER_HEADER = "Earth_Explorer_Header";
    private static final String VARIABLE_HEADER = "Variable_Header";
    private static final String SPECIFIC_PRODUCT_HEADER = "Specific_Product_Header";

    GlobalHeader(Document document) throws IOException {
        parse(document);
    }

    private void parse(Document document) throws IOException {
        final Element rootElement = document.getRootElement();
        if (!EARTH_EXPLORER_HEADER.equals(rootElement.getName()) ) {
            throw new IOException("Invalid input file, not of type " + EARTH_EXPLORER_HEADER);
        }
        // @todo 1 tb/tb continue here - namespace problems .... 2018-05-03
//        final Element variableHeaderElement = rootElement.getChild(VARIABLE_HEADER);
//        final Element specificHeaderElement = variableHeaderElement.getChild(SPECIFIC_PRODUCT_HEADER);
    }
}
