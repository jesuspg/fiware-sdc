package com.telefonica.euro_iaas.sdc.puppetwrapper.services;

import java.io.IOException;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;

public interface GitCloneService {
    
    public void download(String url,String repoName) throws IOException, InvalidRemoteException, TransportException, GitAPIException;

}
