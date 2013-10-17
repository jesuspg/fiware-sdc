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

    public static String NAME = "henartmactmysqlInstance2-tomcat7postgres8Tier-1.novalocal";
    
    private String jsonFilePath = "src/test/resources/Chefnode.js";
    private String jsonFromFile;
    
    @Before
    public void setUp() throws Exception {
        jsonFromFile = getFile(jsonFilePath);
        chefNode = new ChefNode();
        
        json = "{\n"
                + "\"chef-webui\": \"http://localhost:4000/nodes/chef-webui\"\n"
                + "\"henartmactmysqlInstance2-tomcat7postgres8Tier-1.novalocal\": \"http://localhost:4000/nodes/henartmactmysqlInstance2-tomcat7postgres8Tier-1.novalocal\",\n"
                + "\"chef-validator\": \"http://localhost:4000/nodes/chef-validator\",\n"
                + "\"adam.novalocal\": \"http://localhost:4000/nodes/adam.novalocal\"\n" + "}\"";
        
        chefNode.addRecipe("recipe1");
        chefNode.addRecipe("recipe2");
        chefNode.addRecipe("recipe3");
        
        
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
