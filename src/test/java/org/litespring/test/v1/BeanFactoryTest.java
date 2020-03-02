package org.litespring.test.v1;

import org.junit.Before;
import org.junit.Test;
import org.litespring.beans.BeanDefinition;
import org.litespring.beans.factory.support.DefaultBeanFactory;
import org.litespring.beans.factory.xml.XmlBeanDefinitionReader;
import org.litespring.core.io.ClassPathResource;
import org.litespring.service.v1.PetStoreService;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class BeanFactoryTest {


    DefaultBeanFactory factory = null;

    XmlBeanDefinitionReader reader = null;


    @Before
    public void setUp() {

        factory = new DefaultBeanFactory();
        reader = new XmlBeanDefinitionReader(factory);
    }

    @Test
    public void testGetBean() {

        reader.loadBeanDefinitions(new ClassPathResource("petstore-v1.xml"));

        BeanDefinition bd = factory.getBeanDefinition("petStore");

        assertTrue(bd.isSingleton());

        PetStoreService petStore = (PetStoreService)factory.getBean("petStore");
        assertNotNull(petStore);

        System.out.println(bd.getBeanClassName());
    }

}
