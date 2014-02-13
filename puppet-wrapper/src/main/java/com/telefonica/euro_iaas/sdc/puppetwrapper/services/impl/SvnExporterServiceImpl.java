package com.telefonica.euro_iaas.sdc.puppetwrapper.services.impl;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import com.telefonica.euro_iaas.sdc.puppetwrapper.services.ModuleDownloader;

@Service("svnExporterService")
public class SvnExporterServiceImpl implements ModuleDownloader {
    
    private static final Log logger = LogFactory.getLog(SvnExporterServiceImpl.class);

    private String modulesCodeDownloadPath;
    
    private String username="";
    private String password="";

    public void download(String url, String moduleName) {
        // final String url = "svn://cavcops01.global.local/skunkworks";
        // final String destPath = "c:/temp/svntest";

        SVNRepository repository = null;

        try {
            if (url.contains("http://") || url.contains("https://")) {
                DAVRepositoryFactory.setup();
            } else if (url.contains("svn://")) {
                SVNRepositoryFactoryImpl.setup();
            }
            // initiate the reporitory from the url
            repository = SVNRepositoryFactory.create(SVNURL.parseURIDecoded(url));
            // create authentication data
            ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(username, password);
            repository.setAuthenticationManager(authManager);
            // output some data to verify connection
            logger.debug("Repository Root: " + repository.getRepositoryRoot(true));
            logger.debug("Repository UUID: " + repository.getRepositoryUUID(true));
            // need to identify latest revision
            long latestRevision = repository.getLatestRevision();
            logger.debug("Repository Latest Revision: " + latestRevision);

            // create client manager and set authentication
            SVNClientManager ourClientManager = SVNClientManager.newInstance();
            ourClientManager.setAuthenticationManager(authManager);
            // use SVNUpdateClient to do the export
            SVNUpdateClient updateClient = ourClientManager.getUpdateClient();
            updateClient.setIgnoreExternals(false);
            updateClient.doExport(repository.getLocation(), new File(modulesCodeDownloadPath+moduleName), SVNRevision.create(latestRevision),
                    SVNRevision.create(latestRevision), null, true, SVNDepth.INFINITY);

        } catch (SVNException e) {
            e.printStackTrace();
        }catch (Exception ex){
            ex.printStackTrace();
        } finally {
            logger.debug("Done");
        }
    }
    
    @Value(value = "${modulesCodeDownloadPath}")
    public void setModulesCodeDownloadPath(String modulesCodeDownloadPath) {
        this.modulesCodeDownloadPath = modulesCodeDownloadPath;
    }
}
