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

import javax.persistence.Query;

import com.telefonica.euro_iaas.sdc.dao.MetadataDao;
import com.telefonica.euro_iaas.sdc.model.Metadata;
import com.telefonica.fiware.commons.dao.AbstractBaseDao;
import com.telefonica.fiware.commons.dao.EntityNotFoundException;

/**
 * JPA implementation for MetadataDao.
 */

public class MetadataDaoJpaImpl extends AbstractBaseDao<Metadata, String> implements MetadataDao {

    public Metadata loadById(Long id) throws EntityNotFoundException {

        Query query = getEntityManager().createQuery("select m from Metadata m  where m.id = " + id);

        return (Metadata) query.getSingleResult();

    }

    @Override
    public Metadata load(String s) throws EntityNotFoundException {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    @Override
    public List<Metadata> findAll() {
        return super.findAll(Metadata.class);
    }

    /**
     * @param metadata
     */
    public Metadata merge(Metadata metadata) {
        return super.getEntityManager().merge(metadata);
    }
}
