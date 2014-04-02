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
