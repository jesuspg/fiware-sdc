SDC - User and Programmers Guide
______________________________________________

Introduction
============

Welcome the User and Programmer Guide for Software Deployment and Configuration. 


Accessing SDC from the CLI 
===================================

The access through the CLI is made using the curl program. Curl [http://curl.haxx.se/] is a client to get documents/files from or send documents to a server, using any of the supported protocols (HTTP, HTTPS, FTP, GOPHER, DICT, TELNET, LDAP or FILE) and therefore is also usable for OpenStack Compute API. Use the curl command line tool or use libcurl from within your own programs in C. Curl is free and open software that compiles and runs under a wide variety of operating systems.

The normal operations sequence to deploying an environment and an application on top of it could be summarized in the following list:


API Authentication
------------------
All the operations in the SDC API needs to have a valid token to access it. To obtain the token, you need to have an account in FIWARE Lab (account.lab.fi-ware.org).
With the credentials (username, password and tenantName) you can obtain a valid token. From now on, we asume that the value of your tenant-id is "your-tenant-id"

.. code::

    $ curl -v -H "Content-Type: application json" -H "Accept: application/json" -X
    POST "http://cloud.lab.fi-ware.org:4731/v2.0/tokens" -d '{"auth":{"tenantName":
    "your-tenant-id","passwordCredentials":{"username":"youruser","password":"yourpassword"}}}'

You will receive the following answer, with a valid token (id).

.. code:: json
  
    {
    access: {
       token: {
          expires: "2015-07-09T15:16:07Z"
          id: "756cfb31e062216544215f54447e2716"
          tenant: {
	  ..
    }
	
For all the SDC request, you will need to include the following header:

.. code::

    X-Auth-Token: 756cfb31e062216544215f54447e2716
    Tenant-Id: your-tenant-id

For the rest of the explanation, we are going to configure a set of variables:

.. code::

    export SDC_IP =  saggita.lab.fi-ware.org
  
Catalogue Management API
------------------------
Next we detail some operations that can be done in the catalogue management api

Product API
-----------

**Get the Product List from the catalogue**

.. code::

    $ curl -v -H "Content-Type: application/json" -H "Accept: application/xml" -H
    "X-Auth-Token: 756cfb31e062216544215f54447e2716" -H "Tenant-Id: your-tenant-id"
	-X GET "https://saggita.lab.fi-ware.org:8443/sdc/rest/catalog/product"

This operation lists all the products stored in the catalogue. The following example shows an XML response for the Product List API operation.
	
.. code::	

    <products>
 		<product>
			<name>tomcat</name>
			<description>tomcat J2EE container</description>
 		</product>
 		...
 		<product>
			<name>mysql</name>
			<description>mysql</description>
 			<attributes>
 				<key>key1</key>
				<value>value1<value/>
				<description>description1</description>
 			</attributes>
 			</metadatas>
 			<metadatas>
				<key>installator</key>
				<value>chef</value>
				<description>mysql installator</description>
 			</metadatas>
 		</product>
	<products>

**Get the Details of a particular Product List**

.. code::

    $ curl -v -H "Content-Type: application/json" -H "Accept: application/xml" -H
    "X-Auth-Token: 756cfb31e062216544215f54447e2716" -H "Tenant-Id: your-tenant-id"
	-X GET "https://saggita.lab.fi-ware.org:8443/sdc/rest/catalog/product/{product-name}"

This operation lists the environments stored in the catalogue. The following example shows an XML response for the list Environment API operation. It is possible to see it contains a list of tiers including products to be installed.
	
.. code::	

	<product>
		<name>mysql</name>
		<description>mysql</description>
 		<attributes>
 			<key>key1</key>
			<value>value1<value/>
			<description>description1</description>
 		</attributes>
 		</metadatas>
 		<metadatas>
			<key>installator</key>
			<value>chef</value>
			<description>mysql installator</description>
 		</metadatas>
 	</product>


**Add a New Product to the catalogue**

.. code::

    $ curl -v -H "Content-Type: application/json" -H "Accept: application/xml" -H
    "X-Auth-Token: 756cfb31e062216544215f54447e2716" -H "Tenant-Id: your-tenant-id" 
	-X POST "https://saggita.lab.fi-ware.org:8443/sdc/rest/catalog/product/{product-name}"

with the following payload (with metadatas and attributes)

.. code::

	<product>
 		<name>{product-name}</name>
 		<description>Description</description>
 		<attributes>
 			<key>key1</key>
			<value>value1<value/>
			<description>description1</description>
 		</attributes>
 		<metadatas>
			<key>installator</key>
			<value>chef</value>
			<description>mysql installator</description>
 		</metadatas>
	</product>


**Delete a Product from the catalogue**

.. code::

    $ curl -v -H "Content-Type: application/json" -H "Accept: application/xml" -H
    "X-Auth-Token: 756cfb31e062216544215f54447e2716" -H "Tenant-Id: your-tenant-id" 
	-X DELETE "https://saggita.lab.fi-ware.org:8443/sdc/rest/catalog/product/{product-name}"

Product Release API
-------------------

**Get the Releases List of a particular Product**

.. code::

    $ curl -v -H "Content-Type: application/json" -H "Accept: application/xml" -H
    "X-Auth-Token: 756cfb31e062216544215f54447e2716" -H "Tenant-Id: your-tenant-id"
	-X GET "https://saggita.lab.fi-ware.org:8443/sdc/rest/catalog/product/{product-name}/release"

This operation lists the product releases of {product-name} stored in the catalogue. The following example shows an XML response for the list of ProductRelease API operation.
	
.. code::	

	<productReleases>
 		<productRelease>
			<releaseNotes>{product-name} 0.6.15</releaseNotes>
			<version>0.6.15</version>
 			<product>
				<name>{product-name}</name>
				<description>desc</description>
 			</product>
 			<supportedOOSS>
				<id>1</id>
				<v>0</v>
				<osType>94</osType>
				<name>Ubuntu</name>
				<description>Ubuntu 10.04</description>
				<version>10.04</version>
 			</supportedOOSS>
 		</productRelease>
 		<productRelease>
			<version>0.9.0</version>
 			<product>
				<name>{product-name}</name>
				<description>{product-name} 0.6.15</description>
 			</product>
 			<supportedOOSS>
				<id>1</id>
				<v>0</v>
				<osType>94</osType>
				<name>Ubuntu</name>
				<description>Ubuntu 10.04</description>
				<version>10.04</version>
 			</supportedOOSS>
 		</productRelease>
 	</productReleases>


**Get the Details of a Particular Product Release**

.. code::

    $ curl -v -H "Content-Type: application/json" -H "Accept: application/xml" -H
    "X-Auth-Token: 756cfb31e062216544215f54447e2716" -H "Tenant-Id: your-tenant-id"
	-X GET "https://saggita.lab.fi-ware.org:8443/sdc/rest/catalog/product/{product-name}/release/{version}"

This operation lists the details of a Product Release.
	
.. code::	

	<productReleases>
 		<productRelease>
			<releaseNotes>{product-name} 0.6.15</releaseNotes>
			<version>0.6.15</version>
 			<product>
				<name>{product-name}</name>
				<description>desc</description>
 			</product>
 			<supportedOOSS>
				<id>1</id>
				<v>0</v>
				<osType>94</osType>
				<name>Ubuntu</name>
				<description>Ubuntu 10.04</description>
				<version>10.04</version>
 			</supportedOOSS>
 		</productRelease>
 	</productReleases>	
 	
 
**Add a New Release to a Product into the catalogue**

.. code::

    $ curl -v -H "Content-Type: application/json" -H "Accept: application/xml" -H
    "X-Auth-Token: 756cfb31e062216544215f54447e2716" -H "Tenant-Id: your-tenant-id" 
	-X POST "https://saggita.lab.fi-ware.org:8443/sdc/rest/catalog/product/{product-name}/release"

with the following payload

.. code::

	<productReleaseDto>
		<productName>{product-name}</productName>
		<version>{version}</version>
		<productDescription>description of {product-name}-{version}/productDescription>
	</productReleaseDto>

**Delete the Release of a Product**

.. code::

    $ curl -v -H "Content-Type: application/json" -H "Accept: application/xml" -H
    "X-Auth-Token: 756cfb31e062216544215f54447e2716" -H "Tenant-Id: your-tenant-id" 
	-X DELETE "https://saggita.lab.fi-ware.org:8443/sdc/rest/catalog/product/{product-name}/release"


**Get All Product and Releases of the catalogue**

.. code::

    $ curl -v -H "Content-Type: application/json" -H "Accept: application/xml" -H
    "X-Auth-Token: 756cfb31e062216544215f54447e2716" -H "Tenant-Id: your-tenant-id"
	-X GET "https://saggita.lab.fi-ware.org:8443/sdc/rest/catalog/productandrelease"

This operation lists all product releases stored in the Catalogue and available for users.

.. code::

	<productAndReleaseDtoes>
 		<productAndReleaseDto>
 			<product>
				<name>tomcat</name>
				<description>tomcat J2EE container</description>
 			</product>
			<version>6</version>
	 	</productAndReleaseDto>
 		...
 		<productAndReleaseDto>
 			<product>
				<name>nodejs</name>
				<description>nodejs</description>
 			</product>
			<version>0.6.15</version>
 		</productAndReleaseDto>
	</productAndReleaseDtoes>


Product Instance Provisioning API
---------------------------------

**Install a Product in a Virtual Machine**

.. code::

    $ curl -v -H "Content-Type: application/json" -H "Accept: application/xml" -H
    "X-Auth-Token: 756cfb31e062216544215f54447e2716" -H "Tenant-Id: your-tenant-id" 
	-X POST "http://saggita.lab.fi-ware.org:8080/sdc/rest/vdc/{your-tenant-id}/productInstance"

where {your-tenant-id} is the tenant-id in this guide. The payload of this request can be as follows:

.. code::
	
	<productInstanceDto>
  		<vm>
    		<ip>{ip}</ip>
    		<fqn>{fqn}</fqn>
    		<hostname>{hostname}</hostname>
  		</vm>
  		<product>
    		<productDescription/>
    		<name>{product-name}</name>
    		<version>{product-version}</version>
 		</product>
 		<attributes>
			<key>custom_att_02</key>
			<value>default_value_plain</value>
			<type>Plain</type>
		</attributes>
	</productInstanceDto>

The response obatined should be:

.. code::

    <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
        <task href="https://saggita.lab.fi-ware.org:8443/sdc/rest/vdc/{your-tenant-id}/task/{task-id}" startTime="2012-11-08T09:13:18.311+01:00" status="RUNNING">
        <description>Install product {product-name} in  VM {vm}</description>
        <vdc>your-tenant-id</vdc>
    </task>

Given the URL obtained in the href in the Task, it is possible to monitor the operation status (you can check Task Management). Once the environment has been deployed, 
the task status should be SUCCESS. 

.. code::

    <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
        <task href="https://saggita.lab.fi-ware.org:8443/sdc/rest/vdc/{your-tenant-id}/task/{task-id}" startTime="2012-11-08T09:13:28.311+01:00" status="SUCCESS">
        <description>Install product {product-name} in  VM {vm}</description>
        <vdc>your-tenant-id</vdc>
    </task>


**Get the list of Product Instances deployed**	

.. code::

    $ curl -v -H "Content-Type: application/json" -H "Accept: application/xml" -H
    "X-Auth-Token: 756cfb31e062216544215f54447e2716" -H "Tenant-Id: your-tenant-id" 
	-X GET "https://saggita.lab.fi-ware.org:8443/sdc/rest/vdc/{your-tenant-id}/productInstance"

The Response obtained includes all the blueprint instances deployed

.. code::

	<productInstances>
 		<productInstance>
			<id>8790</id>
			<date>2014-10-30T12:49:35.528+01:00</date>
			<name>{productInstance-name}</name>
			<status>INSTALLED</status>
 			<vm>
    			<ip>{ip}</ip>
    			<fqn>{fqn}</fqn>
    			<hostname>{hostname}</hostname>
  			</vm>
			<vdc>{your-tenant-id}</vdc>
 			<productRelease>
				<version>{product-version}</version>
 				<product>
					<name>{product-name}</name>
 					<metadatas>
						<key>key1</key>
						<value>value1</value>
						<description>desc</description>
 					</metadatas>
 				</product>
 			</productRelease>
 		</productInstance>
 		...
 		<productInstance>
 			...
 		</productInstance>
	</productInstances>

**Get details of a certain Product Instance**	

.. code::


    $ curl -v -H "Content-Type: application/json" -H "Accept: application/xml" -H
    "X-Auth-Token: 756cfb31e062216544215f54447e2716" -H "Tenant-Id: your-tenant-id" 
	-X GET "https://saggita.lab.fi-ware.org:8443/sdc/rest/vdc/{your-tenant-id}/productInstance/{productInstance-name}"
	
This operation does not require any payload in the request and provides a BlueprintInstance XML response. 

.. code::

	<?xml version="1.0" encoding="UTF-8" standalone="yes" ?>
 	<productInstance>
		<id>8790</id>
		<date>2014-10-30T12:49:35.528+01:00</date>
		<name>mykurentoinstance-kurento-1-003237_kurento_5.0.3</name>
		<status>INSTALLED</status>
 		<vm>
			<ip>130.206.126.23</ip>
			<hostname>mykurentoinstance-kurento-1-003237</hostname>
			<domain />
			<fqn>mykurentoinstance-kurento-1-003237</fqn>
			<osType />
 		</vm>
		<vdc>{your-tenant-id}</vdc>
 		<productRelease>
			<version>{product-version}</version>
 			<product>
				<name>{product-name}</name>
 				<metadatas>
					<key>key1</key>
					<value>value1</value>
					<description>desc</description>
 				</metadatas>
 			</product>
 		</productRelease>
 	</productInstance>


**Uninstall a Product Instance**	

.. code::

    $ curl -v -H "Content-Type: application/json" -H "Accept: application/xml" -H
    "X-Auth-Token: 756cfb31e062216544215f54447e2716" -H "Tenant-Id: your-tenant-id" 
	-X DELETE "https://saggita.lab.fi-ware.org:8443/sdc/rest/vdc/{your-tenant-id}/productInstance/{productInstance-name}"

This operation does not require a request body and returns the details of a generated task. 

.. code::	
	
    <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
    <task href="https://saggita.lab.fi-ware.org:8443/sdc/rest/vdc/{your-tenant-id}/task/{task-id}" startTime="2012-11-08T09:45:44.020+01:00" status="RUNNING">
        <description>Uninstall Product</description>
        <vdc>your-tenant-id</vdc>
    </task>

With the URL obtained in the href in the Task, it is possible to monitor the operation status (you can checkTask Management). Once the environment has been undeployed, the task status should be SUCCESS. 

.. code::
	
    <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
    <task href="https://saggita.lab.fi-ware.org:8443/sdc/rest//vdc/{your-tenant-id}/task/{task-id}" startTime="2012-11-08T09:13:19.567+01:00" status="SUCCESS">
        <description>Unistall product {product-name}</description>
        <vdc>your-tenant-id</vdc>
    </task>

Node Manegement API
-------------------

**Load a particular node**

.. code::

    $ curl -v -H "Content-Type: application/json" -H "Accept: application/xml" -H
    "X-Auth-Token: 756cfb31e062216544215f54447e2716" -H "Tenant-Id: your-tenant-id"
	-X GET "https://saggita.lab.fi-ware.org:8443/sdc/rest/vdc/{your-tenant-id}/chefClient/{node-name}"

This operation lists information of a specific node.
 		
**Delete a particular node**

.. code::

    $ curl -v -H "Content-Type: application/json" -H "Accept: application/xml" -H
    "X-Auth-Token: 756cfb31e062216544215f54447e2716" -H "Tenant-Id: your-tenant-id" 
	-X DELETE "https://saggita.lab.fi-ware.org:8443/sdc/rest/vdc/{your-tenant-id}/chefClient/{node-name}"

	
	
Task Management
--------------- 

**Get a specific task**	

.. code::

    $ curl -v -H "Content-Type: application/json" -H "Accept: application/xml" -H
    "X-Auth-Token: 756cfb31e062216544215f54447e2716" -H "Tenant-Id: your-tenant-id" 
	-X GET "https://saggita.lab.fi-ware.org:8443/sdc/rest/vdc/your-tenant-id/task/{task-id}"
	
This operation recovers the status of a task created previously. It does not need any request body and the response body in XML would be the following. 

.. code::

    <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
        <task href="https://saggita.lab.fi-ware.org:8443/sdc/rest/vdc/your-tenant-id/task/{task-id}" startTime="2012-11-08T09:13:18.311+01:00" status="SUCCESS">
        <description>Install product {product-name} in  VM {vm}</description>
        <vdc>your-tenant-id</vdc>
    </task>


The value of the status attribute could be one of the following: 

=========  ====================================
Value      Description 
=========  ====================================
QUEUED     The task is queued for execution.   
PENDING    The task is pending for approval.   
RUNNING    The task is currently running.      
SUCCESS    The task is completed successfully.  
ERROR      The task is finished but it failed.  
CANCELLED  The task has been cancelled by user.  
=========  ====================================
