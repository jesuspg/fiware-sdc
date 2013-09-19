package com.telefonica.euro_iaas.sdc.dao;

import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.model.NodeCommand;
import com.telefonica.euro_iaas.sdc.model.OS;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.NodeCommandSearchCriteria;

public class NodeCommandDaoJpaImplTest extends AbstractJpaDaoTest {
	//Need to be revisited
	
    private OSDao soDao;
    private NodeCommandDao nodeCommandDao;

    protected void createSO() throws Exception {
        SODaoJpaImplTest soDaoTest = new SODaoJpaImplTest();
        soDaoTest.setSoDao(soDao);
        soDaoTest.testCreate();
    }

   public void createNodeCommand() throws Exception  {

        NodeCommand nodeCommand = new NodeCommand();
        OS so = new OS("Prueba I", "1", "Prueba I Description","Prueba I Version");

        try{
            so = soDao.load(so.getName());
            System.out.println("The OS " + so.getName()
                    + " already exists");
        } catch (EntityNotFoundException e)
        {
            System.out.println("The Product " +so.getName()
                + " does not exist");
            so = soDao.create(so);
        }

        nodeCommand.setOS(so);
        nodeCommand.setKey("hostname_command");
        nodeCommand.setValue("hostname");

        NodeCommand creatednodeCommand = nodeCommandDao.create(nodeCommand);
        Assert.assertEquals(creatednodeCommand, nodeCommand);

    }

    public void testCreateAndFindByCriteria() throws Exception {
    	
    	System.out.println("testCreateAndFindByCriteria.Start");
    	createNodeCommand();
    	System.out.println("NodeCommand created");
    	
    	NodeCommand nodeCommand = nodeCommandDao.findAll().get(0);  
        

        NodeCommandSearchCriteria criteria =
            new NodeCommandSearchCriteria(nodeCommand.getOS());
        assertEquals(1, nodeCommandDao.findByCriteria(criteria).size());
        //Assert.assertEquals(nodeCommandDao.findByCriteria(criteria), nodeCommand);
    }

    /**
     * @param soDao the soDao to set
     */
    public void setSoDao(OSDao soDao) {
        this.soDao = soDao;
    }

    /**
     * @param nodeCommandDao the nodeCommandDao to set
     */
    public void setNodeCommandDao(NodeCommandDao nodeCommandDao) {
        this.nodeCommandDao = nodeCommandDao;
    }


}

