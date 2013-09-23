package com.telefonica.euro_iaas.sdc.rest;

import org.junit.Test;

import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.dto.Attributes;

public class ProductInstanceResourceImplTest {

    @Test
    public void testConfigureProduct() throws Exception {
        Attributes arguments = new Attributes();
        arguments.add(new Attribute("key", "value"));
        arguments.add(new Attribute("key2", "value2"));

//        Client client = Client.create();
//        client.resource("http://localhost:8080/sdc/app-conf/product/1")
//            .type(MediaType.APPLICATION_XML)
//            .put(arguments);
    }
}
