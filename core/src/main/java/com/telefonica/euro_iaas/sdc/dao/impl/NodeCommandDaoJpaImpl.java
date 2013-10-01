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

import java.util.ArrayList;
import java.util.List;

import com.telefonica.euro_iaas.commons.dao.AbstractBaseDao;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.dao.NodeCommandDao;
import com.telefonica.euro_iaas.sdc.model.NodeCommand;
import com.telefonica.euro_iaas.sdc.model.OS;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.NodeCommandSearchCriteria;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

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
