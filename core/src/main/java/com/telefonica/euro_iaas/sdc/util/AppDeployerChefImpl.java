package com.telefonica.euro_iaas.sdc.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.telefonica.euro_iaas.sdc.exception.ShellCommandException;
import com.telefonica.euro_iaas.sdc.model.Customer;
import com.telefonica.euro_iaas.sdc.model.Image;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.SOInstance;
import com.telefonica.euro_iaas.sdc.model.ProductInstance.Status;

/**
 * <p>
 * AppDeployerChefImpl class.
 * </p>
 *
 * @author ju
 * @version $Id: $
 */
public class AppDeployerChefImpl extends AbstractShellCommand implements
        AppDeployer {

    private static Logger logger = Logger.getLogger("AppDeployerChefImpl");

    /** {@inheritDoc} */
    @Override
    public Image install(Customer customer, SOInstance so,
            List<Product> products) {
        // TODO Auto-generated method stub
        Image image = new Image();
        List<ProductInstance> productInstances = new ArrayList<ProductInstance>();
        ProductInstance productInstance;
        try {
            for (Product product : products) {
                productInstance = new ProductInstance();
                assignProductToSO(so, product);
                productInstance.setProduct(product);
                productInstance.setStatus(ProductInstance.Status.INSTALLING);
                productInstances.add(productInstance);
            }

            installProducts(so);
            for (ProductInstance instance: productInstances) {
                instance.setStatus(Status.INSTALLED);
            }

            image.setSo(so);
            image.setApps(productInstances);
            image.setCustomer(customer);

        } catch (IOException ioe) {
            logger.log(Level.SEVERE, ioe.getMessage());
        } catch (ShellCommandException sce)
        {
            logger.log (Level.SEVERE, sce.getMessage());
        }
        return image;
    }

    /** {@inheritDoc} */
    @Override
    public Image uninstall(Customer customer, SOInstance so, Product app) {
        // TODO Auto-generated method stub
        return null;
    }

    private void assignProductToSO(SOInstance so, Product product) throws IOException, ShellCommandException
    {
        String[] output = new String[2];
        String command = "/root/chef-repo/scripts/assignRecipes.sh " + so.getHostname() + ".hi.inet"  + " " + product.getName();
        output = executeCommand (command);
    }

    private void installProducts (SOInstance so) throws IOException, ShellCommandException
    {
        String[] output = new String[2];
        String command = "/root/chef-repo/scripts/executeRecipes.sh " + "root@" + so.getHostname() + ".hi.inet" ;
        output = executeCommand (command);
    }

}
