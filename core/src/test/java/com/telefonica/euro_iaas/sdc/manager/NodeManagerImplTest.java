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

package com.telefonica.euro_iaas.sdc.manager;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.telefonica.euro_iaas.sdc.dao.ChefClientDao;
import com.telefonica.euro_iaas.sdc.dao.ChefNodeDao;
import com.telefonica.euro_iaas.sdc.dao.ProductInstanceDao;
import com.telefonica.euro_iaas.sdc.exception.CanNotCallChefException;
import com.telefonica.euro_iaas.sdc.exception.NodeExecutionException;
import com.telefonica.euro_iaas.sdc.exception.SdcRuntimeException;
import com.telefonica.euro_iaas.sdc.keystoneutils.OpenStackRegion;
import com.telefonica.euro_iaas.sdc.manager.impl.NodeManagerImpl;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.dto.ChefNode;
import com.telefonica.euro_iaas.sdc.util.HttpsClient;
import com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider;
import com.telefonica.fiware.commons.dao.EntityNotFoundException;
import com.telefonica.fiware.commons.openstack.auth.exception.OpenStackException;

public class NodeManagerImplTest {

    private ProductInstanceDao productInstanceDao;
    private ChefClientDao chefClientDao;
    private ChefNodeDao chefNodeDao;
    private HttpClient client;
    private HttpsClient httpsClient;
    private SystemPropertiesProvider propertiesProvider;

    private NodeManagerImpl nodeManager;

    @SuppressWarnings("unchecked")
    @Before
    public void setup() throws CanNotCallChefException, OpenStackException {

        productInstanceDao = mock(ProductInstanceDao.class);
        chefClientDao = mock(ChefClientDao.class);
        chefNodeDao = mock(ChefNodeDao.class);
        client = mock(HttpClient.class);
        propertiesProvider = mock(SystemPropertiesProvider.class);
        httpsClient = mock(HttpsClient.class);

        nodeManager = new NodeManagerImpl();
        nodeManager.setChefClientDao(chefClientDao);
        nodeManager.setChefNodeDao(chefNodeDao);
        nodeManager.setProductInstanceDao(productInstanceDao);
        nodeManager.setClient(client);
        nodeManager.setHttpsClient(httpsClient);
        OpenStackRegion openStackRegion = mock(OpenStackRegion.class);
        nodeManager.setOpenStackRegion(openStackRegion);
        when(openStackRegion.getChefServerEndPoint()).thenReturn("http://");

    }

    @Test
    public void deleteNodeTestOK() throws NodeExecutionException, CanNotCallChefException, EntityNotFoundException,
            ClientProtocolException, IOException, KeyManagementException, NoSuchAlgorithmException {

        when(chefNodeDao.loadNode("testOk", "token")).thenReturn(new ChefNode());

        List<ProductInstance> productInstances = new ArrayList<ProductInstance>();
        productInstances.add(new ProductInstance());

        when(productInstanceDao.findByHostname(anyString())).thenReturn(productInstances);

        when(
                httpsClient.doHttps(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
                        (Map<String, String>) Mockito.anyObject())).thenReturn(200);

        when(propertiesProvider.getProperty(anyString())).thenReturn("URL");

        nodeManager.nodeDelete("test", "testOk", "token");

        verify(httpsClient, times(1)).doHttps(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
                (Map<String, String>) Mockito.anyObject());
        // verify(client, times(1)).execute((HttpUriRequest) anyObject());
    }

    /**
     * It tests loading the client
     * 
     * @throws Exception
     */
    @Test
    public void testChefClientLoad() throws Exception {
        when(chefNodeDao.loadNode("testOk", "token")).thenReturn(new ChefNode());

        List<ProductInstance> productInstances = new ArrayList<ProductInstance>();
        productInstances.add(new ProductInstance());

        when(productInstanceDao.findByHostname(anyString())).thenReturn(productInstances);

        when(
                httpsClient.doHttps(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
                        (Map<String, String>) Mockito.anyObject())).thenReturn(200);

        when(propertiesProvider.getProperty(anyString())).thenReturn("URL");

        nodeManager.chefClientload("dd", "token");

    }

    /**
     * It test an error in loading node.
     * 
     * @throws Exception
     */
    public void testChefClientLoadError() throws Exception {
        when(chefNodeDao.loadNode("testerrpr", "token")).thenThrow(new SdcRuntimeException());
        nodeManager.chefClientload("dd", "token");

    }

    /**
     * It test loading hostname node.
     * 
     * @throws Exception
     */
    @Test
    public void testChefHostnameClientLoad() throws Exception {
        when(chefNodeDao.loadNodeFromHostname("testOk", "token")).thenReturn(new ChefNode());

        List<ProductInstance> productInstances = new ArrayList<ProductInstance>();
        productInstances.add(new ProductInstance());

        when(propertiesProvider.getProperty(anyString())).thenReturn("URL");

        nodeManager.chefClientfindByHostname("dd", "token");

    }

    /**
     * It test loading hostname node.
     * 
     * @throws Exception
     */
    @Test
    public void testChefHostnameClientLoadError() throws Exception {
        when(chefNodeDao.loadNodeFromHostname("testOk", "token")).thenThrow(new SdcRuntimeException());

        List<ProductInstance> productInstances = new ArrayList<ProductInstance>();
        productInstances.add(new ProductInstance());

        when(productInstanceDao.findByHostname(anyString())).thenReturn(productInstances);

        when(propertiesProvider.getProperty(anyString())).thenReturn("URL");

        nodeManager.chefClientfindByHostname("dd", "token");

    }

    @Test
    public void deleteNodeTestOK_nodeNotFountInPuppet() throws NodeExecutionException, CanNotCallChefException,
            EntityNotFoundException, ClientProtocolException, IOException, KeyManagementException,
            NoSuchAlgorithmException {

        when(chefNodeDao.loadNode("testOk", "token")).thenReturn(new ChefNode());

        List<ProductInstance> productInstances = new ArrayList<ProductInstance>();
        productInstances.add(new ProductInstance());

        when(productInstanceDao.findByHostname(anyString())).thenReturn(productInstances);

        when(
                httpsClient.doHttps(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
                        (Map<String, String>) Mockito.anyObject())).thenReturn(404);

        when(propertiesProvider.getProperty(anyString())).thenReturn("URL");

        nodeManager.nodeDelete("test", "testOk", "token");

        verify(httpsClient, times(1)).doHttps(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
                (Map<String, String>) Mockito.anyObject());
    }

    @Test
    public void deleteNodeTestEntityNotFound_chef() throws EntityNotFoundException, ClientProtocolException,
            IOException, NodeExecutionException, KeyManagementException, NoSuchAlgorithmException {

        when(
                httpsClient.doHttps(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
                        (Map<String, String>) Mockito.anyObject())).thenReturn(200);

        when(propertiesProvider.getProperty(anyString())).thenReturn("URL");

        List<ProductInstance> productInstances = new ArrayList<ProductInstance>();
        productInstances.add(new ProductInstance());

        when(productInstanceDao.findByHostname(anyString())).thenThrow(EntityNotFoundException.class);

        nodeManager.nodeDelete("test", "testError", "token");

        verify(httpsClient, times(1)).doHttps(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
                (Map<String, String>) Mockito.anyObject());

    }

    @Test
    public void deleteNodeTestNodeException_chef_1() throws NodeExecutionException, CanNotCallChefException,
            EntityNotFoundException, IOException, KeyManagementException, NoSuchAlgorithmException {

        when(chefNodeDao.loadNode("testError", "token")).thenThrow(CanNotCallChefException.class);
        when(chefNodeDao.loadNode("testOk", "token")).thenReturn(new ChefNode());

        List<ProductInstance> productInstances = new ArrayList<ProductInstance>();
        productInstances.add(new ProductInstance());

        when(productInstanceDao.findByHostname(anyString())).thenReturn(productInstances);

        when(
                httpsClient.doHttps(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
                        (Map<String, String>) Mockito.anyObject())).thenReturn(200);

        when(propertiesProvider.getProperty(anyString())).thenReturn("URL");

        nodeManager.nodeDelete("test", "testError", "token");

        verify(httpsClient, times(1)).doHttps(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
                (Map<String, String>) Mockito.anyObject());

    }

    @Test(expected = NodeExecutionException.class)
    public void deleteNodeTestNodeException_chef_2() throws NodeExecutionException, Exception {

        when(chefNodeDao.loadNode("testError", "token")).thenThrow(Exception.class);

        HttpResponse httpResponse = mock(HttpResponse.class);
        StatusLine statusLine = mock(StatusLine.class);
        when(httpResponse.getStatusLine()).thenReturn(statusLine);
        when(httpResponse.getStatusLine().getStatusCode()).thenReturn(200);
        when(client.execute((HttpUriRequest) anyObject())).thenReturn(httpResponse);

        when(propertiesProvider.getProperty(anyString())).thenReturn("URL");

        nodeManager.nodeDelete("test", "testError", "token");

    }

    /**
     * It test the deletion of a node in puppet when there is an error.
     * 
     * @throws NodeExecutionException
     * @throws Exception
     */
    @Test(expected = NodeExecutionException.class)
    public void deleteNodeTestNode_errorpuppet1() throws Exception {

        HttpResponse httpResponse = mock(HttpResponse.class);
        StatusLine statusLine = mock(StatusLine.class);
        when(httpResponse.getStatusLine()).thenReturn(statusLine);
        when(httpResponse.getStatusLine().getStatusCode()).thenReturn(500);
        when(client.execute((HttpUriRequest) anyObject())).thenReturn(httpResponse);
        when(chefNodeDao.loadNode(anyString(), anyString())).thenThrow(CanNotCallChefException.class);
        when(propertiesProvider.getProperty(anyString())).thenReturn("URL");
        nodeManager.nodeDelete("test", "testError", "token");

    }

    /**
     * It test the deletion of a node in puppet when there is an error.
     * 
     * @throws NodeExecutionException
     * @throws Exception
     */
    @Test(expected = NodeExecutionException.class)
    public void deleteNodeTestNodeException_puppet_2() throws NodeExecutionException, Exception {
        when(client.execute((HttpUriRequest) anyObject())).thenThrow(IOException.class);
        when(chefNodeDao.loadNode(anyString(), anyString())).thenThrow(CanNotCallChefException.class);
        when(propertiesProvider.getProperty(anyString())).thenReturn("URL");

        nodeManager.nodeDelete("test", "testError", "token");

    }

    /**
     * It tests delete the node.
     * 
     * @throws Exception
     */
    @Test
    public void testDeleteProductInstances() throws Exception {
        List<ProductInstance> lProdutInstance = new ArrayList();
        lProdutInstance.add(new ProductInstance());
        when(productInstanceDao.findByHostname(anyString())).thenReturn(lProdutInstance);
        nodeManager.deleteProductsInNode("node");
        verify(productInstanceDao).remove(any(ProductInstance.class));
    }

    /**
     * It test the deletion of a node in puppet when there is an error.
     * 
     * @throws NodeExecutionException
     * @throws Exception
     */
    @Test(expected = NodeExecutionException.class)
    public void deleteNodeTestNodeException_puppet_3() throws NodeExecutionException, Exception {

        HttpResponse httpResponse = mock(HttpResponse.class);
        when(client.execute((HttpUriRequest) anyObject())).thenThrow(IllegalStateException.class);
        when(chefNodeDao.loadNode(anyString(), anyString())).thenThrow(CanNotCallChefException.class);
        when(propertiesProvider.getProperty(anyString())).thenReturn("URL");

        nodeManager.nodeDelete("vdc", "node", "token");

    }

}
