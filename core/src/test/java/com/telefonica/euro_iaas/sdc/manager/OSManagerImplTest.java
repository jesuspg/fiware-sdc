package com.telefonica.euro_iaas.sdc.manager;

import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.CLONE_IMAGE_SCRIPT;
import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.FREEZE_IMAGE_SCRIPT;
import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.TIME_WAITING_FOR_RUNNING;
import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.WAIT_FOR_RUNNING_SCRIPT;
import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.WEBDAV_BASE_URL;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import junit.framework.TestCase;

import com.telefonica.euro_iaas.sdc.dao.OSInstanceDao;
import com.telefonica.euro_iaas.sdc.exception.ShellCommandException;
import com.telefonica.euro_iaas.sdc.exception.UnableToStartOSException;
import com.telefonica.euro_iaas.sdc.manager.impl.OSInstanceManagerCristianToolsImpl;
import com.telefonica.euro_iaas.sdc.model.OS;
import com.telefonica.euro_iaas.sdc.model.OSInstance;
import com.telefonica.euro_iaas.sdc.model.OSInstance.Status;
import com.telefonica.euro_iaas.sdc.model.dto.VM;
import com.telefonica.euro_iaas.sdc.util.CommandExecutor;
import com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider;
/**
 * OSManager unit test.
 *
 * @author Sergio Arroyo
 *
 */
public class OSManagerImplTest extends TestCase {

    public final static String OS_NAME = "osName";
    public final static String OS_DESCRIPTION = "osDescription";
    public final static String OS_VERSION = "osVersion";

    public final static String HOST_NAME = "hostName";
    public final static String HOST_DOMAIN = "hostDomain";

    public final static String CLONE_COMMAND =
        "/opt/sdc/scripts/newVm.py " + OS_NAME + " " + HOST_NAME;
    public final static String WAIT_COMMAND = "ssh -l root " + HOST_NAME
        + " cat /tmp/status";

    public final static String FREEZE_COMMAND = "/opt/sdc/scripts/haltVM.py "
        + HOST_NAME ;


    private OS os = new OS(OS_NAME, OS_DESCRIPTION, OS_VERSION);
    private VM host = new VM( HOST_NAME, HOST_DOMAIN);


    public void testStartAndRunningWhenEverithingIsOk() throws Exception {
        SystemPropertiesProvider propertiesProvider =
            mock(SystemPropertiesProvider.class);
        when(propertiesProvider.getProperty(CLONE_IMAGE_SCRIPT))
            .thenReturn("/opt/sdc/scripts/newVm.py {0} {1}");
        when(propertiesProvider.getProperty(WAIT_FOR_RUNNING_SCRIPT))
            .thenReturn("ssh -l root {0} cat /tmp/status");
        when(propertiesProvider.getIntProperty(TIME_WAITING_FOR_RUNNING))
            .thenReturn(0);
        OSInstanceDao osInstanceDao = mock(OSInstanceDao.class);
        OSInstance osInstance = new OSInstance(os, Status.INSTALLING, host);
        osInstance.setId(0l);
        when(osInstanceDao.create(any(OSInstance.class))).thenReturn(
                osInstance);

        CommandExecutor commandExecutor = mock(CommandExecutor.class);
        when(commandExecutor.executeCommand(CLONE_COMMAND))
            .thenReturn(new String[2]);
        when(commandExecutor.executeCommand(WAIT_COMMAND))
        .thenReturn(new String[2]);


        OSInstanceManagerCristianToolsImpl manager = new OSInstanceManagerCristianToolsImpl();
        manager.setPropertiesProvider(propertiesProvider);
        manager.setOsInstanceDao(osInstanceDao);
        manager.setCommandExecutor(commandExecutor);

        OSInstance createdInstance = manager.startAndRunning(os, host);
        assertEquals(osInstance, createdInstance);

        verify(propertiesProvider).getProperty(CLONE_IMAGE_SCRIPT);
        verify(propertiesProvider).getProperty(WAIT_FOR_RUNNING_SCRIPT);
        verify(propertiesProvider).getIntProperty(TIME_WAITING_FOR_RUNNING);
        verify(osInstanceDao, times(1)).create(any(OSInstance.class));

        verify(commandExecutor, times(1)).executeCommand(CLONE_COMMAND);
        verify(commandExecutor, times(1)).executeCommand(WAIT_COMMAND);

    }

    public void testStartAndRunningWhenCanNotGetRunning() throws Exception {
        SystemPropertiesProvider propertiesProvider =
            mock(SystemPropertiesProvider.class);
        when(propertiesProvider.getProperty(CLONE_IMAGE_SCRIPT))
            .thenReturn("/opt/sdc/scripts/newVm.py {0} {1}");


        CommandExecutor commandExecutor = mock(CommandExecutor.class);
        when(commandExecutor.executeCommand(CLONE_COMMAND))
            .thenThrow(new ShellCommandException(""));

        OSInstanceManagerCristianToolsImpl manager = new OSInstanceManagerCristianToolsImpl();
        manager.setPropertiesProvider(propertiesProvider);
        manager.setCommandExecutor(commandExecutor);
        try {
            manager.startAndRunning(os, host);
            fail("UnableToStartOSException expected");
        } catch (UnableToStartOSException e) {
            //it's OK
        }
        verify(propertiesProvider).getProperty(CLONE_IMAGE_SCRIPT);
        verify(commandExecutor, times(1)).executeCommand(CLONE_COMMAND);


    }


    public void testFreeze() throws Exception {
        SystemPropertiesProvider propertiesProvider =
            mock(SystemPropertiesProvider.class);
        when(propertiesProvider.getProperty(FREEZE_IMAGE_SCRIPT))
            .thenReturn("/opt/sdc/scripts/haltVM.py {0}");
        when(propertiesProvider.getProperty(WEBDAV_BASE_URL))
        .thenReturn("url {0}");

        CommandExecutor commandExecutor = mock(CommandExecutor.class);
        when(commandExecutor.executeCommand(FREEZE_COMMAND))
            .thenReturn(new String[2]);

        OSInstanceDao osInstanceDao = mock(OSInstanceDao.class);
        OSInstance osInstance = new OSInstance(os, Status.INSTALLING, host);
        osInstance.setId(0l);
        when(osInstanceDao.update(any(OSInstance.class))).thenReturn(
                osInstance);


        OSInstanceManagerCristianToolsImpl manager = new OSInstanceManagerCristianToolsImpl();
        manager.setPropertiesProvider(propertiesProvider);
        manager.setOsInstanceDao(osInstanceDao);
        manager.setCommandExecutor(commandExecutor);
        OSInstance instance = manager.freeze(osInstance);

        assertEquals(osInstance, instance);
        assertEquals("url " +  HOST_NAME, instance.getImageFileLocation());
        assertEquals(Status.STOPPED, instance.getStatus());
        verify(propertiesProvider).getProperty(FREEZE_IMAGE_SCRIPT);
        verify(propertiesProvider).getProperty(WEBDAV_BASE_URL);
        verify(osInstanceDao, times(1)).update(any(OSInstance.class));
        verify(commandExecutor, times(1)).executeCommand(FREEZE_COMMAND);

    }
}
