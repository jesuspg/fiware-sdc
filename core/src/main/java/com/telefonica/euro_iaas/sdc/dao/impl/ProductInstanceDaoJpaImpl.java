package com.telefonica.euro_iaas.sdc.dao.impl;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.dao.ProductInstanceDao;
import com.telefonica.euro_iaas.sdc.exception.NotUniqueResultException;
import com.telefonica.euro_iaas.sdc.model.InstallableInstance;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ProductInstanceSearchCriteria;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 * JPA implementation for AppState.
 * 
 * @author Sergio Arroyo
 */
public class ProductInstanceDaoJpaImpl extends AbstractInstallableInstanceDaoJpaIml<ProductInstance, Long> implements
        ProductInstanceDao {

    @PersistenceContext(unitName = "sdc", type = PersistenceContextType.EXTENDED)
    private EntityManager entityManager;

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

    /** {@inheritDoc} */
    @Override
    public ProductInstance load(String name) throws EntityNotFoundException {
        //
        // try
        // {
        return findByProductInstanceName(name);

        /*
         * } catch (Exception e) { try { return super.loadByField(ProductInstance.class, "name", name); } catch
         * (Exception e2) { e.printStackTrace(); } } return null;
         */

    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<ProductInstance> findByCriteria(ProductInstanceSearchCriteria criteria) {
        Session session = (Session) getEntityManager().getDelegate();
        Criteria baseCriteria = session.createCriteria(ProductInstance.class);

        if (!StringUtils.isEmpty(criteria.getVdc())) {
            baseCriteria.add(Restrictions.eq(InstallableInstance.VDC_FIELD, criteria.getVdc()));
        }
        if (criteria.getVM() != null) {
            baseCriteria.add(getVMCriteria(baseCriteria, criteria.getVm()));
        }
        if (criteria.getStatus() != null) {
            baseCriteria.add(Restrictions.eq(InstallableInstance.STATUS_FIELD, criteria.getStatus()));
        }
        if (criteria.getProductRelease() != null) {
            baseCriteria.add(Restrictions.eq(ProductInstance.PRODUCT_FIELD, criteria.getProductRelease()));
        }

        if (!StringUtils.isEmpty(criteria.getProductName())) {
            baseCriteria.createAlias("productRelease", "rls").createAlias("rls.product", "prod");
            baseCriteria.add(Restrictions.eq("prod.name", criteria.getProductName()));
        }
        return setOptionalPagination(criteria, baseCriteria).list();
    }

    @Override
    public ProductInstance findUniqueByCriteria(ProductInstanceSearchCriteria criteria) throws NotUniqueResultException {
        List<ProductInstance> instances = findByCriteria(criteria);
        if (instances.size() != 1) {
            throw new NotUniqueResultException();
        }
        return instances.iterator().next();
    }

    @SuppressWarnings("unchecked")
    @Override
    public ProductInstance findByProductInstanceName(String productInstanceName) throws EntityNotFoundException {

        // Query query = entityManager.createQuery("select p from artifact " +

        // p join fetch p.productinstance where p.name = :name" );
        /*
         * SELECT * FROM productinstance LEFT JOIN artifact ON productinstance.id=artifact.productinstance_id;
         */
        // Query query = entityManager.createQuery
        // ("select p from ProductInstance p left join fetch p.artifact where p.name = '"+productInstanceName+"'");
        Query query = (Query) entityManager.createQuery("select p from ProductInstance p  where p.name = '"
                + productInstanceName + "'");

        // where p.name = '" + productInstanceName+"'");

        // select claseA from A claseA left join fetch claseA.b claseB

        // Query query =
        // entityManager.createQuery("select p from EnvironmentInstance" +
        // " p join fetch p.tierInstances where p.name = :name" );

        // query.setParameter("name", productInstanceName);

        ProductInstance productInstance = null;
        try {
            productInstance = (ProductInstance) query.getSingleResult();
        } catch (NoResultException e) {
            String message = " No ProductInstance found in the database " + "with name: " + productInstanceName;
            System.out.println(message);
            throw new EntityNotFoundException(ProductInstance.class, "name", productInstanceName);
        }
        return productInstance;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<ProductInstance> findByHostname(String hostname) throws EntityNotFoundException {

        Query query = (Query) entityManager.createQuery("select p from InstallableInstance p  where p.vm.hostname = '"
                + hostname + "'");

        List<ProductInstance> productInstances = null;

        productInstances = (List<ProductInstance>) query.getResultList();

        if (productInstances.isEmpty()) {
            String message = " No ProductInstancse found in the database " + "installed on hostname: : " + hostname;
            System.out.println(message);
            throw new EntityNotFoundException(ProductInstance.class, "hostaname", hostname);
        }
        return productInstances;
    }

}
