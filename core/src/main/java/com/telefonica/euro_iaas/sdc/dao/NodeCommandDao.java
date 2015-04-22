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

package com.telefonica.euro_iaas.sdc.dao;

import java.util.List;

import com.telefonica.euro_iaas.sdc.model.NodeCommand;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.NodeCommandSearchCriteria;
import com.telefonica.fiware.commons.dao.BaseDAO;

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
