/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.sdc.manager.async;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
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

    void deployArtifact(ProductInstance product, Artifact artifact, Task task, String callback);

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
    void undeployArtifact(ProductInstance product, String artifactName, Task task, String callback);

    Artifact load(String vdc, String productInstance, String name) throws EntityNotFoundException;

    List<Artifact> findByCriteria(ArtifactSearchCriteria criteria);

}
