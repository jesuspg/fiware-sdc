/**
 * Copyright 2014 Telefonica Investigación y Desarrollo, S.A.U <br>
 * This file is part of FI-WARE project.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License.
 * </p>
 * <p>
 * You may obtain a copy of the License at:<br>
 * <br>
 * http://www.apache.org/licenses/LICENSE-2.0
 * </p>
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * </p>
 * <p>
 * See the License for the specific language governing permissions and limitations under the License.
 * </p>
 * <p>
 * For those usages not covered by the Apache version 2.0 License please contact with opensource@tid.es
 * </p>
 */

package com.telefonica.euro_iaas.sdc.util;

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
     * Generates the uninstall recipe's name for a concrete product.
     * 
     * @param product
     *            the product containing all information
     * @return the recipe name
     */
    public String getUninstallRecipe(ProductInstance product);

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
     * Generates the restore recipe's name for a concrete product.
     * 
     * @param product
     *            the product containing all information
     * @return the recipe name
     */
    public String getRestoreRecipe(ProductInstance product);

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
