package org.esa.s2tbx.dataio.sen2agri;

import org.jdom.Element;
import org.jdom.Namespace;

class Header {

    static final String EARTH_EXPLORER_HEADER = "Earth_Explorer_Header";
    static final String VARIABLE_HEADER = "Variable_Header";
    static final String SPECIFIC_PRODUCT_HEADER = "Specific_Product_Header";
    static final String ANNEX_INFORMATION = "Annex_Information";
    static final String SIZE = "Size";

    final Namespace namespace;

    Header() {
        namespace = Namespace.getNamespace("http://eop-cfi.esa.int/CFI");
    }

    int parseLinesElement(Element element) {
        return parseIntegerElement("Lines", element);
    }

    int parseColumnsElement(Element element) {
        return parseIntegerElement("Columns", element);
    }

    int parseIntegerElement(String name, Element element) {
        final Element elementChild = element.getChild(name, namespace);
        final String integerValue = elementChild.getValue();
        return Integer.parseInt(integerValue);
    }

    double parseDoubleElement(String name, Element element) {
        final Element elementChild = element.getChild(name, namespace);
        final String doubleValue = elementChild.getValue();
        return Double.parseDouble(doubleValue);
    }


}
