package org.litespring.util;

public abstract class ClassUtils {

    public static ClassLoader getDefaultClassLoader() {

        ClassLoader c1 = null;

        try {

            c1 = Thread.currentThread().getContextClassLoader();
        } catch (Throwable ex) {

        }
        if (c1 == null) {

            c1 = ClassUtils.class.getClassLoader();

            if (c1 == null) {

                try {
                    c1 = ClassLoader.getSystemClassLoader();
                } catch (Exception e) {

                }
            }
        }

        return c1;
    }
}
