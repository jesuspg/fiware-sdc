package com.telefonica.euro_iaas.sdc;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;

import com.telefonica.euro_iaas.sdc.client.SDCClient;
import com.telefonica.euro_iaas.sdc.client.exception.ResourceNotFoundException;
import com.telefonica.euro_iaas.sdc.client.services.ProductService;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;

public class ProductIT {

    @Test
    public void shouldFailWhenLoadAnUnknownProduct() {
        // given

        SDCClient client = new SDCClient();
        String baseUrl = "http://localhost:8080/sdc/rest";
        String mediaType = "application/xml";

        // when

        ProductService productService = client.getProductService(baseUrl, mediaType);
        ProductRelease productRelease = null;
        try {
            productRelease = productService.load("kk", "");
            fail("The product kk should not exist");
        } catch (ResourceNotFoundException e) {
            // then
            assertNotNull(productService);
            assertNull(productRelease);

        }

    }

    @Test
    public void shouldLoadAProduct() throws ResourceNotFoundException {
        // given

        SDCClient client = new SDCClient();
        String baseUrl = "http://localhost:8080/sdc/rest";
        String mediaType = "application/xml";

        // when

        ProductService productService = client.getProductService(baseUrl, mediaType);
        ProductRelease productRelease = productService.load("tomcat", "6");
        // then
        assertNotNull(productService);
        assertNotNull(productRelease);

    }

    @Test
    public void shouldListProductCatalog() {
        // given
        SDCClient client = new SDCClient();
        String baseUrl = "http://localhost:8080/sdc/rest";
        String mediaType = "application/xml";

        // when

        ProductService productService = client.getProductService(baseUrl, mediaType);
        List<ProductRelease> list = productService.findAll(null, null, null, null, null, null);

        // then
        assertNotNull(list);

    }
}
