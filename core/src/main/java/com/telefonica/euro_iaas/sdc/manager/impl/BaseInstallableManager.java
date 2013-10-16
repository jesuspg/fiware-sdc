/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.sdc.manager.impl;

import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.CHEF_DIRECTORY_COOKBOOK;
import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.DELETE_RECIPES_SCRIPT;
import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.UNTAR_COMMAND;
import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.UPLOAD_RECIPES_SCRIPT;
import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.WEBDAV_BASE_URL;
import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.WEBDAV_FILE_URL;

import java.io.File;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.googlecode.sardine.util.SardineException;
import com.telefonica.euro_iaas.sdc.dao.FileDao;
import com.telefonica.euro_iaas.sdc.exception.SdcRuntimeException;
import com.telefonica.euro_iaas.sdc.exception.ShellCommandException;
import com.telefonica.euro_iaas.sdc.model.dto.ReleaseDto;
import com.telefonica.euro_iaas.sdc.util.CommandExecutor;
import com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider;

public class BaseInstallableManager {

    private CommandExecutor commandExecutor;
    protected SystemPropertiesProvider propertiesProvider;
    private FileDao fileDao;

    private String INSTALLABLE_NOT_FOUND = "404";
    private static Logger LOGGER = Logger.getLogger("BaseInstallableManager");

    // *************** METHODS RELATED TO CHEF SERVER ******************
    protected void uploadRecipe(File cookbook, String name) {
        String untarCommand = MessageFormat.format(propertiesProvider.getProperty(UNTAR_COMMAND),
                cookbook.getAbsolutePath(), propertiesProvider.getProperty(CHEF_DIRECTORY_COOKBOOK));
        try {
            LOGGER.log(Level.INFO, "untarCommand : " + untarCommand);
            commandExecutor.executeCommand(untarCommand);

            String uploadRecipeCommand = MessageFormat.format(propertiesProvider.getProperty(UPLOAD_RECIPES_SCRIPT),
                    name);

            LOGGER.log(Level.INFO, "uploadRecipeCommand : " + uploadRecipeCommand);
            commandExecutor.executeCommand(uploadRecipeCommand);
            LOGGER.log(Level.INFO, "Recipe UPLOADED ");

        } catch (ShellCommandException e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
            throw new SdcRuntimeException(e);
        }
        cookbook.deleteOnExit();

    }

    protected void deleteRecipe(String name, String version) {

        String deleteRecipeCommand = MessageFormat.format(propertiesProvider.getProperty(DELETE_RECIPES_SCRIPT), name,
                version);

        try {
            LOGGER.log(Level.INFO, "deleteRecipeCommand : " + deleteRecipeCommand);
            commandExecutor.executeCommand(deleteRecipeCommand);
            LOGGER.log(Level.INFO, "Recipe DELETED ");
        } catch (ShellCommandException e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
            throw new SdcRuntimeException(e);
        }
    }

    // **** FILE UPLOAD TO WEBDAV *****//

    protected void uploadInstallable(File installable, ReleaseDto releaseDto) {

        String webdavFileUrl = MessageFormat.format(propertiesProvider.getProperty(WEBDAV_FILE_URL),
                propertiesProvider.getProperty(WEBDAV_BASE_URL), releaseDto.getType(), releaseDto.getName(),
                releaseDto.getVersion());

        System.out.println(webdavFileUrl);

        createWebDavDirectoryStructure(releaseDto);
        try {
            fileDao.insertFile(webdavFileUrl, installable);
        } catch (SardineException e) {
            throw new SdcRuntimeException(e);
        } finally {
            installable.deleteOnExit();
        }
    }

    private void createWebDavDirectoryStructure(ReleaseDto releaseDto) {

        LOGGER.log(Level.INFO, propertiesProvider.getProperty(WEBDAV_BASE_URL) + "/" + releaseDto.getType() + "/"
                + releaseDto.getName() + "/");
        try {
            if (!fileDao.directoryExists(propertiesProvider.getProperty(WEBDAV_BASE_URL) + "/" + releaseDto.getType()
                    + "/", propertiesProvider.getProperty(WEBDAV_BASE_URL) + "/" + releaseDto.getType() + "/"
                    + releaseDto.getName() + "/"))

                fileDao.createDirectory(propertiesProvider.getProperty(WEBDAV_BASE_URL) + "/" + releaseDto.getType()
                        + "/" + releaseDto.getName());
            else
                LOGGER.log(Level.INFO, "Directory " + propertiesProvider.getProperty(WEBDAV_BASE_URL) + "/"
                        + releaseDto.getType() + "/" + releaseDto.getName() + "/" + " already CREATED");
        } catch (SardineException e) {
            throw new SdcRuntimeException(e);
        }

        try {
            if (!fileDao.directoryExists(propertiesProvider.getProperty(WEBDAV_BASE_URL) + "/" + releaseDto.getType()
                    + "/" + releaseDto.getName() + "/", propertiesProvider.getProperty(WEBDAV_BASE_URL) + "/"
                    + releaseDto.getType() + "/" + releaseDto.getName() + "/" + releaseDto.getVersion() + "/"))
                fileDao.createDirectory(propertiesProvider.getProperty(WEBDAV_BASE_URL) + "/" + releaseDto.getType()
                        + "/" + releaseDto.getName() + "/" + releaseDto.getVersion());
            else
                LOGGER.log(Level.INFO, "Directory " + propertiesProvider.getProperty(WEBDAV_BASE_URL) + "/"
                        + releaseDto.getType() + "/" + releaseDto.getName() + "/" + releaseDto.getVersion()
                        + " already CREATED");
        } catch (SardineException e) {
            throw new SdcRuntimeException(e);
        }
    }

    protected void deleteInstallable(ReleaseDto releaseDto) {
        String webdavFileUrl = MessageFormat.format(propertiesProvider.getProperty(WEBDAV_FILE_URL),
                propertiesProvider.getProperty(WEBDAV_BASE_URL), releaseDto.getType(), releaseDto.getName(),
                releaseDto.getVersion());

        try {
            if (fileDao.directoryExists(propertiesProvider.getProperty(WEBDAV_BASE_URL) + "/" + releaseDto.getType()
                    + "/" + releaseDto.getName() + "/" + releaseDto.getVersion() + "/", webdavFileUrl))

                fileDao.delete(webdavFileUrl);
            else
                LOGGER.log(Level.INFO,
                        "File " + propertiesProvider.getProperty(WEBDAV_BASE_URL) + "/" + releaseDto.getType() + "/"
                                + releaseDto.getName() + "/" + " does NOT exist");
        } catch (SardineException e) {
            if (String.valueOf(e.getStatusCode()).equals(INSTALLABLE_NOT_FOUND)) {
                LOGGER.log(Level.INFO, webdavFileUrl + " does not EXIST");
            } else
                throw new SdcRuntimeException(e);
        }

        deleteWebDavDirectoryStructure(releaseDto);
    }

    private void deleteWebDavDirectoryStructure(ReleaseDto releaseDto) {

        try {
            if (fileDao.directoryExists(propertiesProvider.getProperty(WEBDAV_BASE_URL) + "/" + releaseDto.getType()
                    + "/" + releaseDto.getName() + "/", propertiesProvider.getProperty(WEBDAV_BASE_URL) + "/"
                    + releaseDto.getType() + "/" + releaseDto.getName() + "/" + releaseDto.getVersion() + "/"))

                fileDao.delete(propertiesProvider.getProperty(WEBDAV_BASE_URL) + "/" + releaseDto.getType() + "/"
                        + releaseDto.getName() + "/" + releaseDto.getVersion() + "/");
            else
                LOGGER.log(Level.INFO, "Directory " + propertiesProvider.getProperty(WEBDAV_BASE_URL) + "/"
                        + releaseDto.getType() + "/" + releaseDto.getName() + "/" + releaseDto.getVersion() + "/"
                        + " does NOT exist");
        } catch (SardineException e) {
            if (String.valueOf(e.getStatusCode()).equals(INSTALLABLE_NOT_FOUND)) {
                LOGGER.log(Level.INFO, propertiesProvider.getProperty(WEBDAV_BASE_URL) + "/" + releaseDto.getType()
                        + "/" + releaseDto.getName() + "/" + " does not EXIST");
            } else
                throw new SdcRuntimeException(e);
        }

        try {
            if (fileDao.directoryExists(propertiesProvider.getProperty(WEBDAV_BASE_URL) + "/" + releaseDto.getType()
                    + "/", propertiesProvider.getProperty(WEBDAV_BASE_URL) + "/" + releaseDto.getType() + "/"
                    + releaseDto.getName() + "/"))

                fileDao.delete(propertiesProvider.getProperty(WEBDAV_BASE_URL) + "/" + releaseDto.getType() + "/"
                        + releaseDto.getName() + "/");
            else
                LOGGER.log(Level.INFO, "Directory " + propertiesProvider.getProperty(WEBDAV_BASE_URL) + "/"
                        + releaseDto.getType() + "/" + releaseDto.getName() + "/" + "  does NOT exist");
        } catch (SardineException e) {
            throw new SdcRuntimeException(e);
        }
    }

    /**
     * @param propertiesProvider
     *            the propertiesProvider to set
     */
    public void setPropertiesProvider(SystemPropertiesProvider propertiesProvider) {
        this.propertiesProvider = propertiesProvider;
    }

    /**
     * @param commandExecutor
     *            the commandExecutor to set
     */
    public void setCommandExecutor(CommandExecutor commandExecutor) {
        this.commandExecutor = commandExecutor;
    }

    /**
     * @param FileDao
     *            the fileDao to set
     */
    public void setFileDao(FileDao fileDao) {
        this.fileDao = fileDao;
    }
}
