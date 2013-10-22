package com.telefonica.euro_iaas.sdc.pupperwrapper.services.tests;

import com.telefonica.euro_iaas.sdc.puppetwrapper.services.CatalogManager;
import com.telefonica.euro_iaas.sdc.puppetwrapper.services.impl.FileAccessServiceImpl;

public class FileAccessServiceImpl4Test extends FileAccessServiceImpl {
    
    public FileAccessServiceImpl4Test(){
        super();
    }
    
    public void setCatalogManager(CatalogManager catalogManager){
        this.catalogManager=catalogManager;
    }

}
