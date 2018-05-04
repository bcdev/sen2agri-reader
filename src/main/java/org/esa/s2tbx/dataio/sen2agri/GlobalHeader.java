package org.esa.s2tbx.dataio.sen2agri;

import org.esa.snap.core.datamodel.ProductData;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

class GlobalHeader {

    private static final String EARTH_EXPLORER_HEADER = "Earth_Explorer_Header";
    private static final String FIXED_HEADER = "Fixed_Header";
    private static final String FILE_NAME = "File_Name";
    private static final String VARIABLE_HEADER = "Variable_Header";
    private static final String SPECIFIC_PRODUCT_HEADER = "Specific_Product_Header";
    private static final String PRODUCT_INFORMATION = "Product_Information";
    private static final String ACQUISITION_TIME = "Acquisition_Date_Time";
    private static final String IMAGE_INFORMATION = "Image_Information";
    private static final String LIST_OF_RESOLUTIONS = "List_of_Resolutions";
    private static final String SIZE = "Size";

    private int sceneRasterHeight;
    private int sceneRasterWidth;
    private String productName;
    private ProductData.UTC sensingTime;

    GlobalHeader(Document document) throws IOException {
        sceneRasterHeight = -1;
        sceneRasterWidth = -1;

        parse(document);
    }

    public int getSceneRasterHeight() {
        return sceneRasterHeight;
    }

    public int getSceneRasterWidth() {
        return sceneRasterWidth;
    }

    public String getProductName() {
        return productName;
    }

    public ProductData.UTC getSensingTime() {
        return sensingTime;
    }

    private void parse(Document document) throws IOException {
        final Element rootElement = document.getRootElement();
        if (!EARTH_EXPLORER_HEADER.equals(rootElement.getName())) {
            throw new IOException("Invalid input file, not of type " + EARTH_EXPLORER_HEADER);
        }

        final Namespace namespace = Namespace.getNamespace("http://eop-cfi.esa.int/CFI");

        final Element fixedHeaderElement = rootElement.getChild(FIXED_HEADER, namespace);
        parseProductname(namespace, fixedHeaderElement);

        final Element variableHeaderElement = rootElement.getChild(VARIABLE_HEADER, namespace);
        final Element specificHeaderElement = variableHeaderElement.getChild(SPECIFIC_PRODUCT_HEADER, namespace);
        final Element productInformationElement = specificHeaderElement.getChild(PRODUCT_INFORMATION, namespace);
        parseSensingTimes(namespace, productInformationElement);

        final Element imageInformationElement = specificHeaderElement.getChild(IMAGE_INFORMATION, namespace);
        parseRasterSizes(imageInformationElement, namespace);
    }

    private void parseSensingTimes(Namespace namespace, Element productInformationElement) throws IOException {
        final Element acquisitionTimeElement = productInformationElement.getChild(ACQUISITION_TIME, namespace);
        final String acquisitionTime = acquisitionTimeElement.getValue();
        try {
            sensingTime = ProductData.UTC.parse(acquisitionTime, "'UTC='yyyy-MM-dd'T'HH:mm:ss");
        } catch (ParseException e) {
            throw new IOException(e.getMessage());
        }
    }

    private void parseProductname(Namespace namespace, Element fixedHeaderElement) {
        final Element fileNameElement = fixedHeaderElement.getChild(FILE_NAME, namespace);
        productName = fileNameElement.getValue();
    }

    @SuppressWarnings("unchecked")
    private void parseRasterSizes(Element imageInformationElement, Namespace namespace) {
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
}
