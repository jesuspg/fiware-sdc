/**
 *   (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights
 *   Reserved.
 * 
 *   The copyright to the software program(s) is property of Telefonica I+D.
 *   The program(s) may be used and or copied only with the express written
 *   consent of Telefonica I+D or in accordance with the terms and conditions
 *   stipulated in the agreement/contract under which the program(s) have
 *   been supplied.
 */

package com.telefonica.euro_iaas.sdc.dao.impl;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.AbstractBaseDao;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.dao.ProductDao;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ProductSearchCriteria;
import org.hibernate.Criteria;
import org.hibernate.Session;

/**
 * JPA implementation for ApplicationDao.
 * 
 * @author Sergio Arroyo
 * @version $Id: $
 */
public class ProductDaoJpaImpl extends AbstractBaseDao<Product, String> implements ProductDao {

    /** {@inheritDoc} */
    @Override
    public List<Product> findAll() {
        return super.findAll(Product.class);
    }

    /** {@inheritDoc} */
    @Override
    public Product load(String name) throws EntityNotFoundException {
        return super.loadByField(Product.class, "name", name);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<Product> findByCriteria(ProductSearchCriteria criteria) {
        Session session = (Session) getEntityManager().getDelegate();
        Criteria baseCriteria = session.createCriteria(Product.class);
        return setOptionalPagination(criteria, baseCriteria).list();
    }

}
