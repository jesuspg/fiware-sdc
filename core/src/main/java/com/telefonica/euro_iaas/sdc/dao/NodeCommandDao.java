/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.sdc.dao;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.BaseDAO;
import com.telefonica.euro_iaas.sdc.model.NodeCommand;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.NodeCommandSearchCriteria;

/**
 * Defines the methods needed to persist NodeCommand Object
 * 
 * @author Jesus M. Movilla
 * @version $Id: $
 */
public interface NodeCommandDao extends BaseDAO<NodeCommand, Long> {

    /**
     * Find by criteria
     * 
     * @param criteria
     *            the search criteria (containing pagination info, and some fields criteria).
     * @return the elements that match with the search criteria
     */
    List<NodeCommand> findByCriteria(NodeCommandSearchCriteria criteria);

}
