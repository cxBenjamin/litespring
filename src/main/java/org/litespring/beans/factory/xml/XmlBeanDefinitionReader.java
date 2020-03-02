package org.litespring.beans.factory.xml;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.litespring.beans.BeanDefinition;
import org.litespring.beans.factory.BeanDefinitionStoreException;
import org.litespring.beans.factory.support.BeanDefinitionRegistry;
import org.litespring.beans.factory.support.GenericBeanDefinition;
import org.litespring.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

public class XmlBeanDefinitionReader {

    public static final String ID_ATTRIBUTE = "id";

    public static final String CLASS_ATTRIBUTE = "class";

    public static final String SCOPE_ATTRIBUTE = "scope";

    private BeanDefinitionRegistry registry;


    public XmlBeanDefinitionReader(BeanDefinitionRegistry registry) {

        this.registry = registry;
    }


    public void loadBeanDefinitions(Resource resource) {

        InputStream is = null;

        try {
            is = resource.getInputStream();
            SAXReader reader = new SAXReader();

            Document doc = reader.read(is);

            Element root = doc.getRootElement();

            //System.out.println(">>>>>>>>>> " + root.getName()); <beans>

            Iterator<Element> iterator = root.elementIterator();

            while (iterator.hasNext()) {
                Element ele = iterator.next();

                String id = ele.attributeValue(ID_ATTRIBUTE);
                String beanClassName = ele.attributeValue(CLASS_ATTRIBUTE);
                BeanDefinition bd = new GenericBeanDefinition(id, beanClassName);
                if (ele.attribute(SCOPE_ATTRIBUTE) != null) {
                    bd.setScope(ele.attributeValue(SCOPE_ATTRIBUTE));
                }
                this.registry.registerBeanDefinition(id, bd);
            }

        } catch (Exception e) {

            throw new BeanDefinitionStoreException("IOExcepiton parsing xml from " + resource.getDescription(), e);
        } finally {

            if (is != null) {

                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
