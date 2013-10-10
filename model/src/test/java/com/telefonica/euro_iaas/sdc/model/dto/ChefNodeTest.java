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
public class ChefNodeTest extends TestCase {

    ChefNode chefNode;
    String json;

    public static String NAME = "henartmactmysqlInstance2-tomcat7postgres8Tier-1.novalocal";

    @Before
    public void setUp() throws Exception {

        chefNode = new ChefNode();

        json = "{\n"
                + "\"chef-webui\": \"http://localhost:4000/nodes/chef-webui\"\n"
                + "\"henartmactmysqlInstance2-tomcat7postgres8Tier-1.novalocal\": \"http://localhost:4000/nodes/henartmactmysqlInstance2-tomcat7postgres8Tier-1.novalocal\",\n"
                + "\"chef-validator\": \"http://localhost:4000/nodes/chef-validator\",\n"
                + "\"adam.novalocal\": \"http://localhost:4000/nodes/adam.novalocal\"\n" + "}\"";
    }

    @Test
    public void testGetChefClientURL() throws Exception {
        String name = chefNode.getChefNodeName(json, NAME);
        assertEquals(name, NAME);
    }
}
