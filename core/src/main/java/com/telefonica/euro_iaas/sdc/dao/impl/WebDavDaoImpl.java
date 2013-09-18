package com.telefonica.euro_iaas.sdc.dao.impl;

import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.WEBDAV_PASSWD;
import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.WEBDAV_USERNAME;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.googlecode.sardine.DavResource;
import com.googlecode.sardine.Sardine;
import com.googlecode.sardine.SardineFactory;
import com.googlecode.sardine.util.SardineException;
import com.sun.jersey.api.client.Client;
import com.telefonica.euro_iaas.sdc.dao.WebDavDao;
import com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider;

/**
 * Default implementation of WebDavDao.
 *
 * @author Jesus M. Movilla
 *
 */

public class WebDavDaoImpl implements WebDavDao{

	SystemPropertiesProvider propertiesProvider;
    Client client;
    Sardine sardine;
	
	/**
     * {@inheritDoc}
     */
    @Override
	public void createDirectory(String  webdavDirectoryUrl) throws SardineException{
    	Sardine sardine;
    		sardine = 
				SardineFactory.begin(propertiesProvider.getProperty(WEBDAV_USERNAME), 
					propertiesProvider.getProperty(WEBDAV_PASSWD));
			sardine.createDirectory(webdavDirectoryUrl);
			System.out.println(webdavDirectoryUrl + " CREATED ");
	}
	
    /**
     * {@inheritDoc}
     */
    @Override
	public String insertFile(String webdavFileUrl, File installable) 
    	throws SardineException{
    	
    	sardine = SardineFactory.begin(propertiesProvider.getProperty(WEBDAV_USERNAME), 
   				propertiesProvider.getProperty(WEBDAV_PASSWD));
		byte[] data;
		try {
			data = FileUtils.readFileToByteArray(
					new File(installable.getAbsolutePath()));
	    	sardine.put(webdavFileUrl, data);
	    	System.out.println(webdavFileUrl + " INSERTED ");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
		return  installable.getAbsolutePath();
	}
	
    /**
     * {@inheritDoc}
     */
    @Override
	public void delete(String webdavUrl)throws SardineException {
    	
    	sardine = SardineFactory.begin(propertiesProvider.getProperty(WEBDAV_USERNAME), 
		propertiesProvider.getProperty(WEBDAV_PASSWD));
		sardine.delete(webdavUrl);
		System.out.println(webdavUrl + " DELETED ");
	}
    
    /**
     * {@inheritDoc}
     */
    @Override
	public boolean directoryExists (String directoryBase, String webdavUrl)
    throws SardineException {
    	boolean exist = false;
    	sardine = SardineFactory.begin(propertiesProvider.getProperty(WEBDAV_USERNAME), 
				propertiesProvider.getProperty(WEBDAV_PASSWD));
			
		List<DavResource> resources = sardine.getResources(directoryBase);
		for (DavResource res : resources)
		{
		    if (res.getAbsoluteUrl().equals(webdavUrl)){  
		    	System.out.println(webdavUrl + " exists in " +
		    			directoryBase);
		    	exist = true;
		    }
		}
		return exist;
	}
    
    
    /**
     * @param propertiesProvider the propertiesProvider to set
     */
    public void setPropertiesProvider(SystemPropertiesProvider propertiesProvider) {
        this.propertiesProvider = propertiesProvider;
    }

    /**
     * @param client the client to set
     */
    public void setClient(Client client) {
        this.client = client;
    }
}
