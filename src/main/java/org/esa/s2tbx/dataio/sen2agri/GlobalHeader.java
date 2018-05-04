package org.esa.s2tbx.dataio.sen2agri;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;

import java.io.IOException;
import java.util.List;

class GlobalHeader {

    private static final String EARTH_EXPLORER_HEADER = "Earth_Explorer_Header";
    private static final String VARIABLE_HEADER = "Variable_Header";
    private static final String SPECIFIC_PRODUCT_HEADER = "Specific_Product_Header";
    private static final String IMAGE_INFORMATION = "Image_Information";
    private static final String LIST_OF_RESOLUTIONS = "List_of_Resolutions";
    private static final String SIZE = "Size";

    private int sceneRasterHeight;
    private int sceneRasterWidth;

    GlobalHeader(Document document) throws IOException {
        sceneRasterHeight = -1;
        sceneRasterWidth = -1;

        parse(document);
    }

    @SuppressWarnings("unchecked")
    private void parse(Document document) throws IOException {
        final Element rootElement = document.getRootElement();
        if (!EARTH_EXPLORER_HEADER.equals(rootElement.getName())) {
            throw new IOException("Invalid input file, not of type " + EARTH_EXPLORER_HEADER);
        }

        final Namespace namespace = Namespace.getNamespace("http://eop-cfi.esa.int/CFI");

        final Element variableHeaderElement = rootElement.getChild(VARIABLE_HEADER, namespace);
        final Element specificHeaderElement = variableHeaderElement.getChild(SPECIFIC_PRODUCT_HEADER, namespace);
        final Element imageInformationElement = specificHeaderElement.getChild(IMAGE_INFORMATION, namespace);

        final Element listOfResolutionsElement = imageInformationElement.getChild(LIST_OF_RESOLUTIONS, namespace);
        final List<Element> children = listOfResolutionsElement.getChildren();
        for (final Element resolutionElement : children) {
            final Attribute r = resolutionElement.getAttribute("r");
            if ("10".equals(r.getValue())) {
                final Element sizeElement = resolutionElement.getChild(SIZE, namespace);

                final Element linesElement = sizeElement.getChild("Lines", namespace);
                final String linesValue = linesElement.getValue();
                sceneRasterHeight = Integer.parseInt(linesValue);

                final Element columnsElement = sizeElement.getChild("Columns", namespace);
                final String columnsValue = columnsElement.getValue();
                sceneRasterWidth = Integer.parseInt(columnsValue);
                break;
            }
        }
    }

    public int getSceneRasterHeight() {
        return sceneRasterHeight;
    }

    public int getSceneRasterWidth() {
        return sceneRasterWidth;
    }
}
