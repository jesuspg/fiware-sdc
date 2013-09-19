/**
 * 
 */
package com.telefonica.euro_iaas.sdc.manager.async;

import com.telefonica.euro_iaas.sdc.model.Task;

/**
 * @author jesus.movilla
 *
 */
public interface ChefClientAsyncManager {

	/** 
	 * Delete a ChefClient from ChefServer
	 * @param chefClientName the name of the chefclient to be deleted from chef server
	 * @param task
	 *            the task which contains the information about the async
	 *            execution
	 * @param callback
	 *            if not empty, contains the url where the result of the
	 *            execution will be sent
	 */
	void chefClientDelete(String vdc, String chefClientname, Task task, String callback);
}
