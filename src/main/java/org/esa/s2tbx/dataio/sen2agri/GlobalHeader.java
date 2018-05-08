package org.esa.s2tbx.dataio.sen2agri;

import org.esa.snap.core.datamodel.ProductData;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

class GlobalHeader extends Header {

    private static final String FIXED_HEADER = "Fixed_Header";
    private static final String FILE_NAME = "File_Name";
    private static final String PRODUCT_INFORMATION = "Product_Information";
    private static final String ACQUISITION_TIME = "Acquisition_Date_Time";
    private static final String REFLECTANCE_SCALE_FACTOR = "Reflectance_Quantification_Value";
    private static final String LIST_OF_RESOLUTIONS = "List_of_Resolutions";
    private static final String GEO_REF_INFORMATION = "Geo_Referencing_Information";
    private static final String PRODUCT_COVERAGE = "Product_Coverage";
    private static final String GEOGRAPHIC = "Geographic";
    private static final String CRS = "Coordinate_Reference_System";
    private static final String CODE = "Code";
    private static final String GEO_POSITION = "Geoposition";
    private static final String ULX = "ULX";
    private static final String ULY = "ULY";
    private static final String XDIM = "XDIM";
    private static final String YDIM = "YDIM";

    private int sceneRasterHeight;
    private int sceneRasterWidth;
    private String productName;
    private ProductData.UTC sensingTime;
    private double reflectanceScaleFactor;
    private String epsgCode;
    private CrsParameter highResCrsparameter;
    private CrsParameter lowResCrsparameter;

    GlobalHeader(Document document) throws IOException {
        this();

        parse(document);
    }

    GlobalHeader() {
        super();
        sceneRasterHeight = -1;
        sceneRasterWidth = -1;
    }

    int getSceneRasterHeight() {
        return sceneRasterHeight;
    }

    int getSceneRasterWidth() {
        return sceneRasterWidth;
    }

    String getProductName() {
        return productName;
    }

    // only for tests tb 2018-05-04
    void setProductName(String productName) {
        this.productName = productName;
    }

    ProductData.UTC getSensingTime() {
        return sensingTime;
    }

    double getReflectanceScaleFactor() {
        return reflectanceScaleFactor;
    }

    String getEpsgCode() {
        return epsgCode;
    }

    CrsParameter getHighResCrsParameter() {
        return highResCrsparameter;
    }

    CrsParameter getLowResCrsParameter() {
        return lowResCrsparameter;
    }

    private void parse(Document document) throws IOException {
        final Element rootElement = document.getRootElement();
        if (!EARTH_EXPLORER_HEADER.equals(rootElement.getName())) {
            throw new IOException("Invalid input file, not of type " + EARTH_EXPLORER_HEADER);
        }

        final Element fixedHeaderElement = rootElement.getChild(FIXED_HEADER, namespace);
        parseProductName(fixedHeaderElement, namespace);

        final Element variableHeaderElement = rootElement.getChild(VARIABLE_HEADER, namespace);
        final Element specificHeaderElement = variableHeaderElement.getChild(SPECIFIC_PRODUCT_HEADER, namespace);
        final Element productInformationElement = specificHeaderElement.getChild(PRODUCT_INFORMATION, namespace);
        parseSensingTimes(productInformationElement, namespace);
        parseReflectanceScalingFactor(productInformationElement, namespace);

        final Element imageInformationElement = specificHeaderElement.getChild(IMAGE_INFORMATION, namespace);
        parseRasterSizes(imageInformationElement, namespace);

        final Element geoInformationElement = specificHeaderElement.getChild(GEO_REF_INFORMATION, namespace);
        final Element productCoverageElement = geoInformationElement.getChild(PRODUCT_COVERAGE, namespace);
        final Element geographicElement = productCoverageElement.getChild(GEOGRAPHIC, namespace);
        final Element crsElement = geographicElement.getChild(CRS, namespace);
        final Element codeElement = crsElement.getChild(CODE, namespace);
        epsgCode = codeElement.getValue();

    }

    private void parseSensingTimes(Element productInformationElement, Namespace namespace) throws IOException {
        final Element acquisitionTimeElement = productInformationElement.getChild(ACQUISITION_TIME, namespace);
        final String acquisitionTime = acquisitionTimeElement.getValue();
        try {
            sensingTime = ProductData.UTC.parse(acquisitionTime, "'UTC='yyyy-MM-dd'T'HH:mm:ss");
        } catch (ParseException e) {
            throw new IOException(e.getMessage());
        }
    }

    private void parseReflectanceScalingFactor(Element productInformationElement, Namespace namespace) {
        final Element scaleFactorElement = productInformationElement.getChild(REFLECTANCE_SCALE_FACTOR, namespace);
        final String scalefactor = scaleFactorElement.getValue();
        reflectanceScaleFactor = Double.parseDouble(scalefactor);
    }

    private void parseProductName(Element fixedHeaderElement, Namespace namespace) {
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
                highResCrsparameter = new CrsParameter();
                final Element sizeElement = resolutionElement.getChild(SIZE, namespace);
                sceneRasterHeight = parseLinesElement(sizeElement);
                sceneRasterWidth = parseColumnsElement(sizeElement);

                highResCrsparameter.width = sceneRasterWidth;
                highResCrsparameter.height = sceneRasterHeight;

                final Element geoPositionElement = resolutionElement.getChild(GEO_POSITION, namespace);
                highResCrsparameter.easting = parseIntegerElement(ULX, geoPositionElement);
                highResCrsparameter.northing = parseIntegerElement(ULY, geoPositionElement);
                highResCrsparameter.pixelSizeX = parseIntegerElement(XDIM, geoPositionElement);
                highResCrsparameter.pixelSizeY = parseIntegerElement(YDIM, geoPositionElement);
            }
            if ("20".equals(r.getValue())) {
                lowResCrsparameter = new CrsParameter();
                final Element sizeElement = resolutionElement.getChild(SIZE, namespace);
                lowResCrsparameter.height = parseLinesElement(sizeElement);
                lowResCrsparameter.width = parseColumnsElement(sizeElement);

                final Element geoPositionElement = resolutionElement.getChild(GEO_POSITION, namespace);
                lowResCrsparameter.easting = parseIntegerElement(ULX, geoPositionElement);
                lowResCrsparameter.northing = parseIntegerElement(ULY, geoPositionElement);
                lowResCrsparameter.pixelSizeX = parseIntegerElement(XDIM, geoPositionElement);
                lowResCrsparameter.pixelSizeY = parseIntegerElement(YDIM, geoPositionElement);
            }
        }
    }
}
