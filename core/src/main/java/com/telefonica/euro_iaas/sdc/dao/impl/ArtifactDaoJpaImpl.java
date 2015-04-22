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
import org.hibernate.criterion.Restrictions;

import com.telefonica.euro_iaas.sdc.dao.ArtifactDao;
import com.telefonica.euro_iaas.sdc.model.Artifact;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ArtifactSearchCriteria;
import com.telefonica.fiware.commons.dao.EntityNotFoundException;

/**
 * JPA implementation for ApplicationInstanceDao.
 * 
 * @author Sergio Arroyo
 */
public class ArtifactDaoJpaImpl extends AbstractInstallableInstanceDaoJpaIml<Artifact, String> implements ArtifactDao {

    /** {@inheritDoc} */
    @Override
    public List<Artifact> findAll() {
        return super.findAll(Artifact.class);
    }

    /** {@inheritDoc} */
    /*
     * @Override public Artifact load(Long id) throws EntityNotFoundException { return super.loadByField(Artifact.class,
     * "id", id); }
     */

    /** {@inheritDoc} */
    @Override
    public Artifact load(String name) throws EntityNotFoundException {
        return super.loadByField(Artifact.class, "name", name);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<Artifact> findByCriteria(ArtifactSearchCriteria criteria) {
        Session session = (Session) getEntityManager().getDelegate();
        Criteria baseCriteria = session.createCriteria(Artifact.class);

        if (!StringUtils.isEmpty(criteria.getVdc())) {
            baseCriteria.add(Restrictions.eq(Artifact.VDC_FIELD, criteria.getVdc()));
        }

        /*
         * if (criteria.getStatus() != null) { baseCriteria.add(Restrictions.eq(Artifact.STATUS_FIELD,
         * criteria.getStatus())); }
         */
        if (criteria.getProductInstance() != null) {
            baseCriteria.add(Restrictions.eq(Artifact.PRODUCT_FIELD, criteria.getProductInstance()));
        }

        if (!StringUtils.isEmpty(criteria.getArtifactName())) {
            // baseCriteria.createAlias("artifact", "rls")
            // .createAlias("rls.product", "prod");
            baseCriteria.add(Restrictions.eq("prod.name", criteria.getArtifactName()));
        }
        return setOptionalPagination(criteria, baseCriteria).list();
    }

}
