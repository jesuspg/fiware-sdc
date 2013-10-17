/**
 *   (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights
 *   Reserved.
 * 
 *   The copyright to the software program(s) is property of Telefonica I+D.
 *   The program(s) may be used and or copied only with the express written
 *   consent of Telefonica I+D or in accordance with the terms and conditions
 *   stipulated in the agreement/contract under which the program(s) have
 *   been supplied.
 */
package com.telefonica.euro_iaas.sdc.dao;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.anyVararg;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.ws.rs.core.MediaType;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
/*import static org.powermock.api.easymock.PowerMock.createMock;
import static org.powermock.api.easymock.PowerMock.mockStatic;
import static org.powermock.api.easymock.PowerMock.mockStaticPartialStrict;
*/

import org.powermock.api.easymock.PowerMock;
import org.powermock.api.mockito.PowerMockito;


import static org.easymock.EasyMock.expect;


import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.telefonica.euro_iaas.sdc.dao.impl.ChefNodeDaoRestImpl;
import com.telefonica.euro_iaas.sdc.model.dto.ChefNode;
import com.telefonica.euro_iaas.sdc.util.MixlibAuthenticationDigester;
import com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider;

import junit.framework.TestCase;

/**
 * Unitary tests for the ChefNodeDao.
 * 
 * @author jesus.movilla
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(IOUtils.class) 
public class ChefNodeDaoRestImplTest extends TestCase{
    
    private ChefNodeDaoRestImpl chefNodeDao;
    
    private SystemPropertiesProvider propertiesProvider;
    private MixlibAuthenticationDigester digester;
    private Client client;
    
    private String hostname = "hostname";
    private String chefNodeName = "hostname.domain";
    
    private String allNodesjsonFilePath = "src/test/resources/AllChefNodes.js";
    private String allNodesjsonFromFile;
    private String nodejsonFilePath = "src/test/resources/Chefnode.js";
    private String nodejsonFromFile;
    private InputStream inputStream;
    
    private WebResource webResource;
    
    @Before
    public void setUp() throws Exception {
        allNodesjsonFromFile = getFile(allNodesjsonFilePath);
        nodejsonFromFile = getFile(nodejsonFilePath);
        
        //propertiesProvider = PowerMock.createMock(SystemPropertiesProvider.class);
        propertiesProvider = PowerMockito.mock(SystemPropertiesProvider.class);
        PowerMockito.when(propertiesProvider.getProperty(any(String.class))).thenReturn("url");
        
        /*propertiesProvider = mock(SystemPropertiesProvider.class);
        when(propertiesProvider.getProperty(any(String.class))).thenReturn("url");
        //expect(propertiesProvider.getProperty(any(String.class))).andReturn("url");*/
        
        /*digester = PowerMock.createMock(MixlibAuthenticationDigester.class);
        client = PowerMock.createMock(Client.class);*/
        
        digester = PowerMockito.mock(MixlibAuthenticationDigester.class);
        client = PowerMockito.mock(Client.class);
        
        /*digester = mock(MixlibAuthenticationDigester.class);
        client = mock(Client.class);*/
        
        /*chefNodeDao = new ChefNodeDaoRestImpl();
        chefNodeDao.setClient(client);
        chefNodeDao.setDigester(digester);
        chefNodeDao.setPropertiesProvider(propertiesProvider);*/
        
        //webResource = PowerMock.createMock (WebResource.class);
        //when(client.resource(any(String.class))).thenReturn(webResource);
        
        webResource = PowerMockito.mock(WebResource.class);
        PowerMockito.when(client.resource(any(String.class))).thenReturn(webResource);
        
        //webResource = mock(WebResource.class);
        //when(client.resource(any(String.class))).thenReturn(webResource);
        //expect(client.resource(any(String.class))).andReturn(webResource);

    }
    
    @Test
    public void testLoadNode() throws Exception {
        WebResource.Builder builder = PowerMockito.mock(WebResource.Builder.class);
        
        inputStream = mock(InputStream.class);
        
        // when
        PowerMockito.when(client.resource(any(String.class))).thenReturn(webResource);
        PowerMockito.when(webResource.accept(MediaType.APPLICATION_XML)).thenReturn(builder);
        
        PowerMockito.when(builder.header(any(String.class), any(Object.class))).thenReturn(builder);
        PowerMockito.when(builder.get(InputStream.class)).thenReturn(inputStream);
        
        /*expect(client.resource(any(String.class))).andReturn(webResource);
        expect(webResource.accept(MediaType.APPLICATION_XML)).andReturn(builder);
        expect(builder.header(any(String.class), any(Object.class))).andReturn(builder);
        */
        PowerMockito.mockStatic(IOUtils.class);
        //mockStaticPartialStrict(IOUtils.class, "toString", InputStream.class);
        //when(IOUtils.toString(builder.get(InputStream.class))).thenReturn(nodejsonFromFile);
        PowerMockito.when(IOUtils.toString(any(InputStream.class))).thenReturn(nodejsonFromFile);
        //PowerMockito.when(IOUtils.toString(builder.get(InputStream.class))).thenReturn(nodejsonFromFile);
        
        //expect(IOUtils.toString(any(InputStream.class))).andReturn(nodejsonFromFile);
        //verifyStatic();
        chefNodeDao = new ChefNodeDaoRestImpl();
        chefNodeDao.setClient(client);
        chefNodeDao.setDigester(digester);
        chefNodeDao.setPropertiesProvider(propertiesProvider);
        ChefNode createdChefNode = chefNodeDao.loadNode(chefNodeName);
        assertEquals(createdChefNode.getName(), chefNodeName);
    }
    
    /*@Test
    public void testLoadNodeFromHostname() throws Exception {
        ChefNode createdChefNode = chefNodeDao.loadNodeFromHostname(hostname);
    }*/
    
    
    private String getFile(String file) throws IOException {
        File f = new File(file);
        System.out.println(f.isFile() + " " + f.getAbsolutePath());

        InputStream dd = new FileInputStream(f);

        BufferedReader reader = new BufferedReader(new InputStreamReader(dd));
        StringBuffer ruleFile = new StringBuffer();
        String actualString;

        while ((actualString = reader.readLine()) != null) {
          ruleFile.append(actualString).append("\n");
        }
        return ruleFile.toString();
      }
}
