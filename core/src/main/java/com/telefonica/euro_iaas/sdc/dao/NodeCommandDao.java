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
	 *            the search criteria (containing pagination info, and some
	 *            fields criteria).
	 * @return the elements that match with the search criteria
	 */
	List<NodeCommand> findByCriteria(NodeCommandSearchCriteria criteria);

}