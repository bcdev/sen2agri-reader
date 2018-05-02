package org.esa.s2tbx.dataio.sen2agri;

import org.esa.snap.core.dataio.DecodeQualification;
import org.esa.snap.core.dataio.ProductReader;
import org.esa.snap.core.dataio.ProductReaderPlugIn;
import org.esa.snap.core.util.io.SnapFileFilter;

import java.io.File;
import java.util.Locale;

public class Sen2AgriProductReaderPlugIn implements ProductReaderPlugIn {

    @Override
    public DecodeQualification getDecodeQualification(Object o) {
        throw new RuntimeException("not implemented");
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
    public String[] getFormatNames() {
        return new String[]{"S2_AGRI_SSC"};
    }

    @Override
    public String[] getDefaultFileExtensions() {
        throw new RuntimeException("not implemented");
    }

    @Override
    public String getDescription(Locale locale) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public SnapFileFilter getProductFileFilter() {
        throw new RuntimeException("not implemented");
    }
}
