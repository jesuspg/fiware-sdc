package com.telefonica.euro_iaas.sdc.rest.resources;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.sun.jersey.api.core.InjectParam;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.dao.OSDao;
import com.telefonica.euro_iaas.sdc.exception.SdcRuntimeException;
import com.telefonica.euro_iaas.sdc.exception.UnableToStartOSException;
import com.telefonica.euro_iaas.sdc.manager.OSInstanceManager;
import com.telefonica.euro_iaas.sdc.model.OS;
import com.telefonica.euro_iaas.sdc.model.OSInstance;
import com.telefonica.euro_iaas.sdc.model.dto.VM;
import com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider;
import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.*;

/**
 * Default OSInstanceResoucer implementation
 *
 * @author Sergio Arroyo
 *
 */
@Path("/os")
@Component
@Scope("request")
public class OSInstanceResourceImpl implements OSInstanceResource {
    @InjectParam("osInstanceManager")
    private OSInstanceManager osInstanceManager;

    @InjectParam("osDao")
    private OSDao osDao;

    @InjectParam("systemPropertiesProvider")
    private SystemPropertiesProvider propertiesProvider;

    /**
     * {@inheritDoc}
     */
    @Override
    public OSInstance bootOS(String osName, VM host) {
        OS os;
        try {
            if (host == null) {
                String sufix = new SimpleDateFormat("-yyMMddHHmmss")
                        .format(new Date());
                host = new VM(osName + sufix,
                        propertiesProvider.getProperty(DEFAULT_HOST_DOMAIN));

            }
            os = osDao.load(osName);
            return osInstanceManager.startAndRunning(os, host);
        } catch (UnableToStartOSException e) {
            throw new SdcRuntimeException(e);
        } catch (EntityNotFoundException e) {
            throw new SdcRuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OSInstance freezeOS(Long osId) {
        try {
            return osInstanceManager.freeze(osInstanceManager.load(osId));
        } catch (EntityNotFoundException e) {
            throw new SdcRuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<OSInstance> findAll() {
        return osInstanceManager.findAll();
    }

    @Override
    public OSInstance load(Long id) throws EntityNotFoundException {
        return osInstanceManager.load(id);
    }

}
