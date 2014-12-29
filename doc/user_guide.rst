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

**Get the Product List from the catalogue**

.. code::

    $ curl -v -H "Content-Type: application/json" -H "Accept: application/json" -H
    "X-Auth-Token: 756cfb31e062216544215f54447e2716" -H "Tenant-Id: your-tenant-id"
	-X GET "https://saggita.lab.fi-ware.org:8443/sdc/rest/catalog/product"

This operation lists the environments stored in the catalogue. The following example shows an XML response for the list Environment API operation. It is possible to see it contains a list of tiers including products to be installed.
	
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

    $ curl -v -H "Content-Type: application/json" -H "Accept: application/json" -H
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

    $ curl -v -H "Content-Type: application/json" -H "Accept: application/json" -H
    "X-Auth-Token: 756cfb31e062216544215f54447e2716" -H "Tenant-Id: your-tenant-id" 
	-X POST "https://saggita.lab.fi-ware.org:8443/sdc/rest/catalog/product/{product-name}"

with the following payload

.. code::

	<product>
 		<name>{product-name}</name>
 		<description>Description</description>
	</product>


**Delete a Product from the catalogue**

.. code::

    $ curl -v -H "Content-Type: application/json" -H "Accept: application/json" -H
    "X-Auth-Token: 756cfb31e062216544215f54447e2716" -H "Tenant-Id: your-tenant-id" 
	-X DELETE "https://saggita.lab.fi-ware.org:8443/sdc/rest/catalog/product/{product-name}"





The network and region information are including also in the payload of the enviornment. The following lines show a example. 

.. code::

    <tier>
        <name>{tier-id}</name> 
        <region>Spain</region>
        <network>Internet</network>
        <network>private_network</network>     
        <productReleases>                  
           ...
        </productReleases>              
    </tier>  

**Modify details of a certain blueprint template**

.. code::

    $ curl -v -H "Content-Type: application/json" -H "Accept: application/json" -H
    "X-Auth-Token: 756cfb31e062216544215f54447e2716" -H "Tenant-Id: your-tenant-id" 
	-X PUT "http://pegasus.lab.fi-ware.org:8080/paasmanager/rest/catalog/org/FIWARE/environment/{environment-id}"

The payload of this request cias as follows
	
.. code::

    <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
    <environment>
        <name>{emvironment-name}</name>
        <tiers>
            <tier>
                <initial_number_instances>1</initial_number_instances>
                <maximum_number_instances>1</maximum_number_instances>
                <minimum_number_instances>1</minimum_number_instances>
                <name>{tier-id}</name>               
                <productReleases>                  
                    <product>postgresql</product>
                    <version>0.0.3</version>
                    <withArtifact>true</withArtifact> 
                    <productType> 
                        <id>5</id>
                        <name>Database</name>  
                    </productType> 
                </productReleases>                    
            </tier>   
            <tier>
                <initial_number_instances>1</initial_number_instances>
                <maximum_number_instances>5</maximum_number_instances>
                <minimum_number_instances>1</minimum_number_instances>
                <name>{tier-id}</name>               
                <productReleases>                  
                    <product>tomcat</product>
                    <version>7</version>
                    <withArtifact>true</withArtifact> 
                    <productType> 
                        <id>6</id>
                       <name>webserver</name>  
                    </productType> 
                </productReleases>   
            </tier>   
        </tiers>
    </environment>

**Delete a blueprint template from the catalogue**

.. code::

    $ curl -v -H "Content-Type: application/json" -H "Accept: application/json" -H
    "X-Auth-Token: 756cfb31e062216544215f54447e2716" -H "Tenant-Id: your-tenant-id" 
	-X DELETE "http://pegasus.lab.fi-ware.org:8080/paasmanager/rest/catalog/org/FIWARE/environment/{environment-id}"

BluePrint Instance Provisioning API
-----------------------------------

**Deploy a Blueprint Instance**

.. code::

    $ curl -v -H "Content-Type: application/json" -H "Accept: application/json" -H
    "X-Auth-Token: 756cfb31e062216544215f54447e2716" -H "Tenant-Id: your-tenant-id" 
	-X POST "http://pegasus.lab.fi-ware.org:8080/paasmanager/rest/catalog/org/FIWARE/vdc/your-tenant-id/environmentInstance"

where {vdc-id} is the tenant-id ("your-tenant-id" in this guide). The payload of this request can be as follows:

.. code::
	
    <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
    <environment>
        <name>{emvironment-name}</name>
        <tiers>
            <tier>
                <initial_number_instances>1</initial_number_instances>
                <maximum_number_instances>1</maximum_number_instances>
                <minimum_number_instances>1</minimum_number_instances>
                <name>{tier-id}</name>               
                <productReleases>                  
                    <product>postgresql</product>
                    <version>0.0.3</version>
                    <withArtifact>true</withArtifact> 
                    <productType> 
                       <id>5</id>
                        <name>Database</name>  
                    </productType> 
                </productReleases>
                ...
            </tier>   
        </tiers>
    </environment>

The response obatined should be:

.. code::

    <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
    <task href="http://pegasus.lab.fi-ware.org:8080/paasmanager/rest/org/FIWARE/vdc/your-tenant-id/task/{task-id}" startTime="2012-11-08T09:13:18.311+01:00" status="RUNNING">
        <description>Deploy environment {emvironment-name}</description>
        <vdc>your-tenant-id</vdc>
    </task>

Given the URL obtained in the href in the Task, it is possible to monitor the operation status (you can check Task Management). Once the environment has been deployed, 
the task status should be SUCCESS. 

.. code::

    <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
    <task href="http:/130.206.80.112:8080/paasmanager/rest/org/{org-id}/vdc/your-tenant-id/task/{task-id}" startTime="2012-11-08T09:13:19.567+01:00" status="SUCCESS">
        <description>Deploy environment {emvironment-name}</description>
        <vdc>your-tenant-id</vdc>
    </task>


**Get information about Blueprint Instances deployed**	

.. code::

    $ curl -v -H "Content-Type: application/json" -H "Accept: application/json" -H
    "X-Auth-Token: 756cfb31e062216544215f54447e2716" -H "Tenant-Id: your-tenant-id" 
	-X GET "http://pegasus.lab.fi-ware.org:8080/paasmanager/rest/catalog/org/FIWARE/vdc/your-tenant-id/environmentInstance"

The Response obtained includes all the blueprint instances deployed

.. code::

    <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
    <environmentInstanceDtoes>
        <environmentInstance>
            <environmentInstanceName>{environmentInstance-id</environmentInstanceName>
            <vdc>your-tenant-id</vdc>
            <environment>
                <name>{environment-name}</name>
                <tiers>
                    <tier>
                    <initial_number_instances>1</initial_number_instances>
                    <maximum_number_instances>1</maximum_number_instances>
                    <minimum_number_instances>1</minimum_number_instances>
                    <name>{tier-id}</name>               
                    <productReleases>                  
                        <product>postgresql</product>
                        <version>0.0.3</version>
                        <withArtifact>true</withArtifact> 
                        <productType> 
                            <id>5</id>
                            <name>Database</name>  
                        </productType> 
                    </productReleases>                     ...
                    </tier>   
                </tiers>
            </environment>        
            <tierInstances>
                <id>35</id>
                <date>2012-10-31T09:24:45.298Z</date>  
                <name>tomcat-</name>       
                <status>INSTALLED</status>       
                <vdc>your-tenant-id</vdc>       
                <tier>
                    <name>{tier-id}</name>               
                </tier>   
                <productInstances>
                    <id>33</id>   
                    <date>2012-10-31T09:14:33.192Z</date>  
                    <name>postgresql</name>         
                    <status>INSTALLED</status>    
                    <vdc>your-tenant-id</vdc>  
                    <productRelease>  
                        <product>postgresql</product>  
                        <version>0.0.3</version> 
                    </productRelase>
                    <vm>
                        <fqn>vmfqn</fqn> 
                        <hostname>rehos456544</hostname> 
                        <ip>109.231.70.77</ip> 
                   </vm>
           </tierInstances>
       </environmentInstance>
   </environmentInstanceDtoes>

**Get details of a certain Blueprint Instance**	

.. code::

    $ curl -v -H "Content-Type: application/json" -H "Accept: application/json" -H
    "X-Auth-Token: 756cfb31e062216544215f54447e2716" -H "Tenant-Id: your-tenant-id" 
	-X GET "http://pegasus.lab.fi-ware.org:8080/paasmanager/rest/catalog/org/FIWARE/vdc/your-tenant-id/environmentInstance/{BlueprintInstance-id}"
	
This operation does not require any payload in the request and provides a BlueprintInstance XML response. 

.. code::

    <environmentInstance>
        <environmentInstanceName>{environmentInstance-id</environmentInstanceName>
        <vdc>your-tenant-id</vdc>
        <environment>
            <name>{emvironment-name}</name>
            <tiers>
                <tier>
                    <initial_number_instances>1</initial_number_instances>
                    <maximum_number_instances>1</maximum_number_instances>
                    <minimum_number_instances>1</minimum_number_instances>
                    <name>{tier-id}</name>               
                    <productReleases>                  
                        <product>postgresql</product>
                        <version>0.0.3</version>
                        <withArtifact>true</withArtifact> 
                        <productType> 
                            <id>5</id>
                            <name>Database</name>  
                        </productType> 
                    </productReleases>                    
                    ...
                </tier>   
            </tiers>
        </environment>        
        <tierInstances>
            <id>35</id>
            <date>2012-10-31T09:24:45.298Z</date>  
            <name>tomcat-</name>       
            <status>INSTALLED</status>       
            <vdc>your-tenant-id</vdc>       
            <tier>
                <name>{tier-id}</name>               
            </tier>   
            <productInstances>
                <id>33</id>   
                <date>2012-10-31T09:14:33.192Z</date>  
                <name>postgresql</name>         
                <status>INSTALLED</status>    
                <vdc>your-tenant-id</vdc>  
                <productRelease>  
                    <product>postgresql</product>  
                    <version>0.0.3</version> 
                </productRelase>
                <vm>
                    <fqn>vmfqn</fqn> 
                    <hostname>rehos456544</hostname> 
                    <ip>109.231.70.77</ip> 
                </vm>
            </productInstance>
        </tierInstances>
    </environmentInstance>


**Undeploy a Blueprint Instance**	

.. code::

    $ curl -v -H "Content-Type: application/json" -H "Accept: application/json" -H
    "X-Auth-Token: 756cfb31e062216544215f54447e2716" -H "Tenant-Id: your-tenant-id" 
	-X DELETE "http://pegasus.lab.fi-ware.org:8080/paasmanager/rest/catalog/org/FIWARE/vdc/your-tenant-id/environmentInstance/{BlueprintInstance-id}"

This operation does not require a request body and returns the details of a generated task. 

.. code::	
	
    <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
    <task href="http:/130.206.80.112:8080/paasmanager/rest/org/{org-id}/vdcyour-tenant-id/task/{task-id}" startTime="2012-11-08T09:45:44.020+01:00" status="RUNNING">
        <description>Uninstall environment</description>
        <vdc>your-tenant-id</vdc>
    </task>

With the URL obtained in the href in the Task, it is possible to monitor the operation status (you can checkTask Management). Once the environment has been undeployed, the task status should be SUCCESS. 

.. code::
	
    <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
    <task href="http:/130.206.80.112:8080/paasmanager/rest/org/{org-id}/vdc/your-tenant-id/task/{task-id}" startTime="2012-11-08T09:13:19.567+01:00" status="SUCCESS">
        <description>Undeploy environment {emvironment-name}</description>
        <vdc>your-tenant-id</vdc>
    </task>

Task Management
--------------- 

**Get a specific task**	

.. code::

    $ curl -v -H "Content-Type: application/json" -H "Accept: application/json" -H
    "X-Auth-Token: 756cfb31e062216544215f54447e2716" -H "Tenant-Id: your-tenant-id" 
	-X DELETE "http://pegasus.lab.fi-ware.org:8080/paasmanager/rest/vdc/your-tenant-id/task/{task-id}"
	
This operation recovers the status of a task created previously. It does not need any request body and the response body in XML would be the following. 

.. code::

    <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
        <task href="http:/130.206.80.112:8080/sdc/rest/vdc/your-tenant-id/task/{task-id}" startTime="2012-11-08T09:13:18.311+01:00" status="SUCCESS">
        <description>Install product tomcat in  VM rhel-5200ee66c6</description>
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
