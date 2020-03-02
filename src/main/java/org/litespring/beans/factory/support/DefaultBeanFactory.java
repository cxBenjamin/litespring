package org.litespring.beans.factory.support;

import org.litespring.beans.BeanDefinition;
import org.litespring.beans.factory.config.ConfigurableBeanFactory;
import org.litespring.util.ClassUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultBeanFactory extends DefaultSingletonBeanRegistry implements ConfigurableBeanFactory, BeanDefinitionRegistry {

    private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(64);

    private ClassLoader beanClassLoader;


    public DefaultBeanFactory() {
    }

    @Override
    public void setBeanClassLoader(ClassLoader beanClassLoader) {
        this.beanClassLoader = beanClassLoader;
    }

    @Override
    public ClassLoader getBeanClassLoader() {
        return (this.beanClassLoader == null) ? ClassUtils.getDefaultClassLoader() : this.beanClassLoader;
    }

    @Override
    public Object getBean(String beanId) {

        BeanDefinition beanDefinition = this.getBeanDefinition(beanId);

        if (beanDefinition == null) {
            return null;
        }

        if (beanDefinition.isSingleton()) {
            Object bean = this.getSingleton(beanId);

            if (bean == null) {
                bean = CreateBean(beanDefinition);
                this.registerSingleton(beanId, bean);
            }
            return bean;
        }

        return CreateBean(beanDefinition);
    }

    private Object CreateBean(BeanDefinition beanDefinition) {

        ClassLoader cl = this.getBeanClassLoader();

        String beanClassName = beanDefinition.getBeanClassName();

        try {
            Class<?> clz = cl.loadClass(beanClassName);
            return clz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("create bean " + beanClassName + "failed");
        }

    }

    @Override
    public BeanDefinition getBeanDefinition(String beanId) {
        return this.beanDefinitionMap.get(beanId);
    }

    @Override
    public void registerBeanDefinition(String beanId, BeanDefinition beanDefinition) {

        this.beanDefinitionMap.put(beanId, beanDefinition);
    }
}
