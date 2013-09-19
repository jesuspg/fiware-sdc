package com.telefonica.euro_iaas.sdc.rest.resources;


import java.util.List;

import com.sun.jersey.multipart.MultiPart;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.exception.AlreadyExistsApplicationReleaseException;
import com.telefonica.euro_iaas.sdc.exception.ApplicationReleaseNotFoundException;
import com.telefonica.euro_iaas.sdc.exception.ApplicationReleaseStillInstalledException;
import com.telefonica.euro_iaas.sdc.exception.EnvironmentNotFoundException;
import com.telefonica.euro_iaas.sdc.exception.InvalidApplicationReleaseException;
import com.telefonica.euro_iaas.sdc.exception.InvalidApplicationReleaseUpdateRequestException;
import com.telefonica.euro_iaas.sdc.exception.InvalidMultiPartRequestException;
import com.telefonica.euro_iaas.sdc.exception.InvalidProductReleaseException;
import com.telefonica.euro_iaas.sdc.exception.ProductReleaseNotFoundException;
import com.telefonica.euro_iaas.sdc.model.Application;
import com.telefonica.euro_iaas.sdc.model.ApplicationRelease;
import com.telefonica.euro_iaas.sdc.model.Attribute;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;

import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * Provides a rest api to works with Applications.
 * 
 * @author Sergio Arroyo & Jesus M. Movilla
 * 
 */
public interface ApplicationResource {

	/**
	 * Insert the selected Application + version (ApplicationRelease)
	 * 
	 * @param Multipart
	 *            which includes
	 * @param applicationReleaseDto
	 * @param The
	 *            cookbook in a tar file
	 * @param The
	 *            set of installables requires fot the application to install/
	 *            /uninstall the product
	 * @return the application release.
	 * @throws AlreadyExistsApplicationReleaseException
	 *             if the Application Release exists
	 * @throws InvalidApplicationReleaseException
	 *             if the Application Release is invalid due to either OS,
	 *             Product or Product Release
	 * @throws ProductReleaseNotFoundException
	 *             if any of the productRelease the Application release depends
	 *             on is not found
	 * @throws InvalidMultiPartRequestException
	 *             if the multipart object is null or its size is different to
	 *             three of if the parts are not filled with the right type of
	 *             Object
	 */

	@POST
	@Path("/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes("multipart/mixed")
	ApplicationRelease insert(MultiPart multiPart)
			throws AlreadyExistsApplicationReleaseException,
			InvalidApplicationReleaseException,
			ProductReleaseNotFoundException, InvalidMultiPartRequestException,
			InvalidProductReleaseException, EnvironmentNotFoundException;

	/**
	 * Retrieve all applications available created in the system.
	 * 
	 * @param page
	 *            for pagination is 0 based number(<i>nullable</i>)
	 * @param pageSize
	 *            for pagination, the number of items retrieved in a query
	 *            (<i>nullable</i>)
	 * @param orderBy
	 *            the file to order the search (id by default <i>nullable</i>)
	 * @param orderType
	 *            defines if the order is ascending or descending (asc by
	 *            default <i>nullable</i>)
	 * @return the created OS instances.
	 */
	@GET
	@Path("/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	List<Application> findAll(@QueryParam("page") Integer page,
			@QueryParam("pageSize") Integer pageSize,
			@QueryParam("orderBy") String orderBy,
			@QueryParam("orderType") String orderType);

	/**
	 * Retrieve the selected Application.
	 * 
	 * @param name
	 *            the application name
	 * @return the loaded application.
	 * @throws EntityNotFoundException
	 *             if the osInstance does not exists
	 */
	@GET
	@Path("/{appName}")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	Application load(@PathParam("appName") String name)
			throws EntityNotFoundException;

	/**
	 * Retrieve the available attributes for a selected application.
	 * 
	 * @param name
	 *            the application name
	 * @return the attributes for the selected application.
	 * @throws EntityNotFoundException
	 *             if the application does not exists
	 */
	@GET
	@Path("/{appName}/attributes/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	List<Attribute> loadAttributes(@PathParam("appName") String name)
			throws EntityNotFoundException;

	// ///////////// APPLICATION RELEASES ///////////////

	/**
	 * Retrieve all available versions of the given application.
	 * 
	 * @param page
	 *            for pagination is 0 based number(<i>nullable</i>)
	 * @param pageSize
	 *            for pagination, the number of items retrieved in a query
	 *            (<i>nullable</i>)
	 * @param orderBy
	 *            the file to order the search (id by default <i>nullable</i>)
	 * @param orderType
	 *            defines if the order is ascending or descending (asc by
	 *            default <i>nullable</i>)
	 * @return the created OS instances.
	 */
	@GET
	@Path("/{appName}/release/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	List<ApplicationRelease> findAll(@PathParam("appName") String name,
			@QueryParam("page") Integer page,
			@QueryParam("pageSize") Integer pageSize,
			@QueryParam("orderBy") String orderBy,
			@QueryParam("orderType") String orderType);

	/**
	 * Retrieve the selected Application version.
	 * 
	 * @param name
	 *            the application name
	 * @param version
	 *            the concrete version
	 * @return the application.
	 * @throws EntityNotFoundException
	 *             if the application or the version does not exists
	 */
	@GET
	@Path("/{appName}/release/{version}")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	ApplicationRelease load(@PathParam("appName") String name,
			@PathParam("version") String version)
			throws EntityNotFoundException;

	/**
	 * Retrieve the selected Application version.
	 * 
	 * @param name
	 *            the application name
	 * @param version
	 *            the concrete version
	 * @return the application.
	 * @throws ApplicationReleaseNotFoundException
	 *             if the application Release does not exists
	 * @throws ApplicationReleaseStillInstalledException
	 *             if the application Release is still installed on some VMs
	 */
	@DELETE
	@Path("/{appName}/release/{version}")
	void delete(@PathParam("appName") String name,
			@PathParam("version") String version)
			throws ApplicationReleaseNotFoundException,
			ApplicationReleaseStillInstalledException;

	/**
	 * Update the selected Application version (only he info in DB)
	 * 
	 * @param Multipart
	 *            which includes
	 * @param applicationReleaseDto
	 * @param The
	 *            cookbook in a tar file
	 * @param The
	 *            set of installables requires for the application to install/
	 *            /uninstall the product
	 * @return the application release.
	 * @throws ApplicationReleaseNotFoundException
	 *             if the application Release does not exists
	 * @throws InvalidApplicationReleaseException
	 *             if the application Release is not valid according to DB
	 * @throws ProductReleaseNotFoundException
	 *             any Product in the ApplicationRelease Object does not exist
	 */
	@PUT
	@Path("/{appName}/release/{version}")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	ApplicationRelease update(MultiPart multipart)
			throws ApplicationReleaseNotFoundException,
			InvalidApplicationReleaseException,
			ProductReleaseNotFoundException,
			InvalidApplicationReleaseUpdateRequestException,
			InvalidMultiPartRequestException, InvalidProductReleaseException,
			EnvironmentNotFoundException;

	/**
	 * Retrieve the attributes for the selected application release. The result
	 * is a merge between common attributes (of the application) and the private
	 * attributes of the concrete release.
	 * 
	 * @param name
	 *            the application name
	 * @param version
	 *            the concrete version
	 * @return the attributes.
	 * @throws EntityNotFoundException
	 *             if the application does not exists
	 */
	@GET
	@Path("/{appName}/release/{version}/attributes/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	List<Attribute> loadAttributes(@PathParam("appName") String name,
			@PathParam("version") String version)
			throws EntityNotFoundException;

	/**
	 * Find all possible transitions for a concrete release. It means, the
	 * different version of a application which are compatible with the given
	 * release.
	 * 
	 * @param name
	 *            the application Name
	 * @param version
	 *            the application version
	 * @return the transitable releases.
	 * @throws EntityNotFoundException
	 *             if the given release does not exists.
	 */
	@GET
	@Path("/{appName}/release/{version}/updatable")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	List<ApplicationRelease> findTransitable(@PathParam("appName") String name,
			@PathParam("version") String version)
			throws EntityNotFoundException;

	/**
	 * Find all possible transitions for a concrete release. It means, the
	 * different version of a product which are compatible with the given
	 * release.
	 * 
	 * @param name
	 *            the product Name
	 * @param version
	 *            the product version
	 * @return the transitable releases.
	 * @throws EntityNotFoundException
	 *             if the given release does not exists.
	 */
	@GET
	@Path("/release")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	List<ApplicationRelease> findAllReleases(@QueryParam("page") Integer page,
			@QueryParam("pageSize") Integer pageSize,
			@QueryParam("orderBy") String orderBy,
			@QueryParam("orderType") String orderType);

}
