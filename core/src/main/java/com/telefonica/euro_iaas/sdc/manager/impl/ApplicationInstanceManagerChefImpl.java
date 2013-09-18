package com.telefonica.euro_iaas.sdc.manager.impl;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.telefonica.euro_iaas.commons.dao.AlreadyExistsEntityException;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.sdc.dao.ApplicationInstanceDao;
import com.telefonica.euro_iaas.sdc.exception.CanNotCallChefException;
import com.telefonica.euro_iaas.sdc.exception.ChefExecutionException;
import com.telefonica.euro_iaas.sdc.exception.NotTransitableException;
import com.telefonica.euro_iaas.sdc.exception.SdcRuntimeException;
import com.telefonica.euro_iaas.sdc.manager.ApplicationInstanceManager;
import com.telefonica.euro_iaas.sdc.model.ApplicationInstance;
import com.telefonica.euro_iaas.sdc.model.ApplicationRelease;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.dto.VM;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ApplicationInstanceSearchCriteria;

/**
 * Chef based ApplicationInstanceManager implementation.
 *
 * @author Sergio Arroyo
 *
 */
public class ApplicationInstanceManagerChefImpl extends
        BaseInstallableInstanceManager implements ApplicationInstanceManager {

    private ApplicationInstanceDao applicationInstanceDao;
    private static Logger LOGGER = Logger.getLogger("ApplicationManagerChefImpl");

    /**
     * {@inheritDoc}
     */
    @Override
    public ApplicationInstance install(VM vm, List<ProductInstance> products,
            ApplicationRelease application, List<Attribute> configuration)
    throws ChefExecutionException {
        try {
            ApplicationInstance applicationInstance = new ApplicationInstance(
                    application, products, Status.INSTALLED);
            String recipe = recipeNamingGenerator
                    .getInstallRecipe(applicationInstance);
            callChef(application.getApplication().getName(), recipe, vm,
                    configuration);
            return applicationInstanceDao.create(applicationInstance);

        } catch (InvalidEntityException e) {
            throw new SdcRuntimeException(
                    "Can not create application instance", e);
        } catch (AlreadyExistsEntityException e) {
            throw new SdcRuntimeException(
                    "Can not create application instance", e);
        } catch (CanNotCallChefException e) {
            throw new SdcRuntimeException("Can not exectue the script", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void uninstall(ApplicationInstance applicationInstance)
            throws ChefExecutionException {
        try {
            String recipe = recipeNamingGenerator
                    .getUninstallRecipe(applicationInstance);

            VM vm = applicationInstance.getProducts().get(0).getVM();

            callChef(recipe, vm);
            applicationInstance.setStatus(Status.UNINSTALLED);
            applicationInstanceDao.update(applicationInstance);

        } catch (InvalidEntityException e) {
            throw new SdcRuntimeException(
                    "Can not create application instance", e);
        } catch (CanNotCallChefException e) {
            throw new SdcRuntimeException("Can not exectue the script", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ApplicationInstance configure(
            ApplicationInstance applicationInstance,
            List<Attribute> configuration) throws ChefExecutionException {
        String recipe = recipeNamingGenerator
                .getInstallRecipe(applicationInstance);
        // the application shall be installed over, at least, one product
        VM vm = applicationInstance.getProducts().get(0).getVM();

        try {
            callChef(applicationInstance.getApplication().getApplication()
                    .getType(), recipe, vm, configuration);
        } catch (CanNotCallChefException e) {
            throw new SdcRuntimeException(e);
        }

        return applicationInstance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ApplicationInstance upgrade(ApplicationInstance applicationInstance,
            ApplicationRelease newRelease) throws ChefExecutionException,
            NotTransitableException {

        List<ApplicationRelease> releases = applicationInstance
                .getApplication().getTransitableReleases();

        if (!releases.contains(newRelease)) {
            throw new NotTransitableException();
        }

        try {
            VM vm = applicationInstance.getProducts().iterator().next().getVM();

            String backupRecipe = recipeNamingGenerator
                    .getBackupRecipe(applicationInstance);
            callChef(backupRecipe, vm);

            String uninstallRecipe = recipeNamingGenerator
                    .getUninstallRecipe(applicationInstance);
            callChef(uninstallRecipe, vm);

            applicationInstance.setApplication(newRelease);

            String installRecipe = recipeNamingGenerator
                    .getInstallRecipe(applicationInstance);
            callChef(installRecipe, vm);

            String restoreRecipe = recipeNamingGenerator
                    .getRestoreRecipe(applicationInstance);
            callChef(restoreRecipe, vm);

            return applicationInstanceDao.update(applicationInstance);
        } catch (CanNotCallChefException sce) {
            LOGGER.log(Level.SEVERE, sce.getMessage());
            throw new SdcRuntimeException(sce);
        } catch (InvalidEntityException e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
            throw new SdcRuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ApplicationInstance load(Long id) throws EntityNotFoundException {
        return applicationInstanceDao.load(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ApplicationInstance> findAll() {
        return applicationInstanceDao.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ApplicationInstance> findByCriteria(
            ApplicationInstanceSearchCriteria criteria) {
        return applicationInstanceDao.findByCriteria(criteria);
    }

    // ///////////// I.O.C ////////////
    /**
     * @param applicationInstanceDao
     *            the applicationInstanceDao to set
     */
    public void setApplicationInstanceDao(
            ApplicationInstanceDao applicationInstanceDao) {
        this.applicationInstanceDao = applicationInstanceDao;
    }
}
