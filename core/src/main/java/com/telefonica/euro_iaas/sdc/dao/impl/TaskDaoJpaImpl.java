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

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;

import com.telefonica.euro_iaas.sdc.dao.TaskDao;
import com.telefonica.euro_iaas.sdc.model.Task;
import com.telefonica.euro_iaas.sdc.model.Task.TaskStates;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.TaskSearchCriteria;
import com.telefonica.fiware.commons.dao.AbstractBaseDao;
import com.telefonica.fiware.commons.dao.EntityNotFoundException;

/**
 * TaskDao JPA based implementation.
 * 
 * @author Sergio Arroyo
 */
public class TaskDaoJpaImpl extends AbstractBaseDao<Task, Long> implements TaskDao {

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
        Criteria baseCriteria = session.createCriteria(Task.class);

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
