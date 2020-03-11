package org.litespring.beans.factory.support;

import org.litespring.beans.BeanDefinition;
import org.litespring.beans.PropertyValue;
import org.litespring.beans.SimpleTypeConverter;
import org.litespring.beans.factory.BeanCreateException;
import org.litespring.beans.factory.config.ConfigurableBeanFactory;
import org.litespring.util.ClassUtils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.List;
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

        Object bean = instantiateBean(beanDefinition);

        populateBean(beanDefinition, bean);

        return bean;
    }

    private void populateBean(BeanDefinition beanDefinition, Object bean) {

        List<PropertyValue> propertyValues = beanDefinition.getPropertyValues();
        if (propertyValues == null || propertyValues.isEmpty()) {
            return;
        }

        BeanDefinitionValueResolver valueResolver = new BeanDefinitionValueResolver(this);

        SimpleTypeConverter converter = new SimpleTypeConverter();

        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
            PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();

            for (PropertyValue pv : propertyValues) {

                String propertyName = pv.getName();
                Object origianlValue = pv.getValue();

                Object resolvedValue = valueResolver.resolveValueIfNecessary(origianlValue);

                for (PropertyDescriptor pd : pds) {
                    if (pd.getName().equals(propertyName)) {
                        Object convertValue = converter.convertIfNecessary(resolvedValue, pd.getPropertyType());
                        pd.getWriteMethod().invoke(bean, convertValue);
                        break;
                    }
                }

            }
        } catch (Exception e) {
            throw new BeanCreateException("Failed to obtain beaninfo for class [" + beanDefinition.getBeanClassName() + "] ", e);
        }

    }


    private Object instantiateBean(BeanDefinition beanDefinition) {

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
