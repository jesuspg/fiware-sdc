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
    String json, json2;

    public static String NAME = "henartmactmysqlInstance2-tomcat7postgres8Tier-1.novalocal";
    public static String NAME2 = "new-chef-client2.novalocal";
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
        json2 = "{\n"
                + "\"dhcp-17-155.imdeass\":\"https:\\/\\/130.206.81.105\\/clients\\/dhcp-17-155.imdeass\",\n"
                + "\"new-chef-client2.novalocal\":\"https:\\/\\/130.206.81.105\\/clients\\/new-chef-client2.novalocal\",\n"
                + "\"henarcentos-6\":\"https:\\/\\/130.206.81.105\\/clients\\/henarcentos-6\"\n" + "}\"";
    }

    @Test
    public void testGetChefClientURL() throws Exception {
        String name = chefClient.getChefClientName(json, NAME);
        assertEquals(name, NAME);
    }
    
    @Test
    public void testGetChefClientUR2L() throws Exception {
        String name2 = chefClient.getChefClientName(json2, NAME2);
        assertEquals(name2, NAME2);
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
