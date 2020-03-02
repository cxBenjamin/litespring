package org.litespring.core.io;

import java.io.InputStream;

public interface Resource {

    InputStream getInputStream() throws Exception;

    String getDescription();

}
