package com.telefonica.euro_iaas.sdc.rest.resources;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MultivaluedMap;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.sun.jersey.api.core.InjectParam;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.dao.CustomerDao;
import com.telefonica.euro_iaas.sdc.dao.ImageDao;
import com.telefonica.euro_iaas.sdc.dao.ProductDao;
import com.telefonica.euro_iaas.sdc.dao.SODao;
import com.telefonica.euro_iaas.sdc.manager.ImageManager;
import com.telefonica.euro_iaas.sdc.model.Customer;
import com.telefonica.euro_iaas.sdc.model.Image;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.SO;

/**
 * Resource that implements the customer operations
 * @author Cristian Jaramillo
 *
 */

@Path("/customer/{customer}")
@Component
@Scope("request")

public class CustomerResource implements GetResource, PostResource {
    @InjectParam("customerDao")
    CustomerDao customerDao;

    @InjectParam("soDao")
    SODao soDao;

    @InjectParam("productDao")
    ProductDao productDao;

    @InjectParam("imageDao")
    ImageDao imageDao;

    @InjectParam("imageManager")
    ImageManager manager;

    /**
     * This method returns the list of images that
     * a customer had deployed
     * @param customerID the unique customer identifier
     * @return a list of the available resources associated to the customer
     */
    @Override
    @GET
    @Produces("text/plain")
    public String getResource(@PathParam("customer") String customerID) {
        Customer customer;
        try {
            customer = customerDao.load(customerID);

            StringBuilder sb = new StringBuilder("Lista de imágenes desplegadas:\n");
            for(Image img : imageDao.findAll())
                if(img.getCustomer().equals(customer))
                    sb.append("\tID: "+img.getId()+" - "+img.getSo()+"\n");

            return "Customer "+customer.getName().toUpperCase()+"\n"+sb.toString();
        } catch (EntityNotFoundException e) {
            return "Customer Not Found";
        }
    }

    /**
     * This method process a new image deployment
     * @param formParams necessary params to make a deployment are SO and app List
     * @throws Exception
     */
    @Override
    @POST
    @Consumes("application/x-www-form-urlencoded")
    public void processPetition(@PathParam("customer") String customerID, MultivaluedMap<String, String> formParams) throws Exception {
        Customer customer = customerDao.load(customerID);

        for(String key : formParams.keySet()) {
            System.out.println("Key: "+key+" - Value: "+formParams.get(key)+"\n");
        }

        String soName = formParams.getFirst("so");
        SO so = soDao.load(soName);


        List<Product> productList = new ArrayList<Product>();
        List<String> appFormList = formParams.get("app");
        if(appFormList != null) {
            for(String app : appFormList) {
                productList.add(productDao.load(app));
            }
        }

        for(int i = 0; i < 80; i++)
            System.out.print("-");
        System.out.println();
        System.out.println("Se va a crear una imagen");
        System.out.println("Sistema Operativo: "+so.getName());
        for(Product p : productList)
            System.out.println("Product: "+p.getName());

        manager.deploy(customer, so, productList);
    }
}
