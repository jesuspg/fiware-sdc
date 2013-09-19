package com.telefonica.euro_iaas.sdc.dao.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;

import com.telefonica.euro_iaas.commons.dao.AbstractBaseDao;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.dao.TaskDao;
import com.telefonica.euro_iaas.sdc.model.Task;
import com.telefonica.euro_iaas.sdc.model.Task.TaskStates;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.TaskSearchCriteria;

/**
 * TaskDao JPA based implementation.
 * @author Sergio Arroyo
 *
 */
public class TaskDaoJpaImpl extends AbstractBaseDao<Task, Long>
    implements TaskDao{

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Task> findAll() {
        return super.findAll(Task.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Task load(Long arg0) throws EntityNotFoundException {
        return super.loadByField(Task.class, "id", arg0);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<Task> findByCriteria(TaskSearchCriteria criteria) {
        Session session = (Session) getEntityManager().getDelegate();
        Criteria baseCriteria = session
                .createCriteria(Task.class);

        if (!StringUtils.isEmpty(criteria.getVdc())) {
            baseCriteria.add(Restrictions.eq("vdc", criteria.getVdc()));
        }

        if (criteria.getStates() != null && !criteria.getStates().isEmpty()) {
            Criterion statusCr = null;
            for (TaskStates states : criteria.getStates()) {
                statusCr = addStatus(statusCr, states);
            }
            baseCriteria.add(statusCr);
        }
        if (criteria.getFromDate() != null) {
            baseCriteria.add(Restrictions.ge("startTime", criteria.getFromDate()));
        }
        if (criteria.getToDate() != null) {
            baseCriteria.add(Restrictions.le("startTime", criteria.getToDate()));
        }
        if (!StringUtils.isEmpty(criteria.getResource())) {
            baseCriteria.add(Restrictions.eq("result.href", criteria.getResource()));
        }
        if (!StringUtils.isEmpty(criteria.getOwner())) {
            baseCriteria.add(Restrictions.eq("owner.href", criteria.getOwner()));
        }
        return setOptionalPagination(criteria, baseCriteria).list();
    }

    private Criterion addStatus(Criterion statusCr, TaskStates state) {
        SimpleExpression expression = Restrictions.eq("status", state);
        if (statusCr == null) {
            statusCr = expression;
        } else {
            statusCr = Restrictions.or(statusCr, expression);
        }
        return statusCr;
    }
}
