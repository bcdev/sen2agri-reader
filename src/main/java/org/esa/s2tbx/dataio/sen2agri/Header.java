package org.esa.s2tbx.dataio.sen2agri;

import org.jdom.Element;
import org.jdom.Namespace;

class Header {

    static final String EARTH_EXPLORER_HEADER = "Earth_Explorer_Header";
    static final String VARIABLE_HEADER = "Variable_Header";
    static final String SPECIFIC_PRODUCT_HEADER = "Specific_Product_Header";
    static final String SIZE = "Size";

    final Namespace namespace;

    Header() {
        namespace = Namespace.getNamespace("http://eop-cfi.esa.int/CFI");
    }

    int parseLinesElement(Element element) {
        final Element linesElement = element.getChild("Lines", namespace);
        final String linesValue = linesElement.getValue();
        return Integer.parseInt(linesValue);
    }

    int parseColumnsElement(Element element) {
        final Element columnsElement = element.getChild("Columns", namespace);
        final String columnsValue = columnsElement.getValue();
        return Integer.parseInt(columnsValue);
    }


}
