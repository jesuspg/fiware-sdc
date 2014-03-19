package com.telefonica.euro_iaas.sdc.pupperwrapper.services.tests;

import com.telefonica.euro_iaas.sdc.puppetwrapper.services.CatalogManager;
import com.telefonica.euro_iaas.sdc.puppetwrapper.services.FileAccessService;
import com.telefonica.euro_iaas.sdc.puppetwrapper.services.impl.ActionsServiceImpl;
import com.telefonica.euro_iaas.sdc.puppetwrapper.services.impl.ProcessBuilderFactory;

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
    
    public void setProcessBuilderFactory(ProcessBuilderFactory processBuilderFactory){
        this.processBuilderFactory=processBuilderFactory;
    }

}
