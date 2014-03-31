package com.telefonica.euro_iaas.sdc.puppetwrapper.services;

import com.telefonica.euro_iaas.sdc.puppetwrapper.data.ModuleDownloaderException;

public interface ModuleDownloader {
    
    public void download(String url, String nodeName) throws ModuleDownloaderException;

}
