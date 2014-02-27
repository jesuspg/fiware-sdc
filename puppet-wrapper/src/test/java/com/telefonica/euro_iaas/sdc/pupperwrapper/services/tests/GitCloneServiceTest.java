package com.telefonica.euro_iaas.sdc.pupperwrapper.services.tests;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.telefonica.euro_iaas.sdc.puppetwrapper.data.ModuleDownloaderException;
import com.telefonica.euro_iaas.sdc.puppetwrapper.services.impl.GitCloneServiceImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:testContext.xml" })
@Ignore
public class GitCloneServiceTest {
    
    private GitCloneServiceImpl gitCloneServiceImpl;
    
    @Value("${modulesCodeDownloadPath}")
    private String modulesCodeDownloadPath;
    
    @Before
    public void setUp(){
        gitCloneServiceImpl=new GitCloneServiceImpl();
        gitCloneServiceImpl.setModulesCodeDownloadPath(modulesCodeDownloadPath);
    }
    
    @Test
    public void exportTest() throws ModuleDownloaderException{
        
        gitCloneServiceImpl.download("https://github.com/opscode-cookbooks/powershell.git", "test");
        
    }

}
