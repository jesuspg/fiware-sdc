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

package com.telefonica.euro_iaas.sdc.model.dto;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import junit.framework.TestCase;

import net.sf.json.JSONObject;

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
public class ChefNodeTest extends TestCase {

    ChefNode chefNode;
    String json;

    public static String NAME = "nosdcclient.novalocal";
    
    private String jsonFilePath = "src/test/resources/Chefnode.js";
    private String jsonFromFile;
    
    @Before
    public void setUp() throws Exception {
        jsonFromFile = getFile(jsonFilePath);
        chefNode = new ChefNode();
        
        json = "{\n"
                + "\"chef-webui\": \"http://localhost:4000/nodes/chef-webui\"\n"
                + "\"henartmactmysqlInstance2-tomcat7postgres8Tier-1.novalocal\": \"http://localhost:4000/nodes/henartmactmysqlInstance2-tomcat7postgres8Tier-1.novalocal\",\n"
                + "\"nosdcclient001\": \"http://localhost:4000/nodes/nosdcclient001\",\n"
                + "\"nosdcclient001.novalocal\": \"http://localhost:4000/nodes/nosdcclient001.novalocal\",\n"
                + "\"nosdcclient.novalocal\": \"http://localhost:4000/nodes/nosdcclient.novalocal\",\n"
                + "\"nosdcclient\": \"http://localhost:4000/nodes/nosdcclient\",\n"
                + "\"chef-validator\": \"http://localhost:4000/nodes/chef-validator\",\n"
                + "\"adam.novalocal\": \"http://localhost:4000/nodes/adam.novalocal\"\n" + "}\"";
        
        chefNode.addRecipe("recipe1");
        chefNode.addRecipe("recipe2");
        chefNode.addRecipe("recipe3");
        
        
    }

    @Test
    public void testGetChefClientURL2() throws Exception {
        String json2 =  json = "{\n"
                        + "\"chef-webui\": \"http:\\/\\/localhost:4000\\/nodes\\/chef-webui\"\n"
                        + "\"adam.novalocal\": \"http:\\/\\/localhost:4000\\/nodes\\/adam.novalocal\"\n" + "}\"";
        System.out.println("json: " + json2);
        String name = chefNode.getChefNodeName(json2, "chef-webui");
        assertEquals(name, "chef-webui");
    }
    
    @Test
    public void testGetChefClientURL() throws Exception {
        String name = chefNode.getChefNodeName(json, NAME);
        assertEquals(name, NAME);
    }
    @Test
    public void testHasRecipe() throws Exception {
         assertEquals(chefNode.hasRecipe("recipe"), false);
         assertEquals(chefNode.hasRecipe("recipe1"), true);
    }
    
    @Test
    public void testFromJson() throws Exception {
        chefNode.fromJson(JSONObject.fromObject(jsonFromFile));
        assertEquals(chefNode.getName(), "sdc15102013d.novalocal");
        assertEquals(chefNode.getJsonClass(), "Chef::Node");
        assertEquals(chefNode.getChefType(), "node");
        assertEquals(chefNode.getDefaults().isEmpty(), true);
        assertEquals(chefNode.getOverrides().isEmpty(), true);
        assertEquals(chefNode.getAttributes().isEmpty(), false);
        assertEquals(chefNode.getAttributes().containsKey("tags"), true);
        assertEquals(chefNode.getAutomaticAttributes().isEmpty(), false);
        assertEquals(chefNode.getAutomaticAttributes().containsKey("ohai_time"), true);
        JSONObject attr = new JSONObject();
        //attr.put("ohai_time", "1381841405.78531");
        Double value = (Double) chefNode.getAutomaticAttributes().get("ohai_time");
        System.out.println(value.toString());
        //Hay cinco segundos de diferencia ohai_time="1381841405.78531"
        assertEquals(chefNode.getAutomaticAttributes().get("ohai_time"), Double.valueOf("1381841410"));
    }
    
    private String getFile(String file) throws IOException {
        File f = new File(file);
        System.out.println(f.isFile() + " " + f.getAbsolutePath());

        InputStream dd = new FileInputStream(f);

        BufferedReader reader = new BufferedReader(new InputStreamReader(dd));
        StringBuffer ruleFile = new StringBuffer();
        String actualString;

        while ((actualString = reader.readLine()) != null) {
          ruleFile.append(actualString).append("\n");
        }
        return ruleFile.toString();
      }
}
