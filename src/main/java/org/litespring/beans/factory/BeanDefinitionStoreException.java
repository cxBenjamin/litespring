package org.litespring.beans.factory;

import org.litespring.beans.exception.BeansException;

public class BeanDefinitionStoreException extends BeansException {

    public BeanDefinitionStoreException(String msg, Throwable cause) {

        super(msg, cause);

    }
}
