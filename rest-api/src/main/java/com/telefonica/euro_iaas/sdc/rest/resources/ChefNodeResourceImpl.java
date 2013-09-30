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

package com.telefonica.euro_iaas.sdc.rest.resources;

import java.text.MessageFormat;
import javax.ws.rs.Path;

import com.sun.jersey.api.core.InjectParam;
import com.telefonica.euro_iaas.sdc.exception.NodeExecutionException;
import com.telefonica.euro_iaas.sdc.manager.async.ChefNodeAsyncManager;
import com.telefonica.euro_iaas.sdc.manager.async.TaskManager;
import com.telefonica.euro_iaas.sdc.model.Task;
import com.telefonica.euro_iaas.sdc.model.Task.TaskStates;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

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
