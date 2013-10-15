/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.sdc.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.telefonica.euro_iaas.commons.dao.AbstractBaseDao;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.dao.ProductReleaseDao;
import com.telefonica.euro_iaas.sdc.model.OS;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ProductReleaseSearchCriteria;

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
            String[] keys = {"product", "version"};
            Object[] values = {product, version};
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
