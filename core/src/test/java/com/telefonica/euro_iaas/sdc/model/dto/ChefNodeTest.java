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

import static org.junit.Assert.assertEquals;
import net.sf.json.JSONObject;

import org.junit.Test;

public class ChefNodeTest {

    private final static String JSON_STRING = "{\"normal\":{\"tags\":[],\"postgresql\":"
            + "{\"dir\":\"/etc/postgresql/8.4/main\"}},"
            + "\"name\":\"flexichefnode3.flexiscale.com\",\"override\":{}," + "\"ohai_time\":\"123456789\","
            + "\"default\":{},\"json_class\":\"Chef::Node\"," + "\"run_list\": [ \"recipe[apache::2_install]\" ]}";

    @Test
    public void testChefFromJSon() {
        JSONObject jsonObject = JSONObject.fromObject(JSON_STRING);
        ChefNode node = new ChefNode();
        node.fromJson(jsonObject);
        node.addOverride("war", "application_context", "sdc");
        node.addOverride("war", "application_context", "app-conf");
        node.addAttribute("postgresql", "application_context", "app-conf");
        assertEquals(node.getName(), "flexichefnode3.flexiscale.com");
        assertEquals(node.getRunlList().get(0), "recipe[apache::2_install]");
    }

    @Test
    public void testChefToJSon() {
        JSONObject jsonObject = JSONObject.fromObject(JSON_STRING);
        ChefNode node = new ChefNode();
        node.fromJson(jsonObject);
        String convertedJson = node.toJson();
    }

}
