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

package com.telefonica.euro_iaas.sdc.manager.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;

import com.telefonica.euro_iaas.sdc.dao.MetadataDao;
import com.telefonica.euro_iaas.sdc.dao.ProductDao;
import com.telefonica.euro_iaas.sdc.manager.ProductManager;
import com.telefonica.euro_iaas.sdc.manager.ProductReleaseManager;
import com.telefonica.euro_iaas.sdc.model.Metadata;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.dto.ProductAndReleaseDto;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ProductReleaseSearchCriteria;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ProductSearchCriteria;
import com.telefonica.fiware.commons.dao.AlreadyExistsEntityException;
import com.telefonica.fiware.commons.dao.EntityNotFoundException;
import com.telefonica.fiware.commons.dao.InvalidEntityException;
import com.xmlsolutions.annotation.UseCase;

/**
 * Default ProductManager implementation.
 */
@UseCase(traceTo = "UC_101", status = "partially implemented")
public class ProductManagerImpl extends BaseInstallableManager implements ProductManager {

    private ProductDao productDao;
    private MetadataDao metadataDao;
    private ProductReleaseManager productReleaseManager;
    private static Logger log = Logger.getLogger("ProductManagerImpl");

    public Product insert(Product product, String tenantId) throws AlreadyExistsEntityException, InvalidEntityException {

        Product productOut;
        try {
            productOut = productDao.load(product.getName());
            log.log(Level.INFO, "Product " + productOut.getName() + " LOADED");
        } catch (EntityNotFoundException e) {

            Map<String, Metadata> productMetadataMap = new HashMap();

            List<Metadata> productMetadatas = product.getMetadatas();

            for (Metadata metadata : productMetadatas) {
                productMetadataMap.put(metadata.getKey(), metadata);
            }

            List<Metadata> defaultMetadatas = new ArrayList<Metadata>();
            defaultMetadatas.add(new Metadata("image", ""));
            defaultMetadatas.add(new Metadata("cookbook_url", ""));
            defaultMetadatas.add(new Metadata("cloud", "yes"));
            defaultMetadatas.add(new Metadata("installator", "chef"));
            defaultMetadatas.add(new Metadata("open_ports", "80 22"));
            defaultMetadatas.add(new Metadata("tenant_id", tenantId));

            for (Metadata metadata : defaultMetadatas) {

                if (!productMetadataMap.containsKey(metadata.getKey())) {
                    productMetadatas.add(metadata);
                }
            }

            productOut = productDao.create(product);
        }
        return productOut;
    }

    /**
     * {@inheritDoc}
     */

    @Override
    public List<Product> findAll() {
        return productDao.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Product> findByCriteria(ProductSearchCriteria criteria) {
        return productDao.findByCriteria(criteria);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ProductAndReleaseDto> findProductAndReleaseByCriteria(ProductSearchCriteria criteria) {
        List<Product> productList = productDao.findByCriteria(criteria);

        ProductReleaseSearchCriteria prCriteria = new ProductReleaseSearchCriteria();

        prCriteria.setPage(criteria.getPage());
        prCriteria.setPageSize(criteria.getPageSize());
        prCriteria.setOrderBy(criteria.getOrderBy());
        prCriteria.setOrderType(criteria.getOrderType());

        List<ProductAndReleaseDto> result = new ArrayList<ProductAndReleaseDto>();
        for (Product p : productList) {
            if (!StringUtils.isEmpty(p.getName())) {
                prCriteria.setProduct(p);
                List<ProductRelease> productReleaseList = productReleaseManager.findReleasesByCriteria(prCriteria);

                for (ProductRelease pr : productReleaseList) {

                    ProductAndReleaseDto productAndRelease = new ProductAndReleaseDto();
                    productAndRelease.setProduct(p);
                    productAndRelease.setVersion(pr.getVersion());

                    result.add(productAndRelease);
                }
            }
        }

        return result;
    }

    /**
     * Load metadata by productName and metadataName.
     * 
     * @param productName
     * @param metadataName
     * @return
     * @throws EntityNotFoundException
     */
    @Override
    public Metadata loadMetadata(String productName, String metadataName) throws EntityNotFoundException {

        Product product;
        Metadata metadata;
        try {
            product = this.load(productName);
        } catch (Exception e) {
            log.warning("The product: " + productName + " does not exist" + e.getMessage());
            throw new EntityNotFoundException(Product.class, productName, null);
        }

        try {
            metadata = product.getMetadata(metadataName);
            metadata = metadataDao.loadById(metadata.getId());
        } catch (Exception e) {
            log.warning("Exception: metadata not found or wrong" + metadataName);
            throw new EntityNotFoundException(Metadata.class, "Metadata not found : " + metadataName, null);
        }
        return metadata;
    }

    /**
     * Update metadata.
     * 
     * @param updatedMetadata
     */
    @Override
    public void updateMetadata(Metadata updatedMetadata) {
        metadataDao.merge(updatedMetadata);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Product load(String name) throws EntityNotFoundException {
        return productDao.load(name);
    }

    public boolean exist(String name) {
        try {
            load(name);
            return true;
        } catch (EntityNotFoundException e) {
            return false;
        }
    }

    /**
     * It deletes the product in DB.
     * 
     * @param product
     */
    @Override
    public void delete(Product product) {
        productDao.remove(product);
    }

    /**
     * It updates the product in DB.
     * 
     * @param product
     */
    @Override
    public void update(Product product) {
        productDao.update(product);
    }

    /**
     * @param productDao
     *            the productDao to set
     */
    public void setProductDao(ProductDao productDao) {
        this.productDao = productDao;
    }

    public void setProductReleaseManager(ProductReleaseManager productReleaseManager) {
        this.productReleaseManager = productReleaseManager;
    }

    public MetadataDao getMetadataDao() {
        return metadataDao;
    }

    public void setMetadataDao(MetadataDao metadataDao) {
        this.metadataDao = metadataDao;
    }
}
