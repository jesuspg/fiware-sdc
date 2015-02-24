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

import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.ws.rs.HttpMethod;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class HttpsClientTest {

    private HttpsClient httpsClient;
    private HttpsURLConnection httpsURLConnection;
    private Map<String, String> headers = new HashMap<String, String>();
    private ConnectionSetup connectionSetup;

    @Before
    public void setup() throws KeyManagementException, NoSuchAlgorithmException, IOException {
        httpsURLConnection = mock(HttpsURLConnection.class);
        connectionSetup = mock(ConnectionSetup.class);
        headers.put(HttpsClient.HEADER_AUTH, "");
        headers.put(HttpsClient.HEADER_TENNANT, "");
        
        when(connectionSetup.createConnection((URL)anyObject())).thenReturn(httpsURLConnection);
        
        String source = "test";
        InputStream in = IOUtils.toInputStream(source, "UTF-8");
        when(httpsURLConnection.getInputStream()).thenReturn(in);
        
        OutputStream os = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
            }
        };
        when(httpsURLConnection.getOutputStream()).thenReturn(os);
        
        
        httpsClient = new HttpsClient();
        httpsClient.setConnectionSetup(connectionSetup);

    }

    @Test
    public void testOk() throws IOException, KeyManagementException, NoSuchAlgorithmException{
        
        when(httpsURLConnection.getResponseCode()).thenReturn(200);
        
        httpsClient.doHttps(HttpMethod.POST,"https://www.pepe.com", Mockito.anyString(), headers);
        
        verify(httpsURLConnection,times(1)).getResponseCode();
        
    }
    
    @Test
    public void test500() throws IOException, KeyManagementException, NoSuchAlgorithmException{
        
        when(httpsURLConnection.getResponseCode()).thenReturn(500);
        
        int rc=httpsClient.doHttps(HttpMethod.POST,"https://www.pepe.com", Mockito.anyString(), headers);
        
        verify(httpsURLConnection,times(1)).getResponseCode();
        
        assertEquals(500, rc);
        
    }
    
    @Test
    public void testOk_GET() throws IOException, KeyManagementException, NoSuchAlgorithmException{
        
        when(httpsURLConnection.getResponseCode()).thenReturn(200);
        
        httpsClient.doHttps(HttpMethod.GET,"https://www.pepe.com", Mockito.anyString(), headers);
        
        verify(httpsURLConnection,times(1)).getResponseCode();
        verify(httpsURLConnection,times(1)).setRequestMethod("GET");
        
    }
    
    @Test
    public void testOk_DELETE() throws IOException, KeyManagementException, NoSuchAlgorithmException{
        
        when(httpsURLConnection.getResponseCode()).thenReturn(200);
        
        httpsClient.doHttps(HttpMethod.DELETE,"https://www.pepe.com", Mockito.anyString(), headers);
        
        verify(httpsURLConnection,times(1)).getResponseCode();
        verify(httpsURLConnection,times(1)).setRequestMethod("DELETE");
        
    }
}
