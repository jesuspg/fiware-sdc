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

import com.telefonica.euro_iaas.sdc.dao.NodeCommandDao;
import com.telefonica.euro_iaas.sdc.model.NodeCommand;
import com.telefonica.euro_iaas.sdc.model.OS;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.NodeCommandSearchCriteria;
import com.telefonica.fiware.commons.dao.AbstractBaseDao;
import com.telefonica.fiware.commons.dao.EntityNotFoundException;

/**
 * JPA based implementation for NodeCommandDao.
 * 
 * @author Jesus M. Movilla
 */
public class NodeCommandDaoJpaImpl extends AbstractBaseDao<NodeCommand, Long> implements NodeCommandDao {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<NodeCommand> findAll() {
        return super.findAll(NodeCommand.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NodeCommand load(Long arg0) throws EntityNotFoundException {
        return super.loadByField(NodeCommand.class, "id", arg0);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<NodeCommand> findByCriteria(NodeCommandSearchCriteria criteria) {
        Session session = (Session) getEntityManager().getDelegate();
        Criteria baseCriteria = session.createCriteria(NodeCommand.class);
        if (criteria.getOS() != null) {
            baseCriteria.add(Restrictions.eq("os", criteria.getOS()));
        }

        List<NodeCommand> nodeCommands = setOptionalPagination(criteria, baseCriteria).list();

        if (criteria.getOS() != null) {
            nodeCommands = filterByOS(nodeCommands, criteria.getOS());
        }
        return nodeCommands;

    }

    /**
     * Filter the result by os
     * 
     * @param nodeCommands
     * @param os
     * @return
     */
    private List<NodeCommand> filterByOS(List<NodeCommand> nodeCommands, OS os) {
        List<NodeCommand> result = new ArrayList<NodeCommand>();
        for (NodeCommand nodeCommand : nodeCommands) {
            if (nodeCommand.getOS().getOsType().equals(os.getOsType())) {
                result.add(nodeCommand);
            }
        }
        return result;
    }

}
