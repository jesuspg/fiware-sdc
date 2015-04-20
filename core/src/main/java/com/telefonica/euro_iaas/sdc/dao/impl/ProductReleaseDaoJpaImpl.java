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

package com.telefonica.euro_iaas.sdc.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.telefonica.euro_iaas.sdc.dao.ProductReleaseDao;
import com.telefonica.euro_iaas.sdc.model.OS;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ProductReleaseSearchCriteria;
import com.telefonica.fiware.commons.dao.AbstractBaseDao;
import com.telefonica.fiware.commons.dao.EntityNotFoundException;

/**
 * JPA based implementation for ProductReleaseDao.
 * 
 * @author Sergio Arroyo
 */
public class ProductReleaseDaoJpaImpl extends AbstractBaseDao<ProductRelease, Long> implements ProductReleaseDao {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ProductRelease> findAll() {
        return super.findAll(ProductRelease.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProductRelease load(Long arg0) throws EntityNotFoundException {
        return super.loadByField(ProductRelease.class, "id", arg0);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<ProductRelease> findByCriteria(ProductReleaseSearchCriteria criteria) {
        Session session = (Session) getEntityManager().getDelegate();
        Criteria baseCriteria = session.createCriteria(ProductRelease.class);
        if (criteria.getProduct() != null) {
            baseCriteria.add(Restrictions.eq("product", criteria.getProduct()));
        }

        List<ProductRelease> productReleases = setOptionalPagination(criteria, baseCriteria).list();

        if (criteria.getOSType() != null) {
            productReleases = filterByOSType(productReleases, criteria.getOSType());
        }
        return productReleases;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProductRelease load(Product product, String version) throws EntityNotFoundException {
        Session session = (Session) getEntityManager().getDelegate();
        Criteria baseCriteria = session.createCriteria(ProductRelease.class);
        baseCriteria.add(Restrictions.eq("product", product));
        baseCriteria.add(Restrictions.eq("version", version));

        ProductRelease release = (ProductRelease) baseCriteria.uniqueResult();
        if (release == null) {
            String[] keys = { "product", "version" };
            Object[] values = { product, version };
            throw new EntityNotFoundException(ProductRelease.class, keys, values);
        }
        return release;
    }

    /**
     * Filter the result by product release.
     * 
     * @param applications
     * @param product
     *            Release
     * @return
     */
    private List<ProductRelease> filterByOSType(List<ProductRelease> productReleases, String osType) {
        List<ProductRelease> result = new ArrayList<ProductRelease>();
        for (ProductRelease productRelease : productReleases) {
            for (OS os : productRelease.getSupportedOOSS()) {
                if (os.getOsType().equals(osType)) {
                    result.add(productRelease);
                }
            }

        }
        return result;
    }

}
