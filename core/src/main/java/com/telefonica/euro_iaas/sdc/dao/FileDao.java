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

package com.telefonica.euro_iaas.sdc.dao;

import java.io.File;

import com.googlecode.sardine.util.SardineException;

/**
 * Provides the methods to work with WebDav.
 * 
 * @author Jesus M. Movilla
 */
public interface FileDao {

    /**
     * Create the webdavDirectory directory into the webdav
     * 
     * @param the
     *            webdavDirectoryUrl to be created
     * @return void
     */
    void createDirectory(String webdavDirectoryUrl) throws SardineException;

    /**
     * Insert the File into the specified WebDavDirectory.
     * 
     * @param webdavDirectory
     * @param the
     *            file to be uploaded to the webdav
     * @return the file deleted
     */
    String insertFile(String webdavDirectory, File installable) throws SardineException;

    /**
     * Delete the Resource specified vi webdavUrl.
     * 
     * @param the
     *            URL of the resource to be deleted in webdav (it could be a file or a directory)
     * @return void.
     */
    void delete(String webdavUrl) throws SardineException;

    /**
     * Check if the directory specified by webdavUrle exists
     * 
     * @param the
     *            DirectoryBase to check if a directory exists
     * @param the
     *            directory to be searched
     * @return boolean.
     */
    boolean directoryExists(String directoryBase, String webdavUrl) throws SardineException;
}
