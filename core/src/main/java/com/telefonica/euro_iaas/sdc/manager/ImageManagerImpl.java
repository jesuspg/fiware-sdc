package com.telefonica.euro_iaas.sdc.manager;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.telefonica.euro_iaas.commons.dao.AlreadyExistsEntityException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.sdc.dao.ImageDao;
import com.telefonica.euro_iaas.sdc.dao.ProductDao;
import com.telefonica.euro_iaas.sdc.dao.SODao;
import com.telefonica.euro_iaas.sdc.dao.SOInstanceDao;
import com.telefonica.euro_iaas.sdc.exception.CanNotDeployException;
import com.telefonica.euro_iaas.sdc.exception.ShellCommandException;
import com.telefonica.euro_iaas.sdc.model.Customer;
import com.telefonica.euro_iaas.sdc.model.Image;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.SO;
import com.telefonica.euro_iaas.sdc.model.SOInstance;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ProductSearchCriteria;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.SOSearchCriteria;
import com.telefonica.euro_iaas.sdc.util.AppDeployer;
import com.telefonica.euro_iaas.sdc.util.SODeployer;
import com.telefonica.euro_iaas.sdc.util.SOStatusChecker;

/**
 * Default ImageManager implementation.
 *
 * @author Sergio Arroyo
 * @version $Id: $
 */
public class ImageManagerImpl implements ImageManager {

    private SODao soDao;
    private ProductDao productDao;
    private ImageDao imageDao;
    private SOInstanceDao soInstanceDao;
    private AppDeployer appDeployer;
    private SODeployer soDeployer;
    private SOStatusChecker soStatusChecker;

    private static Logger logger = Logger.getLogger("ImageManagerImpl");

    /** {@inheritDoc} */
    @Override
    public Image deploy(Customer customer, SO so, List<Product> products)
            throws CanNotDeployException {

        SOInstance soInstance = soDeployer.deploySO(so);
        Image image = null;

        try {
            soInstance = soInstanceDao.create(soInstance);
            logger.log(Level.INFO, " The SOInstance has been already created");

            soStatusChecker.waitOK(soInstance);

            image = appDeployer.install(customer, soInstance, products);

            image = imageDao.create(image);
            logger.log(Level.INFO, " The Products has been successfully "
                    + "installed  ");

            image = soDeployer.stopImage(image);
            logger.log (Level.INFO, " The Image has been successfully " +
            "stored  ");

        } catch (AlreadyExistsEntityException aeee) {
            logger.log(Level.SEVERE, " The Instance "
                    + soInstance.getSo().getName() + " already exists");
            logger.log(Level.SEVERE, aeee.getMessage());
        } catch (InterruptedException ie) {
            logger.log(Level.SEVERE, "The waiting phase to the assignment "
                    + "of the recipes has been interrupted");
            logger.log(Level.SEVERE, ie.getMessage());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ShellCommandException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidEntityException e) {
            logger.log(Level.SEVERE, " The Instance "
                    + soInstance.getSo().getName() + " is invalid");
        }

        return image;
    }

    /** {@inheritDoc} */
    @Override
    public List<Product> findAllProducts(ProductSearchCriteria criteria) {
        // TODO Auto-generated method stub
        return productDao.findAll();
    }

    /** {@inheritDoc} */
    @Override
    public List<SO> findAllSSOO(SOSearchCriteria criteria) {
        return soDao.findAll();
    }

    /**
     * <p>
     * Setter for the field <code>soDao</code>.
     * </p>
     *
     * @param soDao
     *            the soDao to set
     */
    public void setSoDao(SODao soDao) {
        this.soDao = soDao;
    }

    /**
     * <p>
     * Setter for the field <code>productDao</code>.
     * </p>
     *
     * @param productDao
     *            the productDao to set
     */
    public void setProductDao(ProductDao productDao) {
        this.productDao = productDao;
    }

    /**
     * <p>
     * Setter for the field <code>imageDao</code>.
     * </p>
     *
     * @param imageDao
     *            the imageDao to set
     */
    public void setImageDao(ImageDao imageDao) {
        this.imageDao = imageDao;
    }

    /**
     * <p>
     * Setter for the field <code>appDeployer</code>.
     * </p>
     *
     * @param appDeployer
     *            the appDeployer to set
     */
    public void setAppDeployer(AppDeployer appDeployer) {
        this.appDeployer = appDeployer;
    }

    /**
     * <p>
     * Setter for the field <code>soDeployer</code>.
     * </p>
     *
     * @param soDeployer
     *            the soDeployer to set
     */
    public void setSoDeployer(SODeployer soDeployer) {
        this.soDeployer = soDeployer;
    }

    /**
     * <p>
     * Setter for the field <code>soInstanceDao</code>.
     * </p>
     *
     * @param soInstanceDao
     *            a {@link com.telefonica.euro_iaas.sdc.dao.SOInstanceDao}
     *            object.
     */
    public void setSoInstanceDao(SOInstanceDao soInstanceDao) {
        this.soInstanceDao = soInstanceDao;
    }

    /**
     * @param soStatusChecker
     *            the soStatusChecker to set
     */
    public void setSoStatusChecker(SOStatusChecker soStatusChecker) {
        this.soStatusChecker = soStatusChecker;
    }

}
