/**
 * 
 */
package com.telefonica.euro_iaas.sdc.manager;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.dao.OSDao;
import com.telefonica.euro_iaas.sdc.dao.ProductDao;
import com.telefonica.euro_iaas.sdc.dao.ProductReleaseDao;
import com.telefonica.euro_iaas.sdc.manager.impl.ProductManagerImpl;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.Metadata;
import com.telefonica.euro_iaas.sdc.model.OS;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider;
import com.telefonica.euro_iaas.sdc.validation.ProductReleaseValidator;

/**
 * @author jesus.movilla
 */
public class ProductManagerImplTest extends TestCase {
    private SystemPropertiesProvider propertiesProvider;
    private ProductDao productDao;
    private OSDao osDao;
    private ProductReleaseDao productReleaseDao;
    private ProductReleaseValidator prValidator;
    private ProductManagerImpl manager;

    private ProductRelease productRelease;
    private Product product, loadedProduct;

    @Override
    @Before
    public void setUp() throws Exception {

        product = new Product("product", "description");
        product.addAttribute(new Attribute("attkey", "attvalue"));
        product.addMetadata(new Metadata("metkey", "metvalue"));

        loadedProduct = new Product("Product", "description");
        loadedProduct.addAttribute(new Attribute("loadedattkey", "loadedattvalue"));
        loadedProduct.addMetadata(new Metadata("loadedmetkey", "loadedmetvalue"));

        List<OS> ooss = new ArrayList<OS>();
        ooss.add(new OS("ostype", "osname", "osdescription", "osbersion"));

        List<Attribute> attrs = new ArrayList<Attribute>();
        attrs.add(new Attribute("attkey", "attvalue"));

        productRelease = new ProductRelease();
        productRelease.setProduct(product);
        productRelease.setVersion("version");
        productRelease.setSupportedOOSS(ooss);
        productRelease.setPrivateAttributes(attrs);

        osDao = mock(OSDao.class);
        productDao = mock(ProductDao.class);
        when(productDao.load(product.getName())).thenReturn(loadedProduct);

        productReleaseDao = mock(ProductReleaseDao.class);
        propertiesProvider = mock(SystemPropertiesProvider.class);

        manager = new ProductManagerImpl();
        manager.setOsDao(osDao);
        manager.setProductDao(productDao);
        manager.setProductReleaseDao(productReleaseDao);
        manager.setPropertiesProvider(propertiesProvider);
        manager.setValidator(prValidator);
    }

    @Test
    public void testInsertProductRelease() throws Exception {
        Mockito.doThrow(new EntityNotFoundException(ProductRelease.class, "test", productRelease))
                        .when(productReleaseDao).load(any(Product.class), any(String.class));

        when(productReleaseDao.create(any(ProductRelease.class))).thenReturn(productRelease);
        ProductRelease createdProductRelease = manager.insert(productRelease);
        assertEquals(createdProductRelease.getProduct().getName(), productRelease.getProduct().getName());
    }
}
