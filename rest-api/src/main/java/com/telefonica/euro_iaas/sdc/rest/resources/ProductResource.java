package com.telefonica.euro_iaas.sdc.rest.resources;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;

/**
 * Provides a rest api to works with Product.
 *
 * @author Sergio Arroyo
 *
 */
public interface ProductResource {

    /**
     * Retrieve all Products available created in the system.
     *
     * @param page
     *            for pagination is 0 based number(<i>nullable</i>)
     * @param pageSize
     *            for pagination, the number of items retrieved in a query
     *            (<i>nullable</i>)
     * @param orderBy
     *            the file to order the search (id by default <i>nullable</i>)
     * @param orderType
     *            defines if the order is ascending or descending
     *            (asc by default <i>nullable</i>)
     * @return the created OS instances.
     */
    @GET
    @Path("/")
    @Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    List<Product> findAll(@QueryParam("page") Integer page,
            @QueryParam("pageSize") Integer pageSize,
            @QueryParam("orderBy") String orderBy,
            @QueryParam("orderType") String orderType);

    /**
     * Retrieve the selected Product.
     *
     * @param name
     *            the product name
     * @return the product.
     * @throws EntityNotFoundException
     *             if the product does not exists
     */
    @GET
    @Path("/{pName}")
    @Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    Product load(@PathParam("pName") String name)
        throws EntityNotFoundException;

    /**
     * Retrieve the common attributes for the selected product.
     *
     * @param name
     *            the product name
     * @return the attributes.
     * @throws EntityNotFoundException
     *             if the product does not exists
     */
    @GET
    @Path("/{pName}/attributes/")
    @Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    List<Attribute> loadAttributes(@PathParam("pName") String name)
        throws EntityNotFoundException;

    /////////////// PRODUCT RELEASES ///////////////

    /**
     * Retrieve all available versions of the given product.
     *
     * @param page
     *            for pagination is 0 based number(<i>nullable</i>)
     * @param pageSize
     *            for pagination, the number of items retrieved in a query
     *            (<i>nullable</i>)
     * @param orderBy
     *            the file to order the search (id by default <i>nullable</i>)
     * @param orderType
     *            defines if the order is ascending or descending
     *            (asc by default <i>nullable</i>)
     * @return the created OS instances.
     */
    @GET
    @Path("/{pName}/release/")
    @Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    List<ProductRelease> findAll(@PathParam("pName") String name,
            @QueryParam("page") Integer page,
            @QueryParam("pageSize") Integer pageSize,
            @QueryParam("orderBy") String orderBy,
            @QueryParam("orderType") String orderType);

    /**
     * Retrieve the selected Product version.
     *
     * @param name
     *            the product name
     * @param version the concrete version
     * @return the product.
     * @throws EntityNotFoundException
     *             if the product or the version does not exists
     */
    @GET
    @Path("/{pName}/release/{version}")
    @Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    ProductRelease load(@PathParam("pName") String name,
            @PathParam("version") String version)
        throws EntityNotFoundException;

    /**
     * Retrieve the attributes for the selected product release. The result is
     * a merge between common attributes (of the product) and the private
     *  attributes of the concrete release.
     *
     * @param name
     *            the product name
     * @param version the concrete version
     * @return the attributes.
     * @throws EntityNotFoundException
     *             if the product does not exists
     */
    @GET
    @Path("/{pName}/release/{version}/attributes/")
    @Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    List<Attribute> loadAttributes(@PathParam("pName") String name,
            @PathParam("version") String version)
        throws EntityNotFoundException;


    /**
     * Find all possible transitions for a concrete release.
     * It means, the different version of a product which are compatible with
     * the given release.
     * @param name the product Name
     * @param version the product version
     * @return the transitable releases.
     * @throws EntityNotFoundException if the given release does not exists.
     */
    @GET
    @Path("/{pName}/release/{version}/updatable")
    @Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    List<ProductRelease> findTransitable(@PathParam("pName") String name,
             @PathParam("version") String version)
             throws EntityNotFoundException;
}
