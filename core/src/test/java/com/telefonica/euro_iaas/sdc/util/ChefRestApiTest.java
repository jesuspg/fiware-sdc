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

package com.telefonica.euro_iaas.sdc.util;

import org.junit.Test;

public class ChefRestApiTest {
    String node;

    @Test
    public void testGetNodes() throws Exception {

        // Map<String, String> header =
        // new MixlibAuthenticationDigesterImpl().digest(
        // "get","/nodes/duck-hunt", "", new Date(), "serch",
        // "/home/ju/tidWorkspace/sdc-server/core/src/test/resources/private.pem");
        // Client client = Client.create();
        // WebResource webResource =
        // client.resource("http://109.231.82.12:4000/nodes/duck-hunt");
        // Builder wr = webResource.accept(MediaType.APPLICATION_JSON);
        // for (String key : header.keySet()) {
        // wr = wr.header(key, header.get(key));
        // }
        // this.node = IOUtils.toString(wr.get(InputStream.class));
        // JSONObject jsonNode = JSONObject.fromObject(this.node);
        //
        // //update the node
        // ChefNode node = new ChefNode();
        // node.fromJson(jsonNode);
        //
        // node.deleteRecipe("test");
        // node.addAttritube("newAttribute", "sdagasdgasdg");
        // node.removeAttritube("attribute.key");
        // String body = node.toJson();
        //
        //
        // header =
        // new MixlibAuthenticationDigesterImpl().digest(
        // "put","/nodes/duck-hunt", body, new Date(), "serch",
        // "/home/ju/tidWorkspace/sdc-server/core/src/test/resources/private.pem");
        //
        // wr = webResource.accept(MediaType.APPLICATION_JSON)
        // .type(MediaType.APPLICATION_JSON).entity(body);
        // for (String key : header.keySet()) {
        // wr = wr.header(key, header.get(key));
        // }
        // this.node = IOUtils.toString(wr.put(InputStream.class));
        // jsonNode = JSONObject.fromObject(this.node);
        //
        // ChefNode newNode = new ChefNode();
        // newNode.fromJson(jsonNode);

    }

}
