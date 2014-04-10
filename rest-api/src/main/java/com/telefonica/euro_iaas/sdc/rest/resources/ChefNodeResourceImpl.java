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
import com.telefonica.euro_iaas.sdc.exception.NodeExecutionException;
import com.telefonica.euro_iaas.sdc.manager.async.ChefNodeAsyncManager;
import com.telefonica.euro_iaas.sdc.manager.async.TaskManager;
import com.telefonica.euro_iaas.sdc.model.Task;
import com.telefonica.euro_iaas.sdc.model.Task.TaskStates;

/**
 * Default ChefNodeResource implementation.
 * 
 * @author Jesus M. Movilla
 */

@Path("/vdc/{vdc}/node")
@Component
@Scope("request")
public class ChefNodeResourceImpl implements ChefNodeResource {

    @InjectParam("chefNodeAsyncManager")
    private ChefNodeAsyncManager chefNodeAsyncManager;
    @InjectParam("taskManager")
    private TaskManager taskManager;

    public Task delete(String vdc, String chefNodeName, String callback) throws NodeExecutionException {

        Task task = createTask(MessageFormat.format("Delete node {0} from Chef Server", chefNodeName), vdc);

        chefNodeAsyncManager.chefNodeDelete(vdc, chefNodeName, task, callback);

        return task;

    }

    private Task createTask(String description, String vdc) {
        Task task = new Task(TaskStates.RUNNING);
        task.setDescription(description);
        task.setVdc(vdc);
        return taskManager.createTask(task);
    }

}
