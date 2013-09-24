package com.telefonica.euro_iaas.sdc.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.telefonica.euro_iaas.commons.dao.AbstractBaseDao;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.dao.ProductReleaseDao;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ProductReleaseSearchCriteria;
/**
 * JPA based  implementation for ProductReleaseDao.
 *
 * @author Sergio Arroyo
 *
 */
public class ProductReleaseDaoJpaImpl
    extends AbstractBaseDao<ProductRelease, Long>
    implements ProductReleaseDao {

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
    public List<ProductRelease> findByCriteria(
            ProductReleaseSearchCriteria criteria) {
        Session session = (Session) getEntityManager().getDelegate();
        Criteria baseCriteria = session.createCriteria(
                ProductRelease.class);
        if (criteria.getProduct() != null) {
            baseCriteria.add(Restrictions.eq(
                    "product", criteria.getProduct()));
        }
        return setOptionalPagination(criteria, baseCriteria).list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProductRelease load(Product product, String version)
            throws EntityNotFoundException {
        Session session = (Session) getEntityManager().getDelegate();
        Criteria baseCriteria = session.createCriteria(
                ProductRelease.class);
        baseCriteria.add(Restrictions.eq("product", product));
        baseCriteria.add(Restrictions.eq("version", version));

        ProductRelease release = (ProductRelease) baseCriteria.uniqueResult();
        if (release == null) {
            String[] keys = {"product", "version"};
            Object[] values = {product, version};
            throw new EntityNotFoundException(ProductRelease.class,
                    keys, values);
        }
        return release;
    }

}
