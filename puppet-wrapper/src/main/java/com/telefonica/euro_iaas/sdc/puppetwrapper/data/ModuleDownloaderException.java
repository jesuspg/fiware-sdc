package com.telefonica.euro_iaas.sdc.puppetwrapper.data;

import java.io.IOException;

/**
 * Exception thrown when our Puppet rest API returns unexpected results.
 * 
 * @author Alberts
 */
@SuppressWarnings("serial")
public class ModuleDownloaderException extends Exception {
    public ModuleDownloaderException() {
        super();
    }

    public ModuleDownloaderException(String msg) {
        super(msg);
    }

    public ModuleDownloaderException(Throwable e) {
        super(e);
    }

    public ModuleDownloaderException(String msg, Throwable e) {
        super(msg, e);
    }

}
