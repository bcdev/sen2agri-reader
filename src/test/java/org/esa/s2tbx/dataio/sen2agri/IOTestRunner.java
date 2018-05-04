package org.esa.s2tbx.dataio.sen2agri;

import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

import java.io.InputStream;

public class IOTestRunner extends BlockJUnit4ClassRunner {
    private static final String PROPERTYNAME_EXECUTE_PRODUCT_TESTS = "org.esa.s2tbx.dataio.tests.execute";
    private final Class<?> clazz;
    private boolean executeProductTests;

    public IOTestRunner(Class<?> klass) throws InitializationError {
        super(klass);

        this.clazz = klass;
        executeProductTests = Boolean.getBoolean(PROPERTYNAME_EXECUTE_PRODUCT_TESTS);
        if (!executeProductTests) {
            System.out.println("IO Tests disabled. Set VM param -D" + PROPERTYNAME_EXECUTE_PRODUCT_TESTS + "=true to enable.");
        }

        final InputStream resourceStream = getClass().getResourceAsStream("dataDirectory.properties");
        if (resourceStream == null) {
            System.out.println("IO Tests disabled. 'dataDirectory.properties' file not found");
            executeProductTests = false;
        }
    }

    @Override
    protected void runChild(FrameworkMethod method, RunNotifier notifier) {
        if (executeProductTests) {
            super.runChild(method, notifier);
        } else {
            final Description description = Description.createTestDescription(clazz, "allMethods. Product tests disabled. Set VM param -D" + PROPERTYNAME_EXECUTE_PRODUCT_TESTS + "=true to enable.");
            notifier.fireTestIgnored(description);
        }
    }
}
