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

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.junit.Before;
import org.junit.Test;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.dao.ChefClientDao;
import com.telefonica.euro_iaas.sdc.dao.ChefNodeDao;
import com.telefonica.euro_iaas.sdc.dao.ProductInstanceDao;
import com.telefonica.euro_iaas.sdc.exception.CanNotCallChefException;
import com.telefonica.euro_iaas.sdc.exception.NodeExecutionException;
import com.telefonica.euro_iaas.sdc.manager.impl.NodeManagerImpl;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.dto.ChefNode;
import com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider;

public class NodeManagerImplTest {

    private ProductInstanceDao productInstanceDao;
    private ChefClientDao chefClientDao;
    private ChefNodeDao chefNodeDao;
    private HttpClient client;
    private SystemPropertiesProvider propertiesProvider;

    private NodeManagerImpl nodeManager;

    @SuppressWarnings("unchecked")
    @Before
    public void setup() throws CanNotCallChefException {

        productInstanceDao = mock(ProductInstanceDao.class);
        chefClientDao = mock(ChefClientDao.class);
        chefNodeDao = mock(ChefNodeDao.class);
        client = mock(HttpClient.class);
        propertiesProvider= mock(SystemPropertiesProvider.class);

        nodeManager = new NodeManagerImpl();
        nodeManager.setChefClientDao(chefClientDao);
        nodeManager.setChefNodeDao(chefNodeDao);
        nodeManager.setProductInstanceDao(productInstanceDao);
        nodeManager.setClient(client);
        nodeManager.setPropertiesProvider(propertiesProvider);

    }

    @Test
    public void deleteNodeTestOK() throws NodeExecutionException, CanNotCallChefException, EntityNotFoundException, ClientProtocolException, IOException {

        when(chefNodeDao.loadNode("testOk")).thenReturn(new ChefNode());

        List<ProductInstance> productInstances = new ArrayList<ProductInstance>();
        productInstances.add(new ProductInstance());

        when(productInstanceDao.findByHostname(anyString())).thenReturn(productInstances);
       
        HttpResponse httpResponse= mock(HttpResponse.class);
        StatusLine statusLine= mock(StatusLine.class);
        when(httpResponse.getStatusLine()).thenReturn(statusLine);
        when(httpResponse.getStatusLine().getStatusCode()).thenReturn(200);
        when(client.execute((HttpUriRequest) anyObject())).thenReturn(httpResponse);
        
        when(propertiesProvider.getProperty(anyString())).thenReturn("URL");

        nodeManager.nodeDelete("test", "testOk");

    }

    @Test
    public void deleteNodeTestEntityNotFound_chef() throws EntityNotFoundException, ClientProtocolException, IOException, NodeExecutionException {
        
        HttpResponse httpResponse= mock(HttpResponse.class);
        StatusLine statusLine= mock(StatusLine.class);
        when(httpResponse.getStatusLine()).thenReturn(statusLine);
        when(httpResponse.getStatusLine().getStatusCode()).thenReturn(200);
        when(client.execute((HttpUriRequest) anyObject())).thenReturn(httpResponse);
        
        when(propertiesProvider.getProperty(anyString())).thenReturn("URL");

        List<ProductInstance> productInstances = new ArrayList<ProductInstance>();
        productInstances.add(new ProductInstance());

        when(productInstanceDao.findByHostname(anyString())).thenThrow(EntityNotFoundException.class);
        
        nodeManager.nodeDelete("test", "testError");

    }

    @Test(expected = NodeExecutionException.class)
    public void deleteNodeTestNodeException_chef_1() throws NodeExecutionException, CanNotCallChefException, EntityNotFoundException, ClientProtocolException, IOException {

        when(chefNodeDao.loadNode("testError")).thenThrow(CanNotCallChefException.class);
        when(chefNodeDao.loadNode("testOk")).thenReturn(new ChefNode());

        List<ProductInstance> productInstances = new ArrayList<ProductInstance>();
        productInstances.add(new ProductInstance());

        when(productInstanceDao.findByHostname(anyString())).thenReturn(productInstances);
        
        HttpResponse httpResponse= mock(HttpResponse.class);
        StatusLine statusLine= mock(StatusLine.class);
        when(httpResponse.getStatusLine()).thenReturn(statusLine);
        when(httpResponse.getStatusLine().getStatusCode()).thenReturn(200);
        when(client.execute((HttpUriRequest) anyObject())).thenReturn(httpResponse);
        
        when(propertiesProvider.getProperty(anyString())).thenReturn("URL");

        nodeManager.nodeDelete("test", "testError");

    }

    @Test(expected = NodeExecutionException.class)
    public void deleteNodeTestNodeException_chef_2() throws NodeExecutionException, Exception {

        when(chefNodeDao.loadNode("testError")).thenThrow(Exception.class);
        
        HttpResponse httpResponse= mock(HttpResponse.class);
        StatusLine statusLine= mock(StatusLine.class);
        when(httpResponse.getStatusLine()).thenReturn(statusLine);
        when(httpResponse.getStatusLine().getStatusCode()).thenReturn(200);
        when(client.execute((HttpUriRequest) anyObject())).thenReturn(httpResponse);
        
        when(propertiesProvider.getProperty(anyString())).thenReturn("URL");

        nodeManager.nodeDelete("test", "testError");

    }
    
    @Test(expected = NodeExecutionException.class)
    public void deleteNodeTestNodeException_puppet_1() throws NodeExecutionException, Exception {

        HttpResponse httpResponse= mock(HttpResponse.class);
        StatusLine statusLine= mock(StatusLine.class);
        when(httpResponse.getStatusLine()).thenReturn(statusLine);
        when(httpResponse.getStatusLine().getStatusCode()).thenReturn(500);
        when(client.execute((HttpUriRequest) anyObject())).thenReturn(httpResponse);
        
        when(propertiesProvider.getProperty(anyString())).thenReturn("URL");

        nodeManager.nodeDelete("test", "testError");

    }
    
    @Test(expected = NodeExecutionException.class)
    public void deleteNodeTestNodeException_puppet_2() throws NodeExecutionException, Exception {

        HttpResponse httpResponse= mock(HttpResponse.class);
        when(client.execute((HttpUriRequest) anyObject())).thenThrow(IOException.class);
        
        when(propertiesProvider.getProperty(anyString())).thenReturn("URL");

        nodeManager.nodeDelete("test", "testError");

    }
    
    @Test(expected = NodeExecutionException.class)
    public void deleteNodeTestNodeException_puppet_3() throws NodeExecutionException, Exception {

        HttpResponse httpResponse= mock(HttpResponse.class);
        when(client.execute((HttpUriRequest) anyObject())).thenThrow(IllegalStateException.class);
        
        when(propertiesProvider.getProperty(anyString())).thenReturn("URL");

        nodeManager.nodeDelete("test", "testError");

    }
    

}
