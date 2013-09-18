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
import com.telefonica.euro_iaas.sdc.exception.UnableToStartOSException;
import com.telefonica.euro_iaas.sdc.manager.impl.OSInstanceManagerCristianToolsImpl;
import com.telefonica.euro_iaas.sdc.model.OS;
import com.telefonica.euro_iaas.sdc.model.OSInstance;
import com.telefonica.euro_iaas.sdc.model.OSInstance.Status;
import com.telefonica.euro_iaas.sdc.model.dto.VM;
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

    public final static String HOST_NAME = "osName";
    public final static String HOST_DOMAIN = "osVersion";


    private OS os = new OS(OS_NAME, OS_DESCRIPTION, OS_VERSION);
    private VM host = new VM( HOST_NAME, HOST_DOMAIN);


    public void testStartAndRunningWhenEverithingIsOk() throws Exception {
        SystemPropertiesProvider propertiesProvider =
            mock(SystemPropertiesProvider.class);
        when(propertiesProvider.getProperty(CLONE_IMAGE_SCRIPT))
            .thenReturn("ls");
        when(propertiesProvider.getProperty(WAIT_FOR_RUNNING_SCRIPT))
            .thenReturn("echo 0");
        when(propertiesProvider.getIntProperty(TIME_WAITING_FOR_RUNNING))
            .thenReturn(0);
        OSInstanceDao osInstanceDao = mock(OSInstanceDao.class);
        OSInstance osInstance = new OSInstance(os, Status.INSTALLING, host);
        osInstance.setId(0l);
        when(osInstanceDao.create(any(OSInstance.class))).thenReturn(
                osInstance);

        OSInstanceManagerCristianToolsImpl manager = new OSInstanceManagerCristianToolsImpl();
        manager.setPropertiesProvider(propertiesProvider);
        manager.setOsInstanceDao(osInstanceDao);
        OSInstance createdInstance = manager.startAndRunning(os, host);
        assertEquals(osInstance, createdInstance);

        verify(propertiesProvider).getProperty(CLONE_IMAGE_SCRIPT);
        verify(propertiesProvider).getProperty(WAIT_FOR_RUNNING_SCRIPT);
        verify(propertiesProvider).getIntProperty(TIME_WAITING_FOR_RUNNING);
        verify(osInstanceDao, times(1)).create(any(OSInstance.class));
    }

    public void testStartAndRunningWhenCanNotGetRunning() throws Exception {
        SystemPropertiesProvider propertiesProvider =
            mock(SystemPropertiesProvider.class);
        when(propertiesProvider.getProperty(CLONE_IMAGE_SCRIPT))
            .thenReturn("asgdses");

        OSInstanceManagerCristianToolsImpl manager = new OSInstanceManagerCristianToolsImpl();
        manager.setPropertiesProvider(propertiesProvider);
        try {
            manager.startAndRunning(os, host);
            fail("UnableToStartOSException expected");
        } catch (UnableToStartOSException e) {
            //it's OK
        }
        verify(propertiesProvider).getProperty(CLONE_IMAGE_SCRIPT);
    }


    public void testFreeze() throws Exception {
        SystemPropertiesProvider propertiesProvider =
            mock(SystemPropertiesProvider.class);
        when(propertiesProvider.getProperty(FREEZE_IMAGE_SCRIPT))
            .thenReturn("ls");
        when(propertiesProvider.getProperty(WEBDAV_BASE_URL))
        .thenReturn("url {0}");
        OSInstanceDao osInstanceDao = mock(OSInstanceDao.class);
        OSInstance osInstance = new OSInstance(os, Status.INSTALLING, host);
        osInstance.setId(0l);
        when(osInstanceDao.update(any(OSInstance.class))).thenReturn(
                osInstance);


        OSInstanceManagerCristianToolsImpl manager = new OSInstanceManagerCristianToolsImpl();
        manager.setPropertiesProvider(propertiesProvider);
        manager.setOsInstanceDao(osInstanceDao);
        OSInstance instance = manager.freeze(osInstance);

        assertEquals(osInstance, instance);
        assertEquals("url " +  HOST_NAME, instance.getImageFileLocation());
        assertEquals(Status.STOPPED, instance.getStatus());
        verify(propertiesProvider).getProperty(FREEZE_IMAGE_SCRIPT);
        verify(propertiesProvider).getProperty(WEBDAV_BASE_URL);
        verify(osInstanceDao, times(1)).update(any(OSInstance.class));

    }
}
