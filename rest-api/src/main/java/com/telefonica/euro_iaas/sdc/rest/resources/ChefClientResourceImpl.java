/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
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
