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

package com.telefonica.euro_iaas.sdc.client.services.impl;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import java.util.Map;
import java.util.Set;


import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.config.DefaultClientConfig;

import com.sun.jersey.client.urlconnection.HTTPSProperties;
import com.telefonica.euro_iaas.sdc.client.services.SdcClientConfig;


public class SdcClientConfigImp extends DefaultClientConfig  implements SdcClientConfig  {
	HostnameVerifier hostnameVerifier;
	SSLContext stx;

	
	public SdcClientConfigImp () {
		super();
		setup ();

	}
	
	private void setup () {
		
		hostnameVerifier =     new HostnameVerifier() {

			public boolean verify(String arg0, SSLSession arg1) {
				return true;
			}
	     };
	     
	    javax.net.ssl.TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager(){
	    	    public X509Certificate[] getAcceptedIssuers(){return null;}
	    	    public void checkClientTrusted(X509Certificate[] certs, String authType){}
	    	    public void checkServerTrusted(X509Certificate[] certs, String authType){}
	    }};
	 

	    try {
	    	stx = SSLContext.getInstance("TLS");
	    	stx.init(null, trustAllCerts, new SecureRandom());
	        HttpsURLConnection.setDefaultSSLSocketFactory(stx.getSocketFactory());
	    } catch (Exception e) {
	        ;
	    }
	    	
	    getProperties().put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES, new HTTPSProperties(hostnameVerifier,stx));
		
	}

	public Set<Class<?>> getClasses() {
		return super.getClasses();
	}


	public boolean getPropertyAsFeature(String featureName) {
		return super.getPropertyAsFeature(featureName);
	}


	public Set<Object> getSingletons() {
		return super.getSingletons();
	}


	public boolean getFeature(String arg0) {
		return super.getFeature(arg0);
	}


	public Map<String, Boolean> getFeatures() {
	     return super.getFeatures();
	}


	public Map<String, Object> getProperties() {

		return super.getProperties();
	}


	public Object getProperty(String arg0) {
		return super.getProperty(arg0);
	}
	
	public Client getClient () {
		return Client.create(this);
	}
	

}
