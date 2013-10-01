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

package com.telefonica.euro_iaas.sdc.model;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import com.telefonica.euro_iaas.sdc.model.dto.ChefClient;
import com.telefonica.euro_iaas.sdc.model.dto.ChefNode;

/**
 * 
 */

/**
 * @author jesus.movilla
 */
public class ChefClientTest extends TestCase {

    ChefClient chefClient;
    String json;

    public static String NAME = "henartmactmysqlInstance2-tomcat7postgres8Tier-1.novalocal";
    public static String KEY = "key";
    public static String VALUE = "value";
    public static String KEY1 = "key1";
    public static String VALUE1 = "value1";

    @Before
    public void setUp() throws Exception {

        chefClient = new ChefClient();

        json = "{\n"
                + "\"chef-webui\": \"http://localhost:4000/clients/chef-webui\"\n"
                + "\"henartmactmysqlInstance2-tomcat7postgres8Tier-1.novalocal\": \"http://localhost:4000/clients/henartmactmysqlInstance2-tomcat7postgres8Tier-1.novalocal\",\n"
                + "\"chef-validator\": \"http://localhost:4000/clients/chef-validator\",\n"
                + "\"adam.novalocal\": \"http://localhost:4000/clients/adam.novalocal\"\n" + "}\"";
    }

    @Test
    public void testGetChefClientURL() throws Exception {
        String name = chefClient.getChefClientName(json, NAME);
        assertEquals(name, NAME);
    }

    @Test
    public void testNodeCommand() throws Exception {
        ChefNode node = new ChefNode();
        node.setName(NAME);
        node.addRecipe("coobook");
        assertEquals(node.getRunlList().size(), 1);
        node.removeRecipe("coobook");
        assertEquals(node.getRunlList().size(), 0);
        node.addDefault(KEY, VALUE);
        node.addAttribute("att1", KEY1, VALUE1);
        node.addOverride(KEY1, "VALUE2");
        assertEquals(node.getAttributes().size(), 1);
        node.removeAttritube(KEY1);
        assertEquals(node.getAttributes().size(), 1);

    }

}
