package com.telefonica.euro_iaas.sdc.pupperwrapper.services.tests;

import org.springframework.data.mongodb.core.MongoTemplate;

import com.telefonica.euro_iaas.sdc.puppetwrapper.services.impl.CatalogManagerMongoImpl;

public class CatalogManagerMongoImpl4Test extends CatalogManagerMongoImpl {
    
    public CatalogManagerMongoImpl4Test(){
        super();
    }
    
    public void setMongoTemplate(MongoTemplate montogemplate){
        this.mongoTemplate=montogemplate;
    }

}
