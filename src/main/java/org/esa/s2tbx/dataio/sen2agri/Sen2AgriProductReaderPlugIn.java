package org.esa.s2tbx.dataio.sen2agri;

import org.esa.snap.core.dataio.DecodeQualification;
import org.esa.snap.core.dataio.ProductReader;
import org.esa.snap.core.dataio.ProductReaderPlugIn;
import org.esa.snap.core.util.io.SnapFileFilter;

import java.io.File;
import java.util.Locale;
import java.util.regex.Pattern;

public class Sen2AgriProductReaderPlugIn implements ProductReaderPlugIn {

    private static final String HDR_FILE_EXTENSION = ".HDR";
    private static final Pattern pattern = Pattern.compile("S2([AB])_OPER_SSC_L2VALD_\\d{2}[A-Z]{3}____\\d{8}.(HDR|hdr)");

     @Override
    public DecodeQualification getDecodeQualification(Object o) {
        final File inputFile = getInputFile(o);

        if (!isValidInputFile(inputFile)) {
            return DecodeQualification.UNABLE;
        }

        return DecodeQualification.INTENDED;
    }

    /**
     * Returns an array containing the classes that represent valid input types for this reader.
     * <p>
     * <p> Intances of the classes returned in this array are valid objects for the <code>setInput</code> method of the
     * <code>ProductReader</code> interface (the method will not throw an <code>InvalidArgumentException</code> in this
     * case).
     *
     * @return an array containing valid input types, never <code>null</code>
     */
    public Class[] getInputTypes() {
        return new Class[]{String.class, File.class};
    }

    @Override
    public ProductReader createReaderInstance() {
        return new Sen2AgriProductReader(this);
    }

    /**
     * Gets the names of the product formats handled by this product I/O plug-in.
     *
     * @return the names of the product formats handled by this product I/O plug-in, never <code>null</code>
     */
    @Override
    public String[] getFormatNames() {
        return new String[]{"S2_AGRI_SSC_L2VALD"};
    }

    /**
     * Gets the default file extensions associated with each of the format names returned by the <code>{@link
     * #getFormatNames}</code> method. <p>The string array returned shall always have the same lenhth as the array
     * returned by the <code>{@link #getFormatNames}</code> method. <p>The extensions returned in the string array shall
     * always include a leading colon ('.') character, e.g. <code>".hdf"</code>
     *
     * @return the default file extensions for this product I/O plug-in, never <code>null</code>
     */
    @Override
    public String[] getDefaultFileExtensions() {
        return new String[]{HDR_FILE_EXTENSION};
    }

    /**
     * Gets a short description of this plug-in. If the given locale is set to <code>null</code> the default locale is
     * used.
     * <p>
     * <p> In a GUI, the description returned could be used as tool-tip text.
     *
     * @param locale the local for the given decription string, if <code>null</code> the default locale is used
     * @return a textual description of this product reader/writer
     */
    @Override
    public String getDescription(Locale locale) {
        return "Sen2Agri SSC_L2VALD Level 2 Data Products";
    }

    /**
     * Creates an instance of the actual product reader class. This method should never return <code>null</code>.
     *
     * @return a new reader instance, never <code>null</code>
     */
    @Override
    public SnapFileFilter getProductFileFilter() {
        return new SnapFileFilter(getFormatNames()[0], getDefaultFileExtensions(), getDescription(null));
    }

    // package access for testing purpose only tb 2018-05-03
    static File getInputFile(Object input) {
        if (input instanceof String) {
            return new File((String) input);
        } else if (input instanceof File) {
            return (File) input;
        }
        return null;
    }

    // package access for testing purpose only tb 2018-05-03
    @SuppressWarnings("SimplifiableIfStatement")
    static boolean hasHDRExtension(File inputFile) {
        if (inputFile == null) {
            return false;
        }

        return inputFile.getPath().toUpperCase().endsWith(HDR_FILE_EXTENSION);
    }

    // package access for testing purpose only tb 2018-05-03
    static boolean matchesRegEx(File inputFile) {
        final String name = inputFile.getName();
        return pattern.matcher(name).matches() ;
    }

    // @todo 3 tb/tb add tests 2018-05-03
    private boolean isValidInputFile(File inputFile) {
        return inputFile != null &&
                inputFile.isFile() &&
                hasHDRExtension(inputFile) &&
                matchesRegEx(inputFile);
    }
}
