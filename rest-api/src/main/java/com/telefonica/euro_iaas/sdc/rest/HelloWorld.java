package com.telefonica.euro_iaas.sdc.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.sun.jersey.api.core.InjectParam;
import com.telefonica.euro_iaas.commons.dao.AlreadyExistsEntityException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.sdc.dao.SODao;
import com.telefonica.euro_iaas.sdc.model.SO;

// The Java class will be hosted at the URI path "/myresource"
/**
 * <p>HelloWorld class.</p>
 *
 * @author ju
 * @version $Id: $
 */
@Path("/myresource")
@Component
@Scope("request")
public class HelloWorld {

    @InjectParam("soDao")
    SODao soDao;
    // The Java method will process HTTP GET requests
    /**
     * <p>getIt</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @GET
    // The Java method will produce content identified by the MIME Media
    // type "text/plain"
    @Produces("text/plain")
    public String getIt() {
        SO so = new SO("SOname", "SODescription", ".0.0");
        int n = 0;
        try {
            soDao.create(so);
            n = soDao.findAll().size();
        } catch (AlreadyExistsEntityException e) {
            e.printStackTrace();
        } catch (InvalidEntityException e) {
            e.printStackTrace();
        }
        return "Hi there ! " + so.getId()+"/"+n;
    }
}

