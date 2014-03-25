package com.telefonica.euro_iaas.sdc.dao.impl;

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


import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.client.urlconnection.HTTPSProperties;

public class ChefClientConfigImp extends DefaultClientConfig {
	HostnameVerifier hostnameVerifier;
	SSLContext stx;
	
	public ChefClientConfigImp () {
		super();
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
	}
	
	



	public Set<Class<?>> getClasses() {
		// TODO Auto-generated method stub
		return null;
	}


	public boolean getPropertyAsFeature(String featureName) {
		// TODO Auto-generated method stub
		return false;
	}


	public Set<Object> getSingletons() {
		// TODO Auto-generated method stub
		return null;
	}


	public boolean getFeature(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}


	public Map<String, Boolean> getFeatures() {
		// TODO Auto-generated method stub
		return null;
	}


	public Map<String, Object> getProperties() {
		
		super.getProperties().put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES, new HTTPSProperties(hostnameVerifier,stx));
	    
		return super.getProperties();
	}


	public Object getProperty(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	

}
