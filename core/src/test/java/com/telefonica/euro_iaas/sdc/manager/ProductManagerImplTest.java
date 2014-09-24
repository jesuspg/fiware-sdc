/**
 * Copyright 2014 Telefonica Investigación y Desarrollo, S.A.U <br>
 * This file is part of FI-WARE project.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License.
 * </p>
 * <p>
 * You may obtain a copy of the License at:<br>
 * <br>
 * http://www.apache.org/licenses/LICENSE-2.0
 * </p>
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * </p>
 * <p>
 * See the License for the specific language governing permissions and limitations under the License.
 * </p>
 * <p>
 * For those usages not covered by the Apache version 2.0 License please contact with opensource@tid.es
 * </p>
 */

package com.telefonica.euro_iaas.sdc.manager;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.dao.ProductDao;
import com.telefonica.euro_iaas.sdc.manager.impl.ProductManagerImpl;
import com.telefonica.euro_iaas.sdc.model.Metadata;
import com.telefonica.euro_iaas.sdc.model.Product;

import junit.framework.TestCase;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

/**
 * Unit test of ProductMangerImpl
 * @author jesus.movilla
 *
 */
public class ProductManagerImplTest extends TestCase {

    private Product product, createdProduct = null;
    private ProductDao productDao = null;
    
    private ProductManagerImpl productManager;
                    
    @Before
    public void setUp() throws Exception {
        productManager = new ProductManagerImpl();
        
        product = new Product("productname","productDescription");
        List<Metadata> extmetadatas = new ArrayList<Metadata>();
        extmetadatas.add(new Metadata("image","otro")); 
        extmetadatas.add(new Metadata("key","value"));
        product.setMetadatas(extmetadatas);
        
        createdProduct = new Product("createdproductname","createdproductDescription");
        
        //createdProduct.setMetadatas(extmetadatas);
        createdProduct.addMetadata(new Metadata("image","otro")); //centos6.3_sdc
        createdProduct.addMetadata(new Metadata("cookbook_url",""));
        createdProduct.addMetadata(new Metadata("cloud","yes"));
        createdProduct.addMetadata(new Metadata("installator","chef"));
        createdProduct.addMetadata(new Metadata("open_ports","80 22"));
        createdProduct.addMetadata(new Metadata("key","value"));
        
        productDao = mock(ProductDao.class);
        when(productDao.create(any(Product.class))).thenReturn(createdProduct);    
    }
    
    /**
     * Test insert Product with metadatas. One is default and the other is new.
     * @throws Exception
     */
    @Test
    public void testInsertNewProduct() throws Exception {
        productManager.setProductDao(productDao);
        
        when(productDao.load(any(String.class))).thenThrow(new EntityNotFoundException(Product.class, "name", product.getName()));
        Product createdProduct = productManager.insert(product, "");
        assertEquals(createdProduct.getMetadatas().size(), 6); 
    }
    
    /**
     * Test Reinsert old Product.
     * @throws Exception
     */
    @Test
    public void testReinsertProduct() throws Exception {
        productManager.setProductDao(productDao);
        
        when(productDao.load(any(String.class))).thenReturn(product);
        Product createdProduct = productManager.insert(product, "");
        assertEquals(createdProduct.getName(), product.getName()); 
    }
}
