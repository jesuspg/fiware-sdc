package com.telefonica.euro_iaas.sdc.manager.impl;

import com.telefonica.euro_iaas.sdc.manager.ApplicationInstanceManager;
import com.telefonica.euro_iaas.sdc.manager.ApplicationInstanceManagerFactory;

/**
 * Default ApplicationInstanceManagerFactory implementation.
 *
 * @author Sergio Arroyo
 *
 */
public class ApplicationInstanceManagerFactoryImpl
    implements ApplicationInstanceManagerFactory {

    private ApplicationInstanceManager defaultManager;
    private ApplicationInstanceManager warManager;

    /**
     * {@inheritDoc}
     */
    @Override
    public ApplicationInstanceManager getInstance(String type) {
        ApplicationInstanceManager manager;
        if (type.equals("war")) {
            manager = warManager;
        } else {
            manager = defaultManager;
        }
        return manager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ApplicationInstanceManager getInstance() {
        return defaultManager;
    }

    ///////////I.O.C./////////
    /**
     * @param defaultManager the defaultManager to set
     */
    public void setDefaultManager(ApplicationInstanceManager defaultManager) {
        this.defaultManager = defaultManager;
    }

    /**
     * @param warManager the warManager to set
     */
    public void setWarManager(ApplicationInstanceManager warManager) {
        this.warManager = warManager;
    }

}
