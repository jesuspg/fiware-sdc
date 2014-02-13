package com.telefonica.euro_iaas.sdc.pupperwrapper.services.tests;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.telefonica.euro_iaas.sdc.puppetwrapper.data.ModuleDownloaderException;
import com.telefonica.euro_iaas.sdc.puppetwrapper.services.impl.SvnExporterServiceImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:testContext.xml" })
public class SvnExportServiceTest {
    
    private SvnExporterServiceImpl svnExporterService;
    
    @Value("${modulesCodeDownloadPath}")
    private String modulesCodeDownloadPath;
    
    @Before
    public void setUp(){
        svnExporterService=new SvnExporterServiceImpl();
        svnExporterService.setModulesCodeDownloadPath(modulesCodeDownloadPath);
    }
    
    @Test
    public void exportTest() throws ModuleDownloaderException{
        
        svnExporterService.download("https://forge.fi-ware.eu/scmrepos/svn/testbed/trunk/cookbooks/GESoftware/beatest", "moduleTest");
        
    }

}
