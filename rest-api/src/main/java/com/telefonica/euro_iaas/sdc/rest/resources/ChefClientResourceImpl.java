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

import java.text.MessageFormat;

import javax.ws.rs.Path;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.sun.jersey.api.core.InjectParam;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.exception.ChefClientExecutionException;
import com.telefonica.euro_iaas.sdc.manager.ChefClientManager;
import com.telefonica.euro_iaas.sdc.manager.async.ChefClientAsyncManager;
import com.telefonica.euro_iaas.sdc.manager.async.TaskManager;
import com.telefonica.euro_iaas.sdc.model.Task;
import com.telefonica.euro_iaas.sdc.model.Task.TaskStates;
import com.telefonica.euro_iaas.sdc.model.dto.ChefClient;

/**
 * 
 */

/**
 * @author jesus.movilla
 */
@Path("/vdc/{vdc}/chefClient")
@Component
@Scope("request")
public class ChefClientResourceImpl implements ChefClientResource {

    @InjectParam("chefClientManager")
    private ChefClientManager chefClientManager;
    @InjectParam("chefClientAsyncManager")
    private ChefClientAsyncManager chefClientAsyncManager;
    @InjectParam("taskManager")
    private TaskManager taskManager;

    /*
     * (non-Javadoc)
     * @see com.telefonica.euro_iaas.sdc.rest.resources.ChefClientResource#findAll()
     */
    public ChefClient findByHostname(String hostname) throws EntityNotFoundException, ChefClientExecutionException {
        return chefClientManager.chefClientfindByHostname(hostname);
    }

    public ChefClient load(String chefClientName) throws EntityNotFoundException, ChefClientExecutionException {

        return chefClientManager.chefClientload(chefClientName);
    }

    public Task delete(String vdc, String chefClientName, String callback) throws ChefClientExecutionException {

        Task task = createTask(MessageFormat.format("Delete ChefClient {0} from Chef Server", chefClientName), vdc);

        chefClientAsyncManager.chefClientDelete(vdc, chefClientName, task, callback);
        return task;

    }

    private Task createTask(String description, String vdc) {
        Task task = new Task(TaskStates.RUNNING);
        task.setDescription(description);
        task.setVdc(vdc);
        return taskManager.createTask(task);
    }

}
