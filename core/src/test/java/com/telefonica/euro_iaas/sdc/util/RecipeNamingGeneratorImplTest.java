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
