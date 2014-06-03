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

package com.telefonica.euro_iaas.sdc.dao.impl;

import static com.telefonica.euro_iaas.sdc.util.Configuration.WEBDAV_PASSWD;
import static com.telefonica.euro_iaas.sdc.util.Configuration.WEBDAV_USERNAME;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.sardine.DavResource;
import com.googlecode.sardine.Sardine;
import com.googlecode.sardine.SardineFactory;
import com.googlecode.sardine.util.SardineException;
import com.sun.jersey.api.client.Client;
import com.telefonica.euro_iaas.sdc.dao.FileDao;
import com.telefonica.euro_iaas.sdc.exception.SdcRuntimeException;


/**
 * Default implementation of FileDao.
 * 
 * @author Jesus M. Movilla
 */

public class FileDaoWebDavImpl implements FileDao {

 //   SystemPropertiesProvider propertiesProvider;
    Client client;
    Sardine sardine;
    private static Logger log = LoggerFactory.getLogger(FileDaoWebDavImpl.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public void createDirectory(String webdavDirectoryUrl) throws SardineException {
        Sardine sardine;
        sardine = SardineFactory.begin(WEBDAV_USERNAME,
                WEBDAV_PASSWD);
        sardine.createDirectory(webdavDirectoryUrl);
        log.info(webdavDirectoryUrl + " CREATED ");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String insertFile(String webdavFileUrl, File installable) throws SardineException {

        sardine = SardineFactory.begin(WEBDAV_USERNAME,
                WEBDAV_PASSWD);
        byte[] data;
        try {
            data = FileUtils.readFileToByteArray(new File(installable.getAbsolutePath()));
            sardine.put(webdavFileUrl, data);
            log.info(webdavFileUrl + " INSERTED ");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new SdcRuntimeException(e);
        }

        return installable.getAbsolutePath();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(String webdavUrl) throws SardineException {

        sardine = SardineFactory.begin(WEBDAV_USERNAME,
                WEBDAV_PASSWD);
        sardine.delete(webdavUrl);
        log.info( webdavUrl + " DELETED ");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean directoryExists(String directoryBase, String webdavUrl) throws SardineException {
        boolean exist = false;
        sardine = SardineFactory.begin(WEBDAV_USERNAME,
                WEBDAV_PASSWD);

        List<DavResource> resources = sardine.getResources(directoryBase);
        for (DavResource res : resources) {
            if (res.getAbsoluteUrl().equals(webdavUrl)) {
                log.info( webdavUrl + " exists in " + directoryBase);
                exist = true;
            }
        }
        return exist;
    }
    /**
     * @param client
     *            the client to set
     */
    public void setClient(Client client) {
        this.client = client;
    }
}
