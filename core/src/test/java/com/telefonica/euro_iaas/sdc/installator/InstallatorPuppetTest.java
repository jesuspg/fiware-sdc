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

package com.telefonica.euro_iaas.sdc.installator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.telefonica.euro_iaas.sdc.exception.CanNotCallChefException;
import com.telefonica.euro_iaas.sdc.exception.CanNotCallPuppetException;
import com.telefonica.euro_iaas.sdc.exception.InstallatorException;
import com.telefonica.euro_iaas.sdc.exception.NodeExecutionException;
import com.telefonica.euro_iaas.sdc.exception.OpenStackException;
import com.telefonica.euro_iaas.sdc.installator.impl.InstallatorPuppetImpl;
import com.telefonica.euro_iaas.sdc.keystoneutils.OpenStackRegion;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.sdc.model.Metadata;
import com.telefonica.euro_iaas.sdc.model.OS;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.dto.PuppetNode;
import com.telefonica.euro_iaas.sdc.model.dto.VM;
import com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider;

public class InstallatorPuppetTest {

    private HttpClient client;
    private InstallatorPuppetImpl puppetInstallator;
    private SystemPropertiesProvider propertiesProvider;
    private Product product;
    private VM host;
    private ProductRelease productRelease;
    private ProductInstance productInstance;
    private OS os;
    private HttpResponse response;
    private HttpEntity entity;
    private StatusLine statusLine;
    private OpenStackRegion openStackRegion;

    String GET_NODES = "[ {\"name\" : \"aaaa-dddfafff-1-000081.novalocal\", " + "\"deactivated\" : null, "
            + "\"catalog_timestamp\" : null, " + "\"facts_timestamp\" : \"2014-08-27T09:39:36.438Z\","
            + "\"report_timestamp\" : null " + "}, {" + "\"name\" : \"abasd-tt-1-000081.novalocal\","
            + "\"deactivated\" : null, " + "\"catalog_timestamp\" : \"2014-08-27T10:34:02.892Z\","
            + "\"facts_timestamp\" : \"2014-08-27T10:35:07.365Z\"," + "\"report_timestamp\" : null" + "}, {"
            + "\"name\" : \"testName.novalocal\"," + "\"deactivated\" : null, "
            + "\"catalog_timestamp\" : \"2014-08-27T10:34:02.892Z\","
            + "\"facts_timestamp\" : \"2014-08-27T10:35:07.365Z\"," + "\"report_timestamp\" : null" + "}]";

    private List<Attribute> attributeList;
    private Attribute attribute1;

    @Before
    public void setup() throws ClientProtocolException, IOException, OpenStackException {
        Product product = new Product("testProduct", "description");
        Metadata metadata = new Metadata("installator", "puppet");
        List<Metadata> metadatas = new ArrayList<Metadata>();
        metadatas.add(metadata);
        product.setMetadatas(metadatas);

        host = new VM("fqn", "ip", "testName", "domain");

        os = new OS("os1", "1", "os1 description", "v1");
        host.setOsType(os.getOsType());

        productRelease = new ProductRelease("version", "releaseNotes", product, Arrays.asList(os), null);
        productInstance = new ProductInstance(productRelease, Status.INSTALLING, host, "vdc");

        attributeList = new ArrayList<Attribute>();
        attribute1 = new Attribute("user", "pepito","desc","Plain");

        attributeList.add(attribute1);

        // client = (HttpClient) new HTTPClientMock();

        client = mock(HttpClient.class);

        response = mock(HttpResponse.class);
        entity = mock(HttpEntity.class);
        statusLine = mock(StatusLine.class);
        openStackRegion = mock(OpenStackRegion.class);

        when(client.execute((HttpUriRequest) Mockito.anyObject())).thenReturn(response);
        when(response.getEntity()).thenReturn(entity);
        String source = "";
        InputStream in = IOUtils.toInputStream(source, "UTF-8");
        when(entity.getContent()).thenReturn(in);
        when(response.getStatusLine()).thenReturn(statusLine);

        propertiesProvider = mock(SystemPropertiesProvider.class);
        when(propertiesProvider.getProperty("PUPPET_MASTER_URL")).thenReturn(
                "http://130.206.82.190:8080/puppetwrapper/");

        puppetInstallator = new InstallatorPuppetImpl();
        puppetInstallator.setClient(client);
        puppetInstallator.setOpenStackRegion(openStackRegion);
        when(openStackRegion.getPuppetWrapperEndPoint("token")).thenReturn("http://");

    }

    @Test
    public void testGenerateFilesinPuppetMaster() throws InstallatorException, NodeExecutionException {

        when(statusLine.getStatusCode()).thenReturn(200);
        puppetInstallator.generateFilesinPuppetMaster(host, "test", productRelease, "install", "token");
    }

    public void testCallService_all_OK() throws InstallatorException, NodeExecutionException {

        when(statusLine.getStatusCode()).thenReturn(200);
        puppetInstallator.callService(host, "test", productRelease, "install", "token");
    }

    @Test(expected = InstallatorException.class)
    public void testCallService_FAIL() throws InstallatorException, NodeExecutionException, OpenStackException {

        when(statusLine.getStatusCode()).thenReturn(500);
        puppetInstallator.generateFilesinPuppetMaster(host, "test", productRelease, "install", "token");
        puppetInstallator.callService(host, "test", productRelease, "install", "token");
    }

    @Test(expected = InstallatorException.class)
    public void testCallService_1_OK_1_FAIL() throws InstallatorException, NodeExecutionException {

        when(statusLine.getStatusCode()).thenReturn(200).thenReturn(500);
        puppetInstallator.generateFilesinPuppetMaster(host, "test", productRelease, "install", "token");

    }

    @Test
    public void testIsNodeDeployed() throws OpenStackException, CanNotCallPuppetException, IOException {

        when(statusLine.getStatusCode()).thenReturn(200).thenReturn(500);
        when(openStackRegion.getPuppetDBEndPoint(any(String.class))).thenReturn("http");

        InputStream in = IOUtils.toInputStream(GET_NODES, "UTF-8");
        when(entity.getContent()).thenReturn(in);
        puppetInstallator.isNodeRegistered("aaaa-dddfafff-1-000081", "token");

    }

    @Test
    public void testLoadNode() throws OpenStackException, CanNotCallPuppetException, IOException, InstallatorException {

        when(statusLine.getStatusCode()).thenReturn(200).thenReturn(500);
        when(openStackRegion.getPuppetDBEndPoint(any(String.class))).thenReturn("http");

        InputStream in = IOUtils.toInputStream(GET_NODES, "UTF-8");
        when(entity.getContent()).thenReturn(in);
        PuppetNode node = puppetInstallator.loadNode("aaaa-dddfafff-1-000081", "token");
        assertNotNull(node);
        assertEquals(node.getName(), "aaaa-dddfafff-1-000081.novalocal");

    }

    @Test
    public void testIsRecipedExecuted() throws OpenStackException, CanNotCallPuppetException, IOException,
            InstallatorException, NodeExecutionException {

        when(statusLine.getStatusCode()).thenReturn(200).thenReturn(500);
        when(openStackRegion.getPuppetDBEndPoint(any(String.class))).thenReturn("http");

        InputStream in = IOUtils.toInputStream(GET_NODES, "UTF-8");
        when(entity.getContent()).thenReturn(in);
        VM vm = new VM("aaaa-dddfafff-1-000081", "ip", "abasd-tt-1-000081", "domain");
        puppetInstallator.MAX_TIME = 10000;
        puppetInstallator.isRecipeExecuted(vm, "recipe", "token");

    }

    @Test(expected = NodeExecutionException.class)
    public void testIsRecipedNoExecuted() throws OpenStackException, CanNotCallChefException, IOException,
            InstallatorException, NodeExecutionException {

        when(statusLine.getStatusCode()).thenReturn(200).thenReturn(500);
        when(openStackRegion.getPuppetDBEndPoint(any(String.class))).thenReturn("http");

        InputStream in = IOUtils.toInputStream(GET_NODES, "UTF-8");
        when(entity.getContent()).thenReturn(in);
        puppetInstallator.MAX_TIME = 10000;
        VM vm = new VM("aaaa-dddfafff-1-000081", "ip", "aaaa-dddfafff-1-000081", "domain");
        puppetInstallator.isRecipeExecuted(vm, "recipe", "token");

    }

    @Test(expected = InstallatorException.class)
    public void testLoadNodeNoExists() throws OpenStackException, CanNotCallPuppetException, IOException,
            InstallatorException, NodeExecutionException {

        when(statusLine.getStatusCode()).thenReturn(200).thenReturn(500);
        when(openStackRegion.getPuppetDBEndPoint(any(String.class))).thenReturn("http");

        InputStream in = IOUtils.toInputStream(GET_NODES, "UTF-8");
        when(entity.getContent()).thenReturn(in);
        puppetInstallator.loadNode("noexists", "token");
        puppetInstallator.callService(host, "test", productRelease, "install", "token");

    }

    @Test
    public void testCallService_attributes_all_OK() throws InstallatorException, NodeExecutionException, IOException {

        when(statusLine.getStatusCode()).thenReturn(200);
        InputStream in = IOUtils.toInputStream(GET_NODES, "UTF-8");
        when(entity.getContent()).thenReturn(in);

        puppetInstallator.callService(productInstance, host, attributeList, "install", "token");
    }

    @Test
    public void testformatAttributes() throws InstallatorException, NodeExecutionException, IOException {

        List<Attribute> li = new ArrayList<Attribute>();
        li.add(new Attribute("key", "111.111.111.111,222.222.222.222", "description", "IPALL"));
        List<Attribute> receivedList = puppetInstallator.formatAttributesForPuppet(li);
        assertEquals("['111.111.111.111','222.222.222.222']",receivedList.get(0).getValue());
        

    }
}
