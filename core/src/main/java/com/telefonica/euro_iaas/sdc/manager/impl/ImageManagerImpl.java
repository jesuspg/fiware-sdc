package com.telefonica.euro_iaas.sdc.manager.impl;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.telefonica.euro_iaas.commons.dao.AlreadyExistsEntityException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.sdc.dao.ImageDao;
import com.telefonica.euro_iaas.sdc.dao.OSDao;
import com.telefonica.euro_iaas.sdc.dao.ProductDao;
import com.telefonica.euro_iaas.sdc.exception.CanNotDeployException;
import com.telefonica.euro_iaas.sdc.exception.SdcRuntimeException;
import com.telefonica.euro_iaas.sdc.exception.UnableToStartOSException;
import com.telefonica.euro_iaas.sdc.manager.ImageManager;
import com.telefonica.euro_iaas.sdc.manager.OSInstanceManager;
import com.telefonica.euro_iaas.sdc.manager.ProductInstanceManager;
import com.telefonica.euro_iaas.sdc.model.Customer;
import com.telefonica.euro_iaas.sdc.model.Image;
import com.telefonica.euro_iaas.sdc.model.OS;
import com.telefonica.euro_iaas.sdc.model.OSInstance;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.dto.VM;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ProductSearchCriteria;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.SOSearchCriteria;

/**
 * Default ImageManager implementation.
 *
 * @author Sergio Arroyo
 */
public class ImageManagerImpl implements ImageManager {

    private OSDao osDao;
    private ProductDao productDao;
    private ImageDao imageDao;
    private ProductInstanceManager productInstanceManager;
    private OSInstanceManager osInstanceManager;


    private static final Logger LOGGER = Logger.getLogger("ImageManagerImpl");

    /** {@inheritDoc} */
    @Override
    public Image deploy(Customer customer, OS so, VM vm, List<Product> products)
            throws CanNotDeployException {
        OSInstance soInstance = null;
        try {
            soInstance = osInstanceManager.startAndRunning(so, vm);

            LOGGER.log(Level.INFO, " The SOInstance has been already created");
            Image image = new Image();
            image.setSo(soInstance);
            image.setCustomer(customer);
            image.setApps(productInstanceManager.install(vm, products));

            LOGGER.log(Level.INFO, " The Products has been successfully "
                    + "installed  ");

            soInstance = osInstanceManager.freeze(image.getSo());
            image.setUrl(soInstance.getImageFileLocation());

            image = imageDao.create(image);

            LOGGER.log (Level.INFO, " The Image has been successfully " +
            "stored  ");
            return image;

        } catch (AlreadyExistsEntityException aeee) {
            LOGGER.log(Level.SEVERE, " The Image "
                    + soInstance.getSo().getName() + " already exists");
            LOGGER.log(Level.SEVERE, aeee.getMessage());
            throw new SdcRuntimeException(aeee);
        } catch (InvalidEntityException e) {
            LOGGER.log(Level.SEVERE, " The Instance "
                    + soInstance.getSo().getName() + " is invalid");
            throw new SdcRuntimeException(e);
        } catch (UnableToStartOSException e) {
            LOGGER.log(Level.SEVERE, "An error happend starting SO"
                    + so.getName() + "in vm" + vm);
            throw new SdcRuntimeException(e);
        }

    }

    /** {@inheritDoc} */
    @Override
    public List<Product> findAllProducts(ProductSearchCriteria criteria) {
        // TODO Auto-generated method stub
        return productDao.findAll();
    }

    /** {@inheritDoc} */
    @Override
    public List<OS> findAllSSOO(SOSearchCriteria criteria) {
        return osDao.findAll();
    }


    ///////////////// I.O.C //////////////////

    /**
     * @param osDao the osDao to set
     */
    public void setOsDao(OSDao osDao) {
        this.osDao = osDao;
    }

    /**
     * @param productDao the productDao to set
     */
    public void setProductDao(ProductDao productDao) {
        this.productDao = productDao;
    }

    /**
     * @param imageDao the imageDao to set
     */
    public void setImageDao(ImageDao imageDao) {
        this.imageDao = imageDao;
    }

    /**
     * @param productInstanceManager the productInstanceManager to set
     */
    public void setProductInstanceManager(
            ProductInstanceManager productInstanceManager) {
        this.productInstanceManager = productInstanceManager;
    }

    /**
     * @param osInstanceManager the osInstanceManager to set
     */
    public void setOsInstanceManager(OSInstanceManager osInstanceManager) {
        this.osInstanceManager = osInstanceManager;
    }
}
