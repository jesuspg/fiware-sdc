package com.telefonica.euro_iaas.sdc.client.services.impl;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import com.telefonica.euro_iaas.sdc.client.ClientConstants;
import com.telefonica.euro_iaas.sdc.client.exception.MaxTimeWaitingExceedException;
import com.telefonica.euro_iaas.sdc.client.model.Tasks;
import com.telefonica.euro_iaas.sdc.client.services.TaskService;
import com.telefonica.euro_iaas.sdc.model.Task;
import com.telefonica.euro_iaas.sdc.model.Task.TaskStates;

/**
 * Default TaskService implementation.
 *
 * @author Sergio Arroyo
 *
 */
public class TaskServiceImpl extends AbstractBaseService implements TaskService {

    private Long waitingPeriod = 5000l; // every 5*iter seconds try again
    private Long maxWaiting = 600000l; // max 10 minutes waiting

    /**
     * @param client
     * @param baseHost
     * @param type
     */
    public TaskServiceImpl(Client client, String baseHost, String type) {
        setClient(client);
        setBaseHost(baseHost);
        setType(type);
    }

    /**
     * @param client
     * @param baseHost
     * @param type
     * @param waitingPeriod
     * @param maxWaiting
     */
    public TaskServiceImpl(Client client, String baseHost, String type,
            Long waitingPeriod, Long maxWaiting) {
        setClient(client);
        setBaseHost(baseHost);
        setType(type);
        this.waitingPeriod = waitingPeriod;
        this.maxWaiting = maxWaiting;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Task load(String vdc, Long id) {
        String url = getBaseHost()
                + MessageFormat.format(ClientConstants.TASK_PATH, vdc, id);
        return this.load(url);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Task load(String url) {
        WebResource wr = getClient().resource(url);
        return wr.accept(getType()).get(Task.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Task> findAll(Integer page, Integer pageSize, String orderBy,
            String orderType, List<TaskStates> states, String resource,
            String owner, Date fromDate, Date toDate, String vdc) {
        String url = getBaseHost()
                + MessageFormat.format(ClientConstants.TASK_PATH, vdc);
        WebResource wr = getClient().resource(url);
        MultivaluedMap<String, String> searchParams = new MultivaluedMapImpl();
        searchParams.add("page", page.toString());
        searchParams.add("pageSize", pageSize.toString());
        searchParams.add("orderBy", orderBy);
        searchParams.add("orderType", orderType);
        searchParams.add("status", resource);
        searchParams.add("vdc", vdc);
        searchParams.add("fromDate", fromDate.toString());
        searchParams.add("toDate", toDate.toString());
        searchParams.add("owner", owner);
        searchParams.add("resource", resource);
        for (TaskStates state : states) {
            searchParams.add("states", state.toString());
        }
        return wr.queryParams(searchParams).accept(getType()).get(Tasks.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Task waitForTask(String url) throws MaxTimeWaitingExceedException {
        try {
            Task task = this.load(url);
            Integer count = 1;
            while (task.getStatus() == TaskStates.RUNNING) {
                Thread.sleep(count * waitingPeriod);
                count = count + 1;
                task = this.load(url);
                if (isTimeExceed(task.getStartTime())) {
                    throw new MaxTimeWaitingExceedException();
                }
            }
            return task;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /*
     * Calculates the time passed since task started and check if is biggest
     * than the maximum
     */
    private Boolean isTimeExceed(Date startedDate) {
        return (new Date().getTime() - startedDate.getTime()) > maxWaiting;
    }

}
