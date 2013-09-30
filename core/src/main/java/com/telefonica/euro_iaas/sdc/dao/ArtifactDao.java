package com.telefonica.euro_iaas.sdc.dao;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.BaseDAO;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.model.Artifact;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ArtifactSearchCriteria;

/**
 * Defines the methods needed to persist Application objects.
 * 
 * @author Sergio Arroyo.
 * @version $Id: $
 */
public interface ArtifactDao extends BaseDAO<Artifact, String> {

    Artifact load(String name) throws EntityNotFoundException;

    /**
     * Find by criteria
     * 
     * @param criteria
     *            the search criteria (containing pagination info, and some fields criteria).
     * @return the elements that match with the search criteria
     */
    List<Artifact> findByCriteria(ArtifactSearchCriteria criteria);

}
