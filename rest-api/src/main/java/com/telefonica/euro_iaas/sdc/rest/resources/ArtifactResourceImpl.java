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
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.telefonica.euro_iaas.sdc.manager.async.ArtifactAsyncManager;
import com.telefonica.euro_iaas.sdc.manager.async.ProductInstanceAsyncManager;
import com.telefonica.euro_iaas.sdc.manager.async.TaskManager;
import com.telefonica.euro_iaas.sdc.model.Artifact;
import com.telefonica.euro_iaas.sdc.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.Task;
import com.telefonica.euro_iaas.sdc.model.Task.TaskStates;
import com.telefonica.euro_iaas.sdc.model.dto.ArtifactDto;
import com.telefonica.euro_iaas.sdc.model.dto.PaasManagerUser;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ArtifactSearchCriteria;
import com.telefonica.euro_iaas.sdc.rest.auth.OpenStackAuthenticationProvider;
import com.telefonica.fiware.commons.dao.EntityNotFoundException;

/**
 * Default ProductInstanceResource implementation.
 * 
 * @author Henar Mu�oz
 */
@Path("/vdc/{vdc}/productInstance/{productInstance}/ac")
@Component
@Scope("request")
public class ArtifactResourceImpl implements ArtifactResource {

    private ArtifactAsyncManager artifactAsyncManager;
    private ProductInstanceAsyncManager productInstanceAsyncManager;
    private TaskManager taskManager;

    private static Logger log = LoggerFactory.getLogger(ArtifactResourceImpl.class);

    public Task install(String vdc, String productIntanceName, ArtifactDto artifactDto, String callback) {
        log.debug("Install artifact " + artifactDto.getName() + " in product " + productIntanceName + " vdc " + vdc);
        ProductInstance productInstance = getProductInstance(vdc, productIntanceName);

        Task task = createTask(MessageFormat.format("Deploy artifact in  product {0} in  VM {1}{2}", productInstance
                .getProductRelease().getProduct().getName(), productInstance.getVm().getHostname(), productInstance
                .getVm().getDomain()), vdc);
        Artifact artifact = new Artifact(artifactDto.getName(), productInstance.getVdc(), productInstance,
                artifactDto.getAttributes());
        artifactAsyncManager.deployArtifact(productInstance, artifact, getToken(), task, callback);

        log.debug("Task id " + task.getId() + " for Install artifact " + artifactDto.getName() + " in product "
                + productIntanceName + " vdc " + vdc);

        return task;
    }

    /**
     * {@inheritDoc}
     */

    public Task uninstall(String vdc, String productInstanceName, String artifactName, String callback) {
        log.debug("Uninstall artifact " + artifactName + " in product " + productInstanceName + " vdc " + vdc);
        ProductInstance productInstance = getProductInstance(vdc, productInstanceName);
        Task task = createTask(MessageFormat.format("Undeploying artifact in  product {0} in  VM {1}{2}",
                productInstance.getProductRelease().getProduct().getName(), productInstance.getVm().getHostname(),
                productInstance.getVm().getDomain()), vdc);
        artifactAsyncManager.undeployArtifact(productInstance, artifactName, getToken(), task, callback);

        log.debug("Task id " + task.getId() + " for  Uninstall artifact " + artifactName + " in product "
                + productInstanceName + " vdc " + vdc);

        return task;
    }

    public Artifact load(String vdc, String productInstance, String name) {
        try {
            return artifactAsyncManager.load(vdc, productInstance, name);
        } catch (EntityNotFoundException e) {
            throw new WebApplicationException(e, 404);
        }
    }

    /**
     * {@inheritDoc}
     */

    public List<ArtifactDto> findAll(Integer page, Integer pageSize, String orderBy, String orderType,
            List<Status> status, String vdc, String productInstance) {

        ArtifactSearchCriteria criteria = new ArtifactSearchCriteria();

        try {
            ProductInstance prodInstance = productInstanceAsyncManager.load(vdc, productInstance);
            criteria.setProductInstance(prodInstance);
            criteria.setVdc(vdc);
        } catch (EntityNotFoundException e) {
            throw new WebApplicationException(e, 404);
        }

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

        List<Artifact> artifact = artifactAsyncManager.findByCriteria(criteria);

        List<ArtifactDto> lArtifactDto = new ArrayList<ArtifactDto>();
        for (int i = 0; i < artifact.size(); i++) {
            ArtifactDto artifactDto = new ArtifactDto();

            if (artifact.get(i).getName() != null)
                artifactDto.setName(artifact.get(i).getName());

            if (artifact.get(i).getAttributes() != null)
                artifactDto.setAttributes(artifact.get(i).getAttributes());

            lArtifactDto.add(artifactDto);

        }
        return lArtifactDto;

    }

    private Task createTask(String description, String vdc) {
        Task task = new Task(TaskStates.RUNNING);
        task.setDescription(description);
        task.setVdc(vdc);
        return taskManager.createTask(task);
    }

    public ProductInstance getProductInstance(String vdc, String productInstanceName) {
        try {
            return productInstanceAsyncManager.load(vdc, productInstanceName);
        } catch (EntityNotFoundException e) {
            throw new WebApplicationException(e, 404);
        }
    }

    public void setTaskManager(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    public void setProductInstanceAsyncManager(ProductInstanceAsyncManager productInstanceAsyncManager) {
        this.productInstanceAsyncManager = productInstanceAsyncManager;
    }

    public void setArtifactAsyncManager(ArtifactAsyncManager artifactAsyncManager) {
        this.artifactAsyncManager = artifactAsyncManager;
    }

    public String getToken() {
        PaasManagerUser user = OpenStackAuthenticationProvider.getCredentials();
        if (user == null) {
            return "";
        } else {
            return user.getToken();
        }

    }

}
