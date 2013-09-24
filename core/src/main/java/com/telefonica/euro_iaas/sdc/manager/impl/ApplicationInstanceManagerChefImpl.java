package com.telefonica.euro_iaas.sdc.manager.impl;

import java.io.File;
import java.util.Date;
import java.util.List;

import com.telefonica.euro_iaas.commons.dao.AlreadyExistsEntityException;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.sdc.dao.ApplicationInstanceDao;
import com.telefonica.euro_iaas.sdc.exception.SdcRuntimeException;
import com.telefonica.euro_iaas.sdc.exception.ShellCommandException;
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
public class ApplicationInstanceManagerChefImpl
    extends BaseInstallableInstanceManager
        implements ApplicationInstanceManager {

    private ApplicationInstanceDao applicationInstanceDao;

    /**
     * {@inheritDoc}
     */
    @Override
    public ApplicationInstance install(VM vm, List<ProductInstance> products,
            ApplicationRelease application) {
        try {
            ApplicationInstance applicationInstance = new ApplicationInstance(
                    application, products, Status.INSTALLED);
            String recipe =
                recipeNamingGenerator.getInstallRecipe(applicationInstance);
            callChef(recipe, vm);
            return applicationInstanceDao.create(applicationInstance);

        } catch (InvalidEntityException e) {
            throw new SdcRuntimeException(
                    "Can not create application instance", e);
        } catch (AlreadyExistsEntityException e) {
            throw new SdcRuntimeException(
                    "Can not create application instance", e);
        } catch (ShellCommandException e) {
            throw new SdcRuntimeException("Can not exectue the script", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void uninstall(ApplicationInstance applicationInstance) {
        try {
            String recipe =
                recipeNamingGenerator.getUninstallRecipe(applicationInstance);

            VM vm = applicationInstance.getProducts().get(0).getVM();

            callChef(recipe, vm);
            applicationInstance.setStatus(Status.UNINSTALLED);
            applicationInstanceDao.update(applicationInstance);

        } catch (InvalidEntityException e) {
            throw new SdcRuntimeException(
                    "Can not create application instance", e);
        } catch (ShellCommandException e) {
            throw new SdcRuntimeException("Can not exectue the script", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ApplicationInstance configure(
            ApplicationInstance applicationInstance,
            List<Attribute> configuration) {
        String filename = "role-"  + applicationInstance.getApplication()
            .getApplication().getName() + new Date().getTime();
        String recipe =
            recipeNamingGenerator.getInstallRecipe(applicationInstance);

        //the application shall be installed over, at least, one product
        VM vm = applicationInstance.getProducts().get(0).getVM();

        String populatedRole = populateRoleTemplate(vm, recipe, configuration,
                filename, applicationInstance.getApplication()
                .getApplication().getType());
        File file = createRoleFile(populatedRole, filename);
        try {
            updateAttributes(filename, file.getAbsolutePath(), vm);

        } catch (ShellCommandException e) {
            throw new SdcRuntimeException(e);
        }

        return applicationInstance;
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
