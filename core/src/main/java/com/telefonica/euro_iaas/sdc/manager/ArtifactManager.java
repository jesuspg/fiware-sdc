package com.telefonica.euro_iaas.sdc.manager;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.exception.FSMViolationException;
import com.telefonica.euro_iaas.sdc.exception.NodeExecutionException;
import com.telefonica.euro_iaas.sdc.exception.NotUniqueResultException;
import com.telefonica.euro_iaas.sdc.model.Artifact;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ArtifactSearchCriteria;

/**
 * Defines the operations the system shall be able to do with Products
 * 
 * @author Sergio Arroyo
 */
public interface ArtifactManager {

    /**
     * Deploy an artefact in a previously installed product.
     * 
     * @param productInstance
     *            the candidate to deploy the artefact
     * @param artefacts
     *            the artefacts
     * @throws NodeExecutionException
     *             if any error happen during the uninstallation in node
     * @throws ApplicationInstalledException
     *             if the product has some applications which depend on it
     * @throws FSMViolationException
     *             if try to make a forbidden transition
     */

    ProductInstance deployArtifact(ProductInstance productInstance, Artifact artifact) throws NodeExecutionException,
            FSMViolationException;

    /**
     * UnDeploy an artefact in a previously installed product.
     * 
     * @param productInstance
     *            the candidate to undeploy the artefact
     * @param artifactName
     *            the artefact to be undeployed
     * @throws NodeExecutionException
     *             if any error happen during the uninstallation in node
     * @throws ApplicationInstalledException
     *             if the product has some applications which depend on it
     * @throws FSMViolationException
     *             if try to make a forbidden transition
     */

    ProductInstance undeployArtifact(ProductInstance productInstance, String artifactName)
            throws NodeExecutionException, FSMViolationException;

    /**
     * Find the ProductInstance using the given id.
     * 
     * @param vdc
     *            the vdc
     * @param id
     *            the productInstance identifier
     * @return the productInstance
     * @throws EntityNotFoundException
     *             if the product instance does not exists
     */
    Artifact load(String vdc, String productInstance, String name) throws EntityNotFoundException;

    /**
     * Find the Artifact that match with the given criteria.
     * 
     * @param criteria
     *            the search criteria
     * @return the productInstance
     * @throws EntityNotFoundException
     *             if the product instance does not exists
     * @throws NotUniqueResultException
     *             if there are more than a product that match with the given criteria
     */
    Artifact loadByCriteria(ArtifactSearchCriteria criteria) throws EntityNotFoundException, NotUniqueResultException;

    /**
     * Retrieve all Artifact created in the system.
     * 
     * @return the existent product instances.
     */
    List<Artifact> findAll();

    /**
     * Find the artifact that match with the given criteria.
     * 
     * @param criteria
     *            the search criteria
     * @return the list of elements that match with the criteria.
     */
    List<Artifact> findByCriteria(ArtifactSearchCriteria criteria);

}
