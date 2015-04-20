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

package com.telefonica.euro_iaas.sdc.manager.async;

import java.util.List;

import com.telefonica.fiware.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.model.Artifact;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.Task;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ArtifactSearchCriteria;

/**
 * Defines the interface to work with async requests.
 * 
 * @author Sergio Arroyo
 */
public interface ArtifactAsyncManager {

    void deployArtifact(ProductInstance product, Artifact artifact, String token, Task task, String callback);

    /**
     * Undeploy an artefact in the product
     * 
     * @param productInstance
     *            the installed product to configure
     * @param artifact
     *            the artefact to be undeploy.
     * @param task
     *            the task which contains the information about the async execution
     * @param callback
     *            if not empty, contains the url where the result of the
     */
    void undeployArtifact(ProductInstance product, String artifactName, String token, Task task, String callback);

    Artifact load(String vdc, String productInstance, String name) throws EntityNotFoundException;

    List<Artifact> findByCriteria(ArtifactSearchCriteria criteria);

}
