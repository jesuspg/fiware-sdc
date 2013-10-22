/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.sdc.model.dto;

import junit.framework.Assert;
import junit.framework.TestCase;
import net.sf.json.JSONObject;

public class ChefNodeTest extends TestCase {

    private final static String JSON_STRING = "{\"normal\":{\"tags\":[],\"postgresql\":"
            + "{\"dir\":\"/etc/postgresql/8.4/main\"}},"
            + "\"name\":\"flexichefnode3.flexiscale.com\",\"override\":{},"
            + "\"ohai_time\":\"123456789\","
            + "\"default\":{},\"json_class\":\"Chef::Node\"," + "\"run_list\": [ \"recipe[apache::2_install]\" ]}";

    public void testChefFromJSon() {
        JSONObject jsonObject = JSONObject.fromObject(JSON_STRING);
        ChefNode node = new ChefNode();
        node.fromJson(jsonObject);
        node.addOverride("war", "application_context", "sdc");
        node.addOverride("war", "application_context", "app-conf");
        node.addAttribute("postgresql", "application_context", "app-conf");
        Assert.assertEquals(node.getName(), "flexichefnode3.flexiscale.com");
        Assert.assertEquals(node.getRunlList().get(0), "recipe[apache::2_install]");
    }

    public void testChefToJSon() {
        JSONObject jsonObject = JSONObject.fromObject(JSON_STRING);
        ChefNode node = new ChefNode();
        node.fromJson(jsonObject);
        String convertedJson = node.toJson();
    }

}
