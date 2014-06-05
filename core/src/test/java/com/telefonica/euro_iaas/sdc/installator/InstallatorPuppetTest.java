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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.protocol.HTTP;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.annotation.ExpectedException;

import com.telefonica.euro_iaas.sdc.dao.ChefClientConfig;
import com.telefonica.euro_iaas.sdc.exception.InstallatorException;
import com.telefonica.euro_iaas.sdc.exception.NodeExecutionException;
import com.telefonica.euro_iaas.sdc.exception.OpenStackException;
import com.telefonica.euro_iaas.sdc.installator.impl.InstallatorPuppetImpl;
import com.telefonica.euro_iaas.sdc.keystoneutils.OpenStackRegion;
import com.telefonica.euro_iaas.sdc.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.sdc.model.Metadata;
import com.telefonica.euro_iaas.sdc.model.OS;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
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
    private StatusLine statusLine;
    private OpenStackRegion openStackRegion;
    
    
    @Before
    public void setup() throws ClientProtocolException, IOException, OpenStackException{
        Product product = new Product("testProduct", "description");
        Metadata metadata=new Metadata("installator", "puppet");
        List<Metadata>metadatas = new ArrayList<Metadata>();
        metadatas.add(metadata);
        product.setMetadatas(metadatas);

        host = new VM("fqn", "ip", "testName", "domain");
        
        os = new OS("os1", "1", "os1 description", "v1");
        host.setOsType(os.getOsType());
        
        productRelease = new ProductRelease("version", "releaseNotes", product, Arrays.asList(os), null);
        productInstance = new ProductInstance(productRelease, Status.INSTALLING, host, "vdc");
        
//        client = (HttpClient) new HTTPClientMock();
        
        client = mock(HttpClient.class);
        response=mock(HttpResponse.class);
        statusLine = mock(StatusLine.class);
        openStackRegion = mock (OpenStackRegion.class);

        
        when(client.execute((HttpUriRequest) Mockito.anyObject())).thenReturn(response);
        when(response.getStatusLine()).thenReturn(statusLine);
        
        
        propertiesProvider = mock(SystemPropertiesProvider.class);
        when(propertiesProvider.getProperty("PUPPET_MASTER_URL")).thenReturn(
                "http://130.206.82.190:8080/puppetwrapper/");
        
        puppetInstallator = new InstallatorPuppetImpl();
        puppetInstallator.setClient(client);
        puppetInstallator.setOpenStackRegion(openStackRegion);
        when (openStackRegion.getPuppetWrapperEndPoint("token")).thenReturn("http://");
   
    }
    
    @Test
    public void testCallService_all_OK() throws InstallatorException, NodeExecutionException{
        
        when(statusLine.getStatusCode()).thenReturn(200);
        
        puppetInstallator.callService(host,"test",productRelease, "install", "token");
        
    }
    
    @Test(expected = InstallatorException.class)
    public void testCallService_FAIL() throws InstallatorException, NodeExecutionException{
        
        when(statusLine.getStatusCode()).thenReturn(500);
        
        puppetInstallator.callService(host,"test",productRelease, "install", "token");
        
    }
    
    @Test(expected = InstallatorException.class)
    public void testCallService_1_OK_1_FAIL() throws InstallatorException, NodeExecutionException{
        
        when(statusLine.getStatusCode()).thenReturn(200).thenReturn(500);
        
        puppetInstallator.callService(host,"test",productRelease, "install", "token");
        
    }

}
