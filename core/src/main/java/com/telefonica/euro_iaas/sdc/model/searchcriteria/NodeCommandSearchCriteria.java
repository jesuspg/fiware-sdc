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

package com.telefonica.euro_iaas.sdc.model.searchcriteria;

import com.telefonica.euro_iaas.sdc.model.OS;
import com.telefonica.fiware.commons.dao.AbstractSearchCriteria;

/**
 * Provides some criteria to search NodeCommand entities.
 * 
 * @author Jesus M. Movilla
 */
public class NodeCommandSearchCriteria extends AbstractSearchCriteria {

    /**
     * The os.
     */
    private OS os;

    /**
     * Default constructor
     */
    public NodeCommandSearchCriteria() {
    }

    /**
     * @param page
     * @param pagesize
     * @param orderBy
     * @param orderType
     * @param os
     */
    public NodeCommandSearchCriteria(Integer page, Integer pageSize, String orderBy, String orderType, OS os) {
        super(page, pageSize, orderBy, orderType);
        this.os = os;
    }

    /**
     * @param orderBy
     * @param orderType
     * @param os
     */
    public NodeCommandSearchCriteria(String orderBy, String orderType, OS os) {
        super(orderBy, orderType);
        this.os = os;
    }

    /**
     * @param page
     * @param pagesize
     * @param os
     */
    public NodeCommandSearchCriteria(Integer page, Integer pageSize, OS os) {
        super(page, pageSize);
        this.os = os;
    }

    /**
     * @param os
     */
    public NodeCommandSearchCriteria(OS os) {
        this.os = os;
    }

    /**
     * @return the os
     */
    public OS getOS() {
        return os;
    }

    /**
     * @param os
     *            the os to set
     */
    public void setOS(OS os) {
        this.os = os;
    }
}
