package com.telefonica.euro_iaas.sdc.manager.impl;

import java.text.MessageFormat;
import java.util.List;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.exception.SdcRuntimeException;
import com.telefonica.euro_iaas.sdc.exception.ShellCommandException;
import com.telefonica.euro_iaas.sdc.manager.ApplicationInstanceManager;
import com.telefonica.euro_iaas.sdc.model.Application;
import com.telefonica.euro_iaas.sdc.model.ApplicationInstance;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.dto.VM;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ApplicationInstanceSearchCriteria;
import com.telefonica.euro_iaas.sdc.util.AbstractShellCommand;
import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.*;

/**
 * Decorator for ApplicationInstanceManager in charge to do the concrete actions
 * for WAR applications.
 *
 * @author Sergio Arroyo
 *
 */
public class ApplicationInstanceManagerWarDecoratorImpl
    extends AbstractShellCommand implements ApplicationInstanceManager{

    private ApplicationInstanceManager applicationInstanceManager;
    /**
     * {@inheritDoc}
     */
    @Override
    public List<ApplicationInstance> findAll() {
        return applicationInstanceManager.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ApplicationInstance> findByCriteria(
            ApplicationInstanceSearchCriteria criteria) {
        return applicationInstanceManager.findByCriteria(criteria);
    }

    /**
     * {@inheritDoc}
     * Also copy the needed files to install the application from server to
     * candidate node.
     */
    @Override
    public ApplicationInstance install(VM vm, List<ProductInstance> products,
            Application application) {
        //prepare the installation
        String fromFolder =  MessageFormat.format(
                propertiesProvider.getProperty(DEFAULT_APP_FILES_SOURCE_FOLDER),
                application.getType(), application.getName());
        String toFolder =  MessageFormat.format(
                propertiesProvider.getProperty(DEFAULT_APP_FILES_DESTINATION_FOLDER),
                application.getType(), application.getName());
        String copyFilesCommand = MessageFormat.format(propertiesProvider.getProperty(
                COPY_APP_FILES_FROM_SERVER_TO_NODE),
                vm.getExecuteChefConectionUrl(), toFolder, fromFolder);
        try {
            executeCommand(copyFilesCommand);
        } catch (ShellCommandException e) {
            throw new SdcRuntimeException("Can not copy the files from server"
                    + " to node to install the application", e);
        }
        return applicationInstanceManager.install(vm, products, application);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ApplicationInstance load(Long id) throws EntityNotFoundException {
        return applicationInstanceManager.load(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void uninstall(ApplicationInstance applicationInstance) {
        applicationInstanceManager.uninstall(applicationInstance);

    }

    ////////////I.O.C. /////////////

    /**
     * @param aplApplicationInstanceManager the aplApplicationInstanceManager to set
     */
    public void setApplicationInstanceManager(
            ApplicationInstanceManager applicationInstanceManager) {
        this.applicationInstanceManager = applicationInstanceManager;
    }

}
