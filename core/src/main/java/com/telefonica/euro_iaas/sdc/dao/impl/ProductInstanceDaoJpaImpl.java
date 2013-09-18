package com.telefonica.euro_iaas.sdc.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.telefonica.euro_iaas.commons.dao.AbstractBaseDao;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.dao.ProductInstanceDao;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ProductInstanceSearchCriteria;
/**
 * JPA implementation for AppState.
 *
 * @author Sergio Arroyo
 *
 */
public class ProductInstanceDaoJpaImpl
        extends AbstractBaseDao<ProductInstance, Long>
        implements ProductInstanceDao {

    /** {@inheritDoc} */
    @Override
    public List<ProductInstance> findAll() {
        return super.findAll(ProductInstance.class);
    }

    /** {@inheritDoc} */
    @Override
    public ProductInstance load(Long id) throws EntityNotFoundException {
        return super.loadByField(ProductInstance.class, "id", id);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<ProductInstance> findByCriteria(
            ProductInstanceSearchCriteria criteria) {
        Session session = (Session) getEntityManager().getDelegate();
        Criteria baseCriteria = session.createCriteria(ProductInstance.class);
        if (criteria.getVM() != null) {
            baseCriteria.add(Restrictions.eq(ProductInstance.VM_FIELD,
                    criteria.getVM()));
        }
        if (criteria.getStatus() != null) {
            baseCriteria.add(Restrictions.eq(ProductInstance.STATUS_FIELD,
                    criteria.getStatus()));
        }
        if (criteria.getProduct() != null) {
            baseCriteria.add(Restrictions.eq(ProductInstance.PRODUCT_FIELD,
                    criteria.getProduct()));
        }
        return setOptionalPagination(criteria, baseCriteria).list();
    }

}
