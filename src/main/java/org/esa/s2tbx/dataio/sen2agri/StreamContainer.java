package org.esa.s2tbx.dataio.sen2agri;

import it.geosolutions.imageioimpl.plugins.tiff.TIFFImageReader;

class StreamContainer {

    TIFFImageReader tiffImageReader;
    int imageIndex;

    TIFFImageReader getTiffImageReader() {
        return tiffImageReader;
    }

    int getImageIndex() {
        return imageIndex;
    }
}
