package org.esa.s2tbx.dataio.sen2agri;

import java.io.File;
import java.io.FilenameFilter;
import java.util.regex.Pattern;

public class RegExFileNameFilter implements FilenameFilter{

    private final Pattern pattern;

    RegExFileNameFilter(String regularExpression) {
        pattern = Pattern.compile(regularExpression);
    }

    @Override
    public boolean accept(File dir, String name) {
        return pattern.matcher(name).matches();
    }
}
