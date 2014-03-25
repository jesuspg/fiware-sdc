package com.telefonica.euro_iaas.sdc.dao.impl;

import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.CHEF_SERVER_NODES_PATH;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.MediaType;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.properties.PropertiesProvider;
import com.telefonica.euro_iaas.commons.properties.PropertiesProviderMBean;
import com.telefonica.euro_iaas.commons.properties.impl.PropertiesDAO;
import com.telefonica.euro_iaas.commons.properties.impl.PropertiesDAOJPAImpl;
import com.telefonica.euro_iaas.commons.properties.impl.PropertiesProviderFactoryImpl;
import com.telefonica.euro_iaas.commons.properties.impl.PropertiesProviderImpl;
import com.telefonica.euro_iaas.commons.properties.mbeans.MBeanUtils;
import com.telefonica.euro_iaas.sdc.exception.CanNotCallChefException;
import com.telefonica.euro_iaas.sdc.exception.SdcRuntimeException;
import com.telefonica.euro_iaas.sdc.model.dto.ChefNode;
import com.telefonica.euro_iaas.sdc.util.MixlibAuthenticationDigester;
import com.telefonica.euro_iaas.sdc.util.MixlibAuthenticationDigesterImpl;
import com.telefonica.euro_iaas.sdc.util.RSASignerImpl;
import com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider;
import com.telefonica.euro_iaas.sdc.util.SystemPropertiesProviderImpl;

/**
 * Unitary tests for ChefNodeDaoRestImpl.
 * @author jesus.movilla
 *
 */
public class ChefTest {
    
    /**
     * Testing loadNode method.
     * @throws CanNotCallChefException
     * @throws IOException 
     * @throws EntityNotFoundException 
     */
    @Test
    public void shouldLoadNode() throws CanNotCallChefException, IOException, EntityNotFoundException {
    	
    	
 
        // given
        ChefNodeDaoRestImpl chefNodeDaoRestImpl = new ChefNodeDaoRestImpl();
        ChefClientDaoRestImpl chefClientDaoRestImpl = new ChefClientDaoRestImpl();
        String chefNodeName = "veojob-web.novalocal";
      
        RSASignerImpl signer = new RSASignerImpl ();
        MixlibAuthenticationDigesterImpl mixlibAuthenticationDigester = new MixlibAuthenticationDigesterImpl ();
        mixlibAuthenticationDigester.setSigner(signer);
        Client client = new Client ();

        chefNodeDaoRestImpl.setPropertiesProvider(null);
        chefNodeDaoRestImpl.setDigester(mixlibAuthenticationDigester);
     //   chefNodeDaoRestImpl.setClient(client);
        chefClientDaoRestImpl.setClient(client);
        chefClientDaoRestImpl.setDigester(mixlibAuthenticationDigester);
        
      //  chefClientDaoRestImpl.getChefClient("d22d34d-tiername45-1");
       // chefClientDaoRestImpl.deleteChefClient("d22d34d-tiername45-1");
        
      //  ChefNode createdChefNode = chefNodeDaoRestImpl.loadNode(chefNodeName);
     
     /*   PropertiesDAOJPAImpl dao = new PropertiesDAOJPAImpl();

        

     
        
            SystemPropertiesProviderImpl spropertiesProvider = new SystemPropertiesProviderImpl ();
            spropertiesProvider.setNamespace("/SystemConfiguration.properties");
            PropertiesProviderImpl propertiesProvider = new PropertiesProviderImpl (dao);
            spropertiesProvider.setPropertiesProvider(propertiesProvider);
            spropertiesProvider.loadProperties();
        chefNodeDaoRestImpl.setPropertiesProvider(spropertiesProvider);*/
       
       
               
       // ChefNode createdChefNode = chefNodeDaoRestImpl.loadNode(chefNodeName);

        // then
        //assertNotNull(createdChefNode);
    }
    
   
}
