package org.esa.s2tbx.dataio.sen2agri;

import com.bc.ceres.core.ProgressMonitor;
import org.esa.snap.core.dataio.AbstractProductReader;
import org.esa.snap.core.dataio.ProductReaderPlugIn;
import org.esa.snap.core.datamodel.Band;
import org.esa.snap.core.datamodel.Product;
import org.esa.snap.core.datamodel.ProductData;
import org.esa.snap.core.datamodel.TiePointGrid;

import java.io.IOException;

public class Sen2AgriProductReader extends AbstractProductReader {

    protected Sen2AgriProductReader(ProductReaderPlugIn readerPlugIn) {
        super(readerPlugIn);
    }

    @Override
    protected Product readProductNodesImpl() throws IOException {
        throw new RuntimeException("not implemented");
    }

    @Override
    protected void readBandRasterDataImpl(int i, int i1, int i2, int i3, int i4, int i5, Band band, int i6, int i7, int i8, int i9, ProductData productData, ProgressMonitor progressMonitor) throws IOException {
        throw new RuntimeException("not implemented");
    }

    @Override
    public void readTiePointGridRasterData(TiePointGrid tpg, int destOffsetX, int destOffsetY, int destWidth, int destHeight, ProductData destBuffer, ProgressMonitor pm) throws IOException {
        throw new RuntimeException("not implemented");
    }
}
