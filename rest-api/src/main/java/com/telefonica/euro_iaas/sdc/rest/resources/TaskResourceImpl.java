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

package com.telefonica.euro_iaas.sdc.rest.resources;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.Path;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.telefonica.fiware.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.manager.async.TaskManager;
import com.telefonica.euro_iaas.sdc.model.Task;
import com.telefonica.euro_iaas.sdc.model.Task.TaskStates;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.TaskSearchCriteria;

/**
 * Default TaskResource implementation.
 * 
 * @author Sergio Arroyo
 */
@Path("/vdc/{vdc}/task")
@Component
@Scope("request")
public class TaskResourceImpl implements TaskResource {

    // @InjectParam("taskManager")
    private TaskManager taskManager;

    private static Logger log = Logger.getLogger("TaskResourceImpl");

    @Override
    public Task load(Long id) throws EntityNotFoundException {

        return taskManager.load(id);
    }

    @Override
    public List<Task> findAll(Integer page, Integer pageSize, String orderBy, String orderType,
            List<TaskStates> states, String resource, String owner, Date fromDate, Date toDate, String vdc) {
        TaskSearchCriteria criteria = new TaskSearchCriteria();
        criteria.setVdc(vdc);
        if (page != null && pageSize != null) {
            criteria.setPage(page);
            criteria.setPageSize(pageSize);
        }
        if (!StringUtils.isEmpty(orderBy)) {
            criteria.setOrderBy(orderBy);
        }
        if (!StringUtils.isEmpty(orderType)) {
            criteria.setOrderBy(orderType);
        }
        criteria.setStates(states);
        criteria.setResource(resource);
        criteria.setOwner(owner);
        criteria.setFromDate(fromDate);
        criteria.setToDate(toDate);
        return taskManager.findByCriteria(criteria);
    }

    public void setTaskManager(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

}
