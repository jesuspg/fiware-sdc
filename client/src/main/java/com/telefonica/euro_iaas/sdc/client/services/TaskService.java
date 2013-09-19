package com.telefonica.euro_iaas.sdc.client.services;

import java.util.Date;
import java.util.List;

import com.telefonica.euro_iaas.sdc.client.exception.MaxTimeWaitingExceedException;
import com.telefonica.euro_iaas.sdc.model.Task;
import com.telefonica.euro_iaas.sdc.model.Task.TaskStates;

/**
 * Provides the way to find some tasks stored in the system.
 * 
 * @author Sergio Arroyo
 * 
 */
public interface TaskService {

	/**
	 * Find a task for a given id.
	 * 
	 * @param vdc
	 *            the vdc
	 * @param id
	 *            the task's id
	 * @return the task
	 */
	Task load(String vdc, Long id);

	/**
	 * Find a task for a given id.
	 * 
	 * @param url
	 *            the url where the task is
	 * @return the task
	 */
	Task load(String url);

	/**
	 * Retrieve the tasks that match with the given criteria
	 * 
	 * @param page
	 *            for pagination is 0 based number(<i>nullable</i>)
	 * @param pageSize
	 *            for pagination, the number of items retrieved in a query
	 *            (<i>nullable</i>)
	 * @param orderBy
	 *            the file to order the search (id by default <i>nullable</i>)
	 * @param orderType
	 *            defines if the order is ascending or descending (asc by
	 *            default <i>nullable</i>)
	 * @param states
	 *            the list containing the task states
	 * @param resource
	 *            the resource url the task is related to
	 * @param owner
	 *            the task's owner url
	 * @param fromDate
	 *            the initial date where the task was created (included)
	 * @param toDate
	 *            the final date where the task was created (included)
	 * @return the tasks that match with the criteria.
	 */
	List<Task> findAll(Integer page, Integer pageSize, String orderBy,
			String orderType, List<TaskStates> states, String resource,
			String owner, Date fromDate, Date toDate, String vdc);

	/**
	 * Retrieve all task for a given product.
	 * 
	 * @param vdc
	 * @param productName
	 * @return
	 */
	List<Task> findAllByProduct(String vdc, String productName);

	/**
	 * Retrieve all task for a given application.
	 * 
	 * @param vdc
	 * @param applicationId
	 * @return
	 */
	List<Task> findAllByApplication(String vdc, Long applicationId);

	/**
	 * Wait while the task is running.
	 * 
	 * @param url
	 *            the url where the task is
	 * @throws MaxTimeWaitingExceedException
	 *             if the waiting takes more time than expected
	 */
	Task waitForTask(String url) throws MaxTimeWaitingExceedException;
}
