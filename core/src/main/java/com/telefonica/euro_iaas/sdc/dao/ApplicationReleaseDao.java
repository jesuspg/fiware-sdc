/**
 *   (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights
 *   Reserved.
 * 
 *   The copyright to the software program(s) is property of Telefonica I+D.
 *   The program(s) may be used and or copied only with the express written
 *   consent of Telefonica I+D or in accordance with the terms and conditions
 *   stipulated in the agreement/contract under which the program(s) have
 *   been supplied.
 */

package com.telefonica.euro_iaas.sdc.dao;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.BaseDAO;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.model.Application;
import com.telefonica.euro_iaas.sdc.model.ApplicationRelease;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ApplicationReleaseSearchCriteria;

/**
 * Defines the methods needed to persist ApplicationRelease objects.
 * 
 * @author Sergio Arroyo.
 */
public interface ApplicationReleaseDao extends BaseDAO<ApplicationRelease, Long> {
    /**
     * Find the application releases that match with the given criteria.
     * 
     * @param criteria
     *            the search criteria
     * @return the list of elements that match with the criteria.
     */
    List<ApplicationRelease> findByCriteria(ApplicationReleaseSearchCriteria criteria);

    /**
     * Find the application release that match with the given criteria.
     * 
     * @param application
     *            the application
     * @param version
     *            the release version
     * @return the element that match with the criteria.
     * @throws EntityNotFoundException
     *             if there is no application with the given version
     */
    ApplicationRelease load(Application application, String version) throws EntityNotFoundException;

}
