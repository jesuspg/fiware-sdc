package com.telefonica.euro_iaas.sdc.dao;



import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.config.ClientConfig;

public interface ChefClientConfig extends ClientConfig {

	Client getClient ();

}
