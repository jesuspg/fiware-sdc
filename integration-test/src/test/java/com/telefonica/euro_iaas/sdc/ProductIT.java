package com.telefonica.euro_iaas.sdc;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.telefonica.euro_iaas.sdc.client.SDCClient;
import com.telefonica.euro_iaas.sdc.client.exception.ResourceNotFoundException;
import com.telefonica.euro_iaas.sdc.client.services.ProductService;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.Metadata;
import com.telefonica.euro_iaas.sdc.model.OS;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.dto.ProductReleaseDto;

public class ProductIT {

    SDCClient client;
    String baseUrl;
    String mediaType;

    @Before
    public void setUp() {
        client = new SDCClient();
        baseUrl = "http://localhost:8888/sdc/rest";
        mediaType = "application/xml";
    }

    @Test
    public void shouldFailWhenLoadAnUnknownProduct() {
        // given
        ProductService productService = client.getProductService(baseUrl, mediaType);

        // when

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

        ProductService productService = client.getProductService(baseUrl, mediaType);
        // when

        ProductRelease productRelease = productService.load("tomcat", "6");
        // then
        assertNotNull(productService);
        assertNotNull(productRelease);

    }

    @Test
    public void shouldListProductCatalog() {
        // given
        ProductService productService = client.getProductService(baseUrl, mediaType);

        // when

        List<ProductRelease> list = productService.findAll(null, null, null, null, null, null);

        // then
        assertNotNull(list);
        assertFalse(list.isEmpty());

    }

    @Test
    public void shouldListProductCatalogFindByTomcat() {
        // given

        // when
        ProductService productService = client.getProductService(baseUrl, mediaType);

        List<ProductRelease> list = productService.findAll(null, null, null, null, "tomcat", null);
        // then
        assertNotNull(list);
        assertFalse(list.isEmpty());

    }

    @Test
    @Ignore
    public void shouldReturnError404WithFindByUnknownProduct() {
        // given

        ProductService productService = client.getProductService(baseUrl, mediaType);
        // when

        List<ProductRelease> list = productService.findAll(null, null, null, null, "kk", null);

        // then
        assertNotNull(list);
        assertTrue(list.isEmpty());

    }

    @Test
    @Ignore
    public void shouldAddProductToCatalog() {
        // given
        String productName = "tomcat";
        String version = "6";
        String description = "tomcat 6";
        InputStream cookbook = this.getClass().getResourceAsStream("/files/" + productName + version + "-cookbook.tar");
        InputStream files = this.getClass().getResourceAsStream("/files/" + productName + version + "-bin.tar");

        List<OS> supportedOS = new ArrayList<OS>();
        OS os1 = new OS();
        os1.setName("Debian");
        os1.setOsType("95");
        os1.setVersion("9");
        supportedOS.add(os1);
        supportedOS.add(os1);
        List<ProductRelease> transitableReleases = new ArrayList<ProductRelease>();
        ProductRelease productRelease1 = new ProductRelease();
        Product product = new Product();
        product.setName("tomcat");
        productRelease1.setProduct(product);
        transitableReleases.add(productRelease1);
        List<Attribute> attributes = new ArrayList<Attribute>();

        String releaseNotes = "release notes";
        List<Metadata> metadatas = new ArrayList<Metadata>();
        ProductReleaseDto productReleaseDto = new ProductReleaseDto(productName, description, version, releaseNotes,
                attributes, metadatas, supportedOS, transitableReleases);
        // when
        ProductRelease productRelease = client.getProductService(baseUrl, mediaType).add(productReleaseDto, cookbook,
                files);

        // then
        assertNotNull(productRelease);
    }
}
