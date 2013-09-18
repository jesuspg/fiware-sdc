package com.telefonica.euro_iaas.sdc.manager.impl;

import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.CLONE_IMAGE_SCRIPT;
import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.FREEZE_IMAGE_SCRIPT;
import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.WAIT_FOR_RUNNING_SCRIPT;
import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.*;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.telefonica.euro_iaas.commons.dao.AlreadyExistsEntityException;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.sdc.dao.OSInstanceDao;
import com.telefonica.euro_iaas.sdc.exception.SdcRuntimeException;
import com.telefonica.euro_iaas.sdc.exception.ShellCommandException;
import com.telefonica.euro_iaas.sdc.exception.UnableToStartOSException;
import com.telefonica.euro_iaas.sdc.manager.OSInstanceManager;
import com.telefonica.euro_iaas.sdc.model.OS;
import com.telefonica.euro_iaas.sdc.model.OSInstance;
import com.telefonica.euro_iaas.sdc.model.OSInstance.Status;
import com.telefonica.euro_iaas.sdc.model.dto.VM;
import com.telefonica.euro_iaas.sdc.util.AbstractShellCommand;
import com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider;

/**
 * Implements OSManager using the Crisitan tools implementation (it means,
 * calling a python based scripts).
 *
 * @author Sergio Arroyo
 *
 */
public class OSInstanceManagerCristianToolsImpl extends AbstractShellCommand implements
        OSInstanceManager {

    private SystemPropertiesProvider propertiesProvider;
    private OSInstanceDao osInstanceDao;

    private static final Logger LOGGER = Logger
            .getLogger("SODeployerCrisitanToolsImpl");

    /**
     * {@inheritDoc}
     */
    @Override
    public OSInstance startAndRunning(OS so, VM vm)
            throws UnableToStartOSException {
        try {
            OSInstance instance = new OSInstance(so, Status.INSTALLING, vm);
            // invoque the script:
            String command = MessageFormat.format(propertiesProvider
                    .getProperty(CLONE_IMAGE_SCRIPT),
                    instance.getSo().getName(), instance.getVM()
                    .getHostname());
            LOGGER.info("starting OS in vm: " + vm);
            executeCommand(command);
            waitOK(instance);
            instance.setStatus(Status.RUNNING);
            return osInstanceDao.create(instance);
        } catch (ShellCommandException e) {
            LOGGER.log(Level.SEVERE, "Can not get the OS running due to "
                    + e.getMessage());
            throw new UnableToStartOSException();
        } catch (InterruptedException e) {
            LOGGER.log(Level.SEVERE, "Can not get the OS running due to "
                    + e.getMessage());
            throw new SdcRuntimeException(e);
        } catch (InvalidEntityException e) {
            LOGGER.log(Level.SEVERE, "Can not store the OS running due to "
                    + e.getMessage());
            throw new SdcRuntimeException(e);
        } catch (AlreadyExistsEntityException e) {
            LOGGER.log(Level.SEVERE, "Can not store the OS running due to "
                    + e.getMessage());
            throw new SdcRuntimeException(e);
        }
    }

    /**
     * Wait during the OS is getting running.
     * @param soInstance
     * @throws InterruptedException
     * @throws IOException
     * @throws ShellCommandException
     */
    private void waitOK(OSInstance soInstance) throws InterruptedException {
        //Wait 3 minutes to complete cloning and booting
        Thread.sleep(propertiesProvider.getIntProperty(TIME_WAITING_FOR_RUNNING));
        Boolean running = false;
        while (!running) {
            String command = MessageFormat.format(propertiesProvider
                    .getProperty(WAIT_FOR_RUNNING_SCRIPT), soInstance.getVM()
                    .getHostname());
            try {
                executeCommand(command);
                running = true;
            } catch (ShellCommandException e) {
                // still waiting
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OSInstance freeze(OSInstance instance) {
        try {
            String command = MessageFormat.format(
                    propertiesProvider.getProperty(FREEZE_IMAGE_SCRIPT),
                    instance.getVM().getHostname());
            executeCommand(command);
            instance.setStatus(Status.STOPPED);
            instance.setImageFileLocation(MessageFormat.format(
                    propertiesProvider.getProperty(
                            WEBDAV_BASE_URL),
                            instance.getVM().getHostname()));
            return osInstanceDao.update(instance);
        } catch (ShellCommandException e) {
            LOGGER.log(Level.SEVERE, "Can not freeze the OS image due to"
                    + e.getMessage());
            throw new SdcRuntimeException(e);
        } catch (InvalidEntityException e) {
            LOGGER.log(Level.SEVERE, "Problem found when trying to persist the "
                    + "changes in OSInstance: " + e.getMessage());
            throw new SdcRuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OSInstance load(Long id) throws EntityNotFoundException {
        return osInstanceDao.load(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<OSInstance> findAll() {
        return osInstanceDao.findAll();
    }


    //////////////// I.O.C ////////////////
    /**
     * @param propertiesProvider the propertiesProvider to set
     */
    public void setPropertiesProvider(
            SystemPropertiesProvider propertiesProvider) {
        this.propertiesProvider = propertiesProvider;
    }

    /**
     * @param osInstanceDao the osInstanceDao to set
     */
    public void setOsInstanceDao(OSInstanceDao osInstanceDao) {
        this.osInstanceDao = osInstanceDao;
    }

}
