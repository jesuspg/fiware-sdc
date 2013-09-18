package com.telefonica.euro_iaas.sdc.manager;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.exception.UnableToStartOSException;
import com.telefonica.euro_iaas.sdc.model.OS;
import com.telefonica.euro_iaas.sdc.model.OSInstance;
import com.telefonica.euro_iaas.sdc.model.dto.VM;
/**
 * Provides the needed methods to clone, get running and freeze a image of
 * an OS.
 * @author Sergio Arroyo
 *
 */
public interface OSInstanceManager {

    /**
     * Get an instance of a OS running on the selected vm.
     * @param so the operative system
     * @param vm
     * @throws UnableToStartOSException if something happens
     *
     * @return the OS Instance running on a machine.
     *
     */
    OSInstance startAndRunning(OS so, VM vm) throws UnableToStartOSException;

    /**
     * Generate an image file with the actual state of OS.
     * @param instance
     * @return
     */
    OSInstance freeze(OSInstance instance);

    /**
     * Find the OSInstance using the given id.
     * @param id the osInstance identifier
     * @return the osInstance
     * @throws EntityNotFoundException if the os instance does not exists
     */
    OSInstance load(Long id) throws EntityNotFoundException;

    /**
     * Retrieve all OSInstance created in the system.
     * @return the created OS instances.
     */
    List<OSInstance> findAll();

}
