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
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.HttpMethod;

import org.hibernate.context.TenantIdentifierMismatchException;

/**
 * Handles HTTPS communtications
 * 
 *
 */
public class HttpsClient {

    public static final String HEADER_AUTH = "X-Auth-Token";
    public static final String HEADER_TENNANT = "Tenant-Id";

    private HttpsURLConnection connectionSetup(URL url) throws NoSuchAlgorithmException, KeyManagementException,
            IOException {
        // Install the all-trusting trust manager
        SSLContext sslctx = SSLContext.getInstance("SSL");
        javax.net.ssl.TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        } };
        sslctx.init(null, trustAllCerts, null);

        HttpsURLConnection.setDefaultSSLSocketFactory(sslctx.getSocketFactory());
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
        // Install the all-trusting host verifier
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

        return con;

    }

    public int doHttps(String method, String _url, String payload, Map<String, String> headers)
            throws KeyManagementException, NoSuchAlgorithmException, IOException {
        URL url = new URL(_url);
        HttpsURLConnection con = connectionSetup(url);

        if (HttpMethod.POST.equals(method)) {
            con.setRequestMethod("POST");
        } else if (HttpMethod.GET.equals(method)) {
            con.setRequestMethod("GET");
        } else if (HttpMethod.DELETE.equals(method)) {
            con.setRequestMethod("DELETE");
        }

        con.setRequestProperty(HEADER_TENNANT, headers.get(HEADER_TENNANT));
        con.setRequestProperty(HEADER_AUTH, headers.get(HEADER_AUTH));
        con.setRequestProperty("Content-Type", "application/json");

        con.setDoOutput(true);
        PrintStream ps = new PrintStream(con.getOutputStream());
        ps.println(payload);
        ps.close();
        con.connect();
        StringBuilder sb = new StringBuilder();
        if (con.getResponseCode() == HttpsURLConnection.HTTP_OK) {
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line;

            while ((line = br.readLine()) != null) {
                System.out.println(line);
                sb.append(line);
            }
            br.close();
        }
        int responseStatus = con.getResponseCode();
        con.disconnect();
        return responseStatus;

    }

}
