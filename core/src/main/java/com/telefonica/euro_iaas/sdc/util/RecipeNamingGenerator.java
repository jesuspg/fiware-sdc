/**
 *   (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights
 *   Reserved.
 * 
 *   The copyright to the software program(s) is property of Telefonica I+D.
 *   The program(s) may be used and or copied only with the express written
 *   consent of Telefonica I+D or in accordance with the terms and conditions
 *   stipulated in the agreement/contract under which the program(s) have
 *   been supplied.
 */

package com.telefonica.euro_iaas.sdc.util;

import com.telefonica.euro_iaas.sdc.model.ApplicationInstance;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;

/**
 * Generates the names for every recipe. This names shall be according to naming conventions
 * 
 * @author Sergio Arroyo
 */
public interface RecipeNamingGenerator {

    /**
     * Generates the install recipe's name for a concrete product.
     * 
     * @param product
     *            the product containing all information
     * @return the recipe name
     */
    public String getInstallRecipe(ProductInstance product);

    /**
     * Generates the install recipe's name for a concrete application.
     * 
     * @param product
     *            the application containing all information
     * @return the recipe name
     */
    public String getInstallRecipe(ApplicationInstance application);

    /**
     * Generates the uninstall recipe's name for a concrete product.
     * 
     * @param product
     *            the product containing all information
     * @return the recipe name
     */
    public String getUninstallRecipe(ProductInstance product);

    /**
     * Generates the uninstall recipe's name for a concrete application.
     * 
     * @param product
     *            the application containing all information
     * @return the recipe name
     */
    public String getUninstallRecipe(ApplicationInstance application);

    /**
     * Generates the backup recipe's name for a concrete product.
     * 
     * @param product
     *            the product containing all information
     * @return the recipe name
     */
    public String getBackupRecipe(ProductInstance product);

    public String getConfigureRecipe(ProductInstance product);

    /**
     * Generates the backup recipe's name for a concrete application.
     * 
     * @param product
     *            the application containing all information
     * @return the recipe name
     */
    public String getBackupRecipe(ApplicationInstance application);

    /**
     * Generates the restore recipe's name for a concrete product.
     * 
     * @param product
     *            the product containing all information
     * @return the recipe name
     */
    public String getRestoreRecipe(ProductInstance product);

    /**
     * Generates the restore recipe's name for a concrete application.
     * 
     * @param product
     *            the application containing all information
     * @return the recipe name
     */
    public String getRestoreRecipe(ApplicationInstance application);

    /**
     * Generates the deploy Artifact operation.
     * 
     * @param product
     *            the application containing all information
     * @return the recipe name
     */

    public String getDeployArtifactRecipe(ProductInstance productInstance);

    /**
     * Generates the undeploy Artifact operation.
     * 
     * @param product
     *            the application containing all information
     * @return the recipe name
     */

    public String getUnDeployArtifactRecipe(ProductInstance productInstance);

}
