package org.litespring.core.io;

import org.litespring.util.ClassUtils;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class ClassPathResource implements Resource {

    private String path;

    private ClassLoader classLoader;

    public ClassPathResource(String path) {
        this(path, null);
    }

    public ClassPathResource(String path, ClassLoader classLoader) {
        this.path = path;
        this.classLoader = (classLoader != null ? classLoader : ClassUtils.getDefaultClassLoader());
    }

    @Override
    public InputStream getInputStream() throws Exception {

        InputStream is = this.classLoader.getResourceAsStream(path);

        if (is == null) {
            throw new FileNotFoundException(path + " can not be found");
        }

        return is;
    }

    @Override
    public String getDescription() {
        return this.path;
    }
}
