package org.litespring.context;

import org.litespring.beans.factory.support.DefaultBeanFactory;
import org.litespring.beans.factory.xml.XmlBeanDefinitionReader;
import org.litespring.core.io.Resource;
import org.litespring.util.ClassUtils;

public abstract class AbstractApplicationContext implements ApplicationContext {


    private DefaultBeanFactory factory = null;

    private ClassLoader beanClassLoader;

    public AbstractApplicationContext(String configFile) {

        factory = new DefaultBeanFactory();

        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);

        Resource resource = this.getResourceByPath(configFile);

        reader.loadBeanDefinitions(resource);

        factory.setBeanClassLoader(this.getBeanClassLoader());
    }


    protected abstract Resource getResourceByPath(String path);

    @Override
    public void setBeanClassLoader(ClassLoader beanClassLoader) {
        this.beanClassLoader = beanClassLoader;
    }

    @Override
    public ClassLoader getBeanClassLoader() {
        return (this.beanClassLoader != null ? this.beanClassLoader : ClassUtils.getDefaultClassLoader());
    }

    @Override
    public Object getBean(String beanId) {
        return factory.getBean(beanId);
    }
}
