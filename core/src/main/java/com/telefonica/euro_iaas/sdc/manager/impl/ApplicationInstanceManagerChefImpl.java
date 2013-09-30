package com.telefonica.euro_iaas.sdc.manager.impl;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.AlreadyExistsEntityException;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.sdc.dao.ApplicationDao;
import com.telefonica.euro_iaas.sdc.dao.ApplicationInstanceDao;
import com.telefonica.euro_iaas.sdc.exception.AlreadyInstalledException;
import com.telefonica.euro_iaas.sdc.exception.CanNotCallChefException;
import com.telefonica.euro_iaas.sdc.exception.FSMViolationException;
import com.telefonica.euro_iaas.sdc.exception.IncompatibleProductsException;
import com.telefonica.euro_iaas.sdc.exception.NodeExecutionException;
import com.telefonica.euro_iaas.sdc.exception.NotInstalledProductsException;
import com.telefonica.euro_iaas.sdc.exception.NotTransitableException;
import com.telefonica.euro_iaas.sdc.exception.NotUniqueResultException;
import com.telefonica.euro_iaas.sdc.exception.SdcRuntimeException;
import com.telefonica.euro_iaas.sdc.manager.ApplicationInstanceManager;
import com.telefonica.euro_iaas.sdc.model.Application;
import com.telefonica.euro_iaas.sdc.model.ApplicationInstance;
import com.telefonica.euro_iaas.sdc.model.ApplicationRelease;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.EnvironmentInstance;
import com.telefonica.euro_iaas.sdc.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.dto.VM;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ApplicationInstanceSearchCriteria;
import com.telefonica.euro_iaas.sdc.validation.ApplicationInstanceValidator;
import com.xmlsolutions.annotation.Requirement;
import com.xmlsolutions.annotation.UseCase;

/**
 * Chef based ApplicationInstanceManager implementation.
 * 
 * @author Sergio Arroyo, Jesus M. Movilla
 */
@UseCase(traceTo = "UC_002", status = "implemented")
@Requirement(traceTo = "BR002", status = "implemented")
public class ApplicationInstanceManagerChefImpl extends BaseInstallableInstanceManager implements
        ApplicationInstanceManager {

    private ApplicationInstanceValidator validator;
    private ApplicationInstanceDao applicationInstanceDao;
    private ApplicationDao applicationDao;

    /**
     * {@inheritDoc}
     */
    @UseCase(traceTo = "UC_002.1", status = "implemented")
    public ApplicationInstance install(VM vm, String vdc, EnvironmentInstance environmentInstance,
            ApplicationRelease application, List<Attribute> configuration) throws NodeExecutionException,
            IncompatibleProductsException, AlreadyInstalledException, NotInstalledProductsException {
        ApplicationInstance instance = getApplicationToInstall(application, vm, vdc, environmentInstance, configuration);
        Status previousStatus = instance.getStatus();
        try {
            validator.validateInstall(instance);
            instance.setStatus(Status.INSTALLING);
            if (instance.getId() != null) {
                instance = applicationInstanceDao.update(instance);
            } else {
                instance = applicationInstanceDao.create(instance);
            }
            String recipe = recipeNamingGenerator.getInstallRecipe(instance);
            callChef(application.getApplication().getName(), recipe, vm, configuration);
            instance.setStatus(Status.INSTALLED);
            return applicationInstanceDao.update(instance);

        } catch (InvalidEntityException e) {
            throw new SdcRuntimeException("Can not create application instance", e);
        } catch (AlreadyExistsEntityException e) {
            throw new SdcRuntimeException("Can not create application instance", e);
        } catch (CanNotCallChefException e) {
            restoreInstance(previousStatus, instance);
            throw new SdcRuntimeException("Can not exectue the script", e);
        } catch (RuntimeException e) {
            // by default restore the previous state when a runtime is thrown
            restoreInstance(previousStatus, instance);
            throw new SdcRuntimeException(e);
        } catch (NodeExecutionException e) {
            restoreInstance(Status.ERROR, instance);
            throw e;
        }
    }

    /**
     * {@inheritDoc}
     */

    public void uninstall(ApplicationInstance applicationInstance) throws NodeExecutionException, FSMViolationException {
        Status previousStatus = applicationInstance.getStatus();
        try {
            validator.validateUninstall(applicationInstance);
            applicationInstance.setStatus(Status.UNINSTALLING);
            applicationInstance = applicationInstanceDao.update(applicationInstance);

            String recipe = recipeNamingGenerator.getUninstallRecipe(applicationInstance);

            callChef(recipe, applicationInstance.getVm());
            applicationInstance.setStatus(Status.UNINSTALLED);
            applicationInstanceDao.update(applicationInstance);

        } catch (CanNotCallChefException e) {
            restoreInstance(previousStatus, applicationInstance);
            throw new SdcRuntimeException(e);
        } catch (InvalidEntityException e) {
            throw new SdcRuntimeException(e);
        } catch (RuntimeException e) {
            // by default restore the previous state when a runtime is thrown
            restoreInstance(previousStatus, applicationInstance);
            throw new SdcRuntimeException(e);
        } catch (NodeExecutionException e) {
            restoreInstance(Status.ERROR, applicationInstance);
            throw e;
        }
    }

    /**
     * {@inheritDoc}
     */

    public ApplicationInstance configure(ApplicationInstance applicationInstance, List<Attribute> configuration)
            throws NodeExecutionException, FSMViolationException {
        Status previousStatus = applicationInstance.getStatus();

        try {
            validator.validateConfigure(applicationInstance);
            applicationInstance.setStatus(Status.CONFIGURING);
            applicationInstance = applicationInstanceDao.update(applicationInstance);

            VM vm = applicationInstance.getVm();

            String backupRecipe = recipeNamingGenerator.getBackupRecipe(applicationInstance);
            callChef(backupRecipe, vm);

            String uninstallRecipe = recipeNamingGenerator.getUninstallRecipe(applicationInstance);
            callChef(uninstallRecipe, vm);

            Application application = applicationDao.load(applicationInstance.getApplication().getApplication()
                    .getName());
            application.setAttributes(configuration);

            applicationDao.update(application);

            ApplicationRelease applicationRelease = applicationInstance.getApplication();
            applicationRelease.setApplication(application);

            String recipe = recipeNamingGenerator.getInstallRecipe(applicationInstance);
            // the application shall be installed over, at least, one product
            callChef(applicationInstance.getApplication().getApplication().getName(), recipe,
                    applicationInstance.getVm(), configuration);

            String restoreRecipe = recipeNamingGenerator.getRestoreRecipe(applicationInstance);
            callChef(restoreRecipe, vm);

            applicationInstance.setApplication(applicationRelease);
            applicationInstance.setStatus(Status.INSTALLED);
            return applicationInstanceDao.update(applicationInstance);
        } catch (CanNotCallChefException e) {
            restoreInstance(previousStatus, applicationInstance);
            throw new SdcRuntimeException(e);
        } catch (RuntimeException e) { // by runtime restore the previous state
            // restore the status
            restoreInstance(previousStatus, applicationInstance);
            throw new SdcRuntimeException(e);
        } catch (NodeExecutionException e) {
            restoreInstance(Status.ERROR, applicationInstance);
            throw e;
        } catch (InvalidEntityException e) {
            throw new SdcRuntimeException(e);
        } catch (EntityNotFoundException e) {
            throw new SdcRuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @UseCase(traceTo = "UC_002.4", status = "implemented")
    public ApplicationInstance upgrade(ApplicationInstance applicationInstance, ApplicationRelease newRelease)
            throws NodeExecutionException, NotTransitableException, IncompatibleProductsException,
            FSMViolationException {
        Status previousStatus = applicationInstance.getStatus();

        try {
            validator.validateUpdate(applicationInstance, newRelease);
            applicationInstance.setStatus(Status.UPGRADING);
            applicationInstance = applicationInstanceDao.update(applicationInstance);

            VM vm = applicationInstance.getVm();

            String backupRecipe = recipeNamingGenerator.getBackupRecipe(applicationInstance);
            callChef(backupRecipe, vm);

            String uninstallRecipe = recipeNamingGenerator.getUninstallRecipe(applicationInstance);
            callChef(uninstallRecipe, vm);

            applicationInstance.setApplication(newRelease);
            applicationInstance.setStatus(Status.INSTALLED);

            String installRecipe = recipeNamingGenerator.getInstallRecipe(applicationInstance);
            callChef(installRecipe, vm);

            String restoreRecipe = recipeNamingGenerator.getRestoreRecipe(applicationInstance);
            callChef(restoreRecipe, vm);

            return applicationInstanceDao.update(applicationInstance);
        } catch (CanNotCallChefException sce) {
            restoreInstance(previousStatus, applicationInstance);
            throw new SdcRuntimeException(sce);
        } catch (InvalidEntityException e) {
            // don't restore the status because this exception is storing the
            // product in database so it will fail anyway
            throw new SdcRuntimeException(e);
        } catch (RuntimeException e) { // by runtime restore the previous state
            // restore the status
            restoreInstance(previousStatus, applicationInstance);
            throw new SdcRuntimeException(e);
        } catch (NodeExecutionException e) {
            restoreInstance(Status.ERROR, applicationInstance);
            throw e;
        }
    }

    /**
     * {@inheritDoc}
     */

    public ApplicationInstance load(Long id) throws EntityNotFoundException {
        return applicationInstanceDao.load(id);
    }

    /**
     * {@inheritDoc}
     */

    public ApplicationInstance loadByCriteria(ApplicationInstanceSearchCriteria criteria)
            throws EntityNotFoundException, NotUniqueResultException {
        List<ApplicationInstance> application = applicationInstanceDao.findByCriteria(criteria);
        if (application.size() == 0) {
            throw new EntityNotFoundException(ProductInstance.class, "searchCriteria", criteria.toString());
        } else if (application.size() > 1) {
            throw new NotUniqueResultException();
        }
        return application.get(0);
    }

    /**
     * Creates or find the application instance in installation operation.
     * 
     * @param application
     * @param vm
     * @return
     */
    private ApplicationInstance getApplicationToInstall(ApplicationRelease applicationRelease, VM vm, String vdc,
            EnvironmentInstance environmentInstance, List<Attribute> configuration) {
        ApplicationInstance instance;
        try {
            ApplicationInstanceSearchCriteria criteria = new ApplicationInstanceSearchCriteria();
            criteria.setVm(vm);
            criteria.setApplicationName(applicationRelease.getApplication().getName());
            instance = applicationInstanceDao.findUniqueByCriteria(criteria);
            instance.setEnvironmentInstance(environmentInstance);

            Application application;
            try {
                application = applicationDao.load(applicationRelease.getApplication().getName());
            } catch (EntityNotFoundException e) {
                application = new Application(applicationRelease.getApplication().getName(), applicationRelease
                        .getApplication().getDescription(), applicationRelease.getApplication().getType());
            }
            application.setAttributes(configuration);

            applicationRelease.setApplication(application);
            instance.setApplication(applicationRelease);
        } catch (NotUniqueResultException e) {
            instance = new ApplicationInstance(applicationRelease, environmentInstance, Status.UNINSTALLED, vm, vdc);
        }
        return instance;
    }

    /**
     * Go to previous state when a runtime exception is thrown in any method which can change the status of the product
     * instance.
     * 
     * @param previousStatus
     *            the previous status
     * @param instance
     *            the application instance
     * @return the instance.
     */
    private ApplicationInstance restoreInstance(Status previousStatus, ApplicationInstance instance) {
        instance.setStatus(previousStatus);
        try {
            return applicationInstanceDao.update(instance);
        } catch (InvalidEntityException e) {
            throw new SdcRuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     */

    public List<ApplicationInstance> findAll() {
        return applicationInstanceDao.findAll();
    }

    /**
     * {@inheritDoc}
     */

    public List<ApplicationInstance> findByCriteria(ApplicationInstanceSearchCriteria criteria) {
        return applicationInstanceDao.findByCriteria(criteria);
    }

    // ///////////// I.O.C ////////////
    /**
     * @param applicationInstanceDao
     *            the applicationInstanceDao to set
     */
    public void setApplicationInstanceDao(ApplicationInstanceDao applicationInstanceDao) {
        this.applicationInstanceDao = applicationInstanceDao;
    }

    /**
     * @param applicationInstanceDao
     *            the applicationDao to set
     */
    public void setApplicationDao(ApplicationDao applicationDao) {
        this.applicationDao = applicationDao;
    }

    /**
     * @param validator
     *            the validator to set
     */
    public void setValidator(ApplicationInstanceValidator validator) {
        this.validator = validator;
    }

}
