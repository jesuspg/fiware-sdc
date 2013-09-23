package com.telefonica.euro_iaas.sdc.manager;
/**
 * Defines a factory object to get the concrete instaces of
 * ApplicationInstanceManager class depending of some variables.
 *
 * @author Sergio Arroyo
 *
 */
public interface ApplicationInstanceManagerFactory {

    /**
     * Get the manager according to application type.
     * @param type the type
     * @return the manager.
     */
    ApplicationInstanceManager getInstance(String type);

    /**
     * Get the default manager.
     * @return the manager.
     */
    ApplicationInstanceManager getInstance();

}
