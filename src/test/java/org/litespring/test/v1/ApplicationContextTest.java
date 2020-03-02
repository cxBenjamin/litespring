package org.litespring.test.v1;

import org.junit.Assert;
import org.junit.Test;
import org.litespring.context.ClassPathXmlApplicationContext;
import org.litespring.service.v1.PetStoreService;

public class ApplicationContextTest {

    @Test
    public void testGetBean(){

        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("petstore-v1.xml");
        PetStoreService petStore = (PetStoreService) ctx.getBean("petStore");

        Assert.assertNotNull(petStore);
    }
}
