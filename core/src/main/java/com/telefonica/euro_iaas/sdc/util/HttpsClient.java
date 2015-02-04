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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.ws.rs.HttpMethod;

/**
 * Handles HTTPS communtications
 * 
 *
 */
public class HttpsClient {

    public static final String HEADER_AUTH = "X-Auth-Token";
    public static final String HEADER_TENNANT = "Tenant-Id";
    
    private ConnectionSetup connectionSetup;
    

    public int doHttps(String method, String _url, String payload, Map<String, String> headers)
            throws KeyManagementException, NoSuchAlgorithmException, IOException {
        URL url = new URL(_url);
        HttpURLConnection con = connectionSetup.createConnection(url);
        
        con.setRequestProperty(HEADER_TENNANT, headers.get(HEADER_TENNANT));
        con.setRequestProperty(HEADER_AUTH, headers.get(HEADER_AUTH));
        con.setRequestProperty("Content-Type", "application/json");

        if (HttpMethod.POST.equals(method)) {
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            PrintStream ps = new PrintStream(con.getOutputStream());
            ps.println(payload);
            ps.close();
        } else if (HttpMethod.GET.equals(method)) {
            con.setRequestMethod("GET");
        } else if (HttpMethod.DELETE.equals(method)) {
            con.setRequestMethod("DELETE");
        }

        
        
        con.connect();
        StringBuilder sb = new StringBuilder();
        int responseStatus = con.getResponseCode();
        if (responseStatus == HttpsURLConnection.HTTP_OK) {
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line;

            while ((line = br.readLine()) != null) {
                System.out.println(line);
                sb.append(line);
            }
            br.close();
        }
        con.disconnect();
        return responseStatus;

    }


    public void setConnectionSetup(ConnectionSetup connectionSetup) {
        this.connectionSetup = connectionSetup;
    }

}
