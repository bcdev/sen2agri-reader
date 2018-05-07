package org.esa.s2tbx.dataio.sen2agri;

import it.geosolutions.imageioimpl.plugins.tiff.TIFFImageReader;

class StreamContainer {

    TIFFImageReader tiffImageReader;
    int imageIndex;

    public TIFFImageReader getTiffImageReader() {
        return tiffImageReader;
    }

    public void setTiffImageReader(TIFFImageReader tiffImageReader) {
        this.tiffImageReader = tiffImageReader;
    }

    public int getImageIndex() {
        return imageIndex;
    }

    public void setImageIndex(int imageIndex) {
        this.imageIndex = imageIndex;
    }
}
