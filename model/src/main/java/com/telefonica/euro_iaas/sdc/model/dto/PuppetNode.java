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

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Models a chef node.
 * 
 * @author Sergio Arroyo
 */
public class PuppetNode {

    private final static String RECIPE_ITEM_TEMPLATE = "recipe[{0}]";

    private String name;
    private String deactivated;
    private String catalog_timestamp;
    private String facts_timestamp;
    private String report_timestamp;
    
    public String getName (){
    	return name;
    }
    
    public String getCatalogTimestamp (){
    	return catalog_timestamp;
    }


    @SuppressWarnings("unchecked")
    public void fromJson(JSONObject jsonNode) {
        name = jsonNode.getString("name");
        deactivated = jsonNode.getString("deactivated");
        catalog_timestamp = jsonNode.getString("catalog_timestamp");
        catalog_timestamp = jsonNode.getString("catalog_timestamp");
        report_timestamp = jsonNode.getString("report_timestamp");

    }
    
    @SuppressWarnings("unchecked")
    public String getNodeName(String stringNodes, String hostname) {

    	JSONArray array = JSONArray.fromObject(stringNodes);
    	for(Object js : array){
            JSONObject json = (JSONObject)js;
            PuppetNode node = new PuppetNode();
            node.fromJson(json);
            if (node.getName().startsWith(hostname+".")) {
            	return node.getName();
            }
        }
        return null;
    }
    
    @SuppressWarnings("unchecked")
    public PuppetNode getNode(String stringNodes, String hostname) {

    	JSONArray array = JSONArray.fromObject(stringNodes);
    	for(Object js : array){
            JSONObject json = (JSONObject)js;
            PuppetNode node = new PuppetNode();
            node.fromJson(json);
            if (node.getName().startsWith(hostname+".")) {
            	return node;
            }
        }
        return null;
    }

}
