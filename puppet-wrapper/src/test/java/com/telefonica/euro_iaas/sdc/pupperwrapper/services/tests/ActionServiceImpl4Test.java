package com.telefonica.euro_iaas.sdc.pupperwrapper.services.tests;

import com.telefonica.euro_iaas.sdc.puppetwrapper.services.CatalogManager;
import com.telefonica.euro_iaas.sdc.puppetwrapper.services.FileAccessService;
import com.telefonica.euro_iaas.sdc.puppetwrapper.services.impl.ActionsServiceImpl;

public class ActionServiceImpl4Test extends ActionsServiceImpl {
    
    public ActionServiceImpl4Test(){
        super();
    }
    
    public void setCatalogManager(CatalogManager catalogManager){
        this.catalogManager=catalogManager;
    }
    
    public void setFileAccessService(FileAccessService fileAccessService){
        this.fileAccessService=fileAccessService;
    }

}
