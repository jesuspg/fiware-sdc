package com.telefonica.euro_iaas.sdc.puppetwrapper.services.impl;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.telefonica.euro_iaas.sdc.puppetwrapper.data.ModuleDownloaderException;
import com.telefonica.euro_iaas.sdc.puppetwrapper.services.ModuleDownloader;


@Service("gitCloneService")
public class GitCloneServiceImpl implements ModuleDownloader {

    private static final Log logger = LogFactory.getLog(GitCloneServiceImpl.class);
    
    private String modulesCodeDownloadPath;
    
    public void download(String url,String moduleName) throws ModuleDownloaderException {
        // prepare a new folder for the cloned repository
        File localPath =  new File(modulesCodeDownloadPath+moduleName);
        localPath.delete();
        
        try {
            FileUtils.deleteDirectory(localPath);
        } catch (IOException e) {
            throw new ModuleDownloaderException(e);
        }

        // then clone
        logger.debug("Cloning from " + url + " to " + localPath);
        try {
            Git.cloneRepository()
                    .setURI(url)
                    .setDirectory(localPath)
                    .call();
        } catch (InvalidRemoteException e) {
            throw new ModuleDownloaderException(e);
        } catch (TransportException e) {
            throw new ModuleDownloaderException(e);
        } catch (GitAPIException e) {
            throw new ModuleDownloaderException(e);
        }

        // now open the created repository
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        Repository repository;
        try {
            repository = builder.setGitDir(localPath)
                    .readEnvironment() // scan environment GIT_* variables
                    .findGitDir() // scan up the file system tree
                    .build();
        } catch (IOException e) {
            throw new ModuleDownloaderException(e);
        }

        logger.debug("Having repository: " + repository.getDirectory());

        repository.close();
    }
    
    @Value(value = "${modulesCodeDownloadPath}")
    public void setModulesCodeDownloadPath(String modulesCodeDownloadPath) {
        this.modulesCodeDownloadPath = modulesCodeDownloadPath;
    }
}

