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

import org.junit.Test;

/**
 * Unit test suite for RecipeNamingGenerator.
 * 
 * @author Sergio Arroyo
 */
public class RecipeNamingGeneratorImplTest {

    private final static String INSTALL_PRODUCT_RECIPE_TEMPLATE = "{0}::{1}_install";
    private final static String INSTALL_APPLICATION_RECIPE_TEMPLATE = "{0}::{1}-{2}_install_{3}";
    private final static String PRODUCT_LIST_TEMPLATE = "{0}-{1}";
    private final static String PRODUCT_LIST_SEPARATOR = "-";

    private final static String INSTALL_PRODUCT_RECIPE = "tomcat::5.5_install";
    private final static String INSTALL_APPLICATION_RECIPE = "war::sdc-1.0.0_install_postgresql-8.4_tomcat-6";

    /**
     * This test will receive the installation template and will return the installation template filled with the
     * correct values.
     * 
     * @throws Exception
     */
    @Test
    public void testGetInstallProductRecipe() throws Exception {

    }

    /**
     * This test will receive the installation template and will return the installation template filled with the
     * correct values.
     * 
     * @throws Exception
     */
    @Test
    public void testGetInstallApplicationRecipe() throws Exception {

    }

}
