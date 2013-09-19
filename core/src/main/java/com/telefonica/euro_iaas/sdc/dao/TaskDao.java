package com.telefonica.euro_iaas.sdc.dao;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.BaseDAO;
import com.telefonica.euro_iaas.sdc.model.Task;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.TaskSearchCriteria;
/**
 * Default the persistence operations for Task elements.
 * @author Sergio Arroyo
 *
 */
public interface TaskDao extends BaseDAO<Task, Long> {

    List<Task> findByCriteria(TaskSearchCriteria criteria);

}
