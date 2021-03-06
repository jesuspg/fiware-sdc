SDC Build and Installation
==========================


Requirements
------------

In order to execute the SDC, it is needed to have previously installed
the following software:

-  Chef node
-  Chef server
   [http://wiki.opscode.com/display/chef/Installing+Chef+Server\ ]. For
   CentOS it is possible to follow the following
   instructions[http://blog.frameos.org/2011/05/19/installing-chef-server-0-10-in-rhel-6-scientificlinux-6/\ ]

-  SDC node
-  open jdk 7
-  PostgreSQL [http://www.postgresql.org/\ ]
-  Webdav

Building instructions
---------------------

It is a a maven application:

-  Compile, launch test and build all modules

.. code::

       $ mvn clean install

-  Create a zip with distribution in target/sdc-server-dist.zip

.. code ::

       $ mvn assembly:assembly -DskipTests
       
       $ cp target/distribution/sdc-server-dist {folder}
       $ {folder}/sdc-server-dist/bin/generateselfsigned.sh start 
       $ cd {folder}/sdc-server-dist/bin ; ./jetty.sh start 

-  You can generate a rpm o debian packages (using profiles in pom)

   for debian/ubuntu:

.. code::

       $ mvn install -Pdebian -DskipTests
       (created target/sdc-server-XXXXX.deb)

   for centOS:
   
.. code::

   		$ mvn package -P rpm -DskipTests
   		(created target/rpm/sdc/RPMS/noarch/fiware-sdc-XXXX.noarch.rpm)

Please, be aware  that the supported installation method is the RPM package. If you use other method, some extra steps may be required. For example you would need to generate manually the certificate (See the section about "Configuring the HTTPS certificate" for more information):

Installation instructions
-------------------------

Chef server
~~~~~~~~~~~

Chef server Installation
^^^^^^^^^^^^^^^^^^^^^^^^

The installation of the chef-server involves to install the chef-server
package, which can be obtained in
[http://www.getchef.com/chef/install/\ ]. We can just execute

.. code::

    rpm -Uvh chef-server-package.rpm

Verify the the hostname for the Chef server by running the hostname
command. The hostname for the Chef server must be a FQDN. This means
hostaname.domainame. In case it is not configure, you can do it

.. code::

    hostname chef-server.localdomain

and include it in the /etc/hosts

After that, it is required to configure the certificates and other
staff in the chef-server, with chef-server-ctl. This command will set up
all of the required components, including Erchef, RabbitMQ, and
PostgreSQL.

.. code::

    sudo chef-server-ctl reconfigure

In order to test verify the installation of Chef Server 11.x by running
the following command:

.. code::

    sudo chef-server-ctl test

After that, you can obtain the different certificates for the
different clients in /etc/chef-server. There you can find a
chef-validator.pem (needed for all the nodes), the chef-server-gui for
the GUI.. You can copy them in order to use them later.

The next step is to configure a client in the chef-server so that you
can execute the chef-server CLI. To do that, you need to install the
chef-client

.. code::

    curl -L https://www.opscode.com/chef/install.sh | sudo bash

and configure it with the following command. You can accept all the
default

.. code::

    knife configure --initial

The validation\_key attribute in the knife.rb file must specify the
path to the validation key. The validation\_client\_name attribute
defaults to chef-validator (which is the chef-validator.pem private key
created by the open source Chef server on startup). When prompted for
the URL for the Chef server, use the FQDN for the Chef server

Once you have a client configured, you can run the CLI. Just one
example:

.. code::

    knife client list

Chef server cookbook repository
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The FI-WARE cookbook repository is in FI-WARE SVN repository. To upload
the recipes into the chef server you need:

-  To dowload the svn repository:

.. code::

   svn checkout https://forge.fi-ware.org/scmrepos/svn/testbed/trunk/cookbooks

-  Inside the cookbooks folder, create a file update with the following
   content. It will update the repository and upload into the chef-server

.. code::

    svn update
    knife cookbook upload --all -o BaseRecipes/
    knife cookbook upload --all -o BaseSoftware/
    knife cookbook upload --all -o GESoftware/


Database configuration
----------------------

The SDC node needs to have PostgreSQL installed in service mode and a
database created called SDC. For CentOS, these are the instructions:

Firstly, it is required to install the PostgreSQL
[http://wiki.postgresql.org/wiki/YUM_Installation\ ].

.. code:: 
    yum install postgreql postgresql-server posgresql-cotrib


Start Postgresql
~~~~~~~~~~~~~~~~

Type the following commands to install the postgresql as service and
restarted

.. code::

    chkconfig --add postgresql
    chkconfig postgresql on
    service postgresql initdb
    service postgresql start

Then, you need to configure postgresql to allow for accessing. In
/var/lib/pgsql/data/postgresql.conf

.. code::

    listen\_addresses = '0.0.0.0'

In /var/lib/pgsql/data/pg\_hba.conf, change the table at the end of the file to
look like:

.. code::

    #TYPE   DATABASE  USER        CIDR-ADDRESS          METHOD
    #"local" is for Unix domain socket connections only
    local   all       all                               ident
    # IPv4 local connections:
    host    all       all         127.0.0.1/32          md5
    # IPv6 local connections:
    host    all       all         ::1/128               md5


Restart the postgres service postgresql restart


Create the DB
^^^^^^^^^^^^^

Connect to Postgresql Server using:

.. code::

    su - postgres

Connect as postgres to the postgres database and set the password for
user postgres using alter user as below:

.. code::

    $ psql postgres postgres
    > alter user postgres with password 'postgres';


Create the SDC DB

.. code::

    > createdb sdc

Check that the database has been created correctly:

.. code::

   $ su - postgres
   $ psql -U postgres sdc -h localhost

Then we need to create the database tables for the sdc. To do that
obtain the files from
[https://github.com/telefonicaid/fiware-sdc/blob/develop/migrations/src/main/resources\ ]
and execute

.. code::

   $ psql -d sdc -a -f db-initial.sql
   $ psql -d sdc -a -f db-changelog.sql


Install and configure WebDav
~~~~~~~~~~~~~~~~~~~~~~~~~~~~

These instructions are based on
[http://www.howtoforge.com/how-to-set-up-webdav-with-apache2-on-centos-5.5\ ]

The webdav is the component of the SDC-Server that stores the
installables of the available software (products and applications) to be
installed in the nodes.

Make sure Apache2 is installed and the optional DAV modules are enabled

.. code::

     yum install httpd
     vi /etc/httpd/conf/httpd.conf
     [...] LoadModule dav_module modules/mod_dav.so
     [...] LoadModule dav_fs_module modules/mod_dav_fs.so [...]

Then create the system startup links for Apache and start it:

.. code::

    chkconfig --levels 235 httpd on
    /etc/init.d/httpd start<pre>

Create a Virtual host in /etc/apache2/sites-available/sdc.com

.. code::

    <VirtualHost *:80>
     ServerAdmin webmaster@example.com
     ServerName 109.231.82.11
     DocumentRoot /opt/sdc/webdav
     <Directory /opt/sdc/webdav>
        Options Indexes MultiViews
        AllowOverride None
        Order allow,deny allow from all
     </Directory>
    </VirtualHost>

We need now to create the directory where all the files managed by our
WebDav are going to be:

.. code::

    mkdir /opt/sdc/webdav
    chown www-data /opt/sdc/webdav
    a2ensite sdc.com
    apache2ctl configtest
    /etc/init.d/apache2 reload

Now we are interested in setup a Basic Authentication mechanism in our
WebDav server. Enable the authentication module and create the password
file

.. code::

    htpasswd -c /etc/apache2/passwd/passwords root

You will be prompted to introduce the password: temporal

After, we introduce the WebDAV section into the Virtual host:

.. code::

    # Note Alias goes to our DocumentRoot. Alias /webdav /opt/sdc/webdav
    # But we apply different settings
    <Location /webdav>
      Dav on
      AuthType Basic  
      AuthName "SDC Server Webdav"
      AuthUserFile /etc/apache2/passwd/passwords
      Require user root

We reconfigure apache and reload it

.. code::

    apache2ctl configtest /etc/init.d/apache2 reload

In order to test if the webdav has been configured in a good way, with a
explorer go to `http://{IP}/webdav/ <http://{IP}/webdav/>`__. Finally,
create the directories product and application in the webdav. This
directories will be visible trough the url:
`http://{IP}/webdav/product <http://{IP}/webdav/product>`__ and
`http://{IP}/webdav/application <http://{IP}/webdav/application>`__

Configure SDC application
^^^^^^^^^^^^^^^^^^^^^^^^^

Once the prerequisites are satisfied, you change the context file. To do
that, change sdc.xml found in distribution file and store it in folder
$SDC\_HOME/webapps/.

See the snipet bellow to know how it works:

.. code::

    <New id="sdc" class="org.eclipse.jetty.plus.jndi.Resource">
        <Arg>jdbc/sdc</Arg>
        <Arg>

            <New class="org.postgresql.ds.PGSimpleDataSource">
                <Set name="User"> <database user> </Set>
                <Set name="Password"> <database password> </Set>
                <Set name="DatabaseName"> <database name>   </Set>
                <Set name="ServerName"> <IP/hostname> </Set>
                <Set name="PortNumber">5432</Set>
            </New>

        </Arg>
    </New>

You also have to add the provided scripts found in the dist file (in
folder /opt/sdc/scripts/) in the same folder (or everywhere you want if
you prefer to change the default configuration).

Configure SDC application
^^^^^^^^^^^^^^^^^^^^^^^^^

The configuration of SDC is in configuration\_properties table. There,
it is required to configure:

-  openstack-tcloud.keystone.url: This is the url where the
   keystone-proxy is deployed
-  openstack-tcloud.keystone.user: the admmin user
-  openstack-tcloud.keystone.password: the admin password
-  openstack-tcloud.keystone.tenant: the admin tenant
-  sdc\_manager\_url: the final url, mainly http://sdc-ip:8080/sdc

The last step is to create a sdc client in the chef-server, so that, the
SDC can communicate with the chef-server. To do that, we can use the
chef-server-web-ui, which is usually deployed on https://chef-server-ip,
go to https://chef-server-ip/clients and create a sdc client as
administrator. Then, it is required to copy the private key.

In the sdc machine, it is required to copy this private key in
/etc/chef/sdc.pem (you can configure the path also in the properties)

Register SDC application into keystone
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The last step involves to regiter the SDC and chef-server endpoints into
the keystone endpoint catalogue. To do that, you should write into the
config.js in the keystone-proxy the following lines:

.. code::

     {"endpoints": [
        {"adminURL": "http://sdc-ip:8080/sdc/rest",
        "region": "myregion",
        "internalURL": "http://sdc-ip:8080/sdc/rest",
        "publicURL": "http://sdc-ip:8080/sdc/rest"
        }
        ],
        "endpoints_links": [],
        "type": "sdc",
        "name": "sdc"
    },
    {"endpoints": [
        {"adminURL": "https://chef-server-ip",
        "region": "myregion",
        "internalURL": "https://chef-server-ip",
        "publicURL": "https://chef-server-ip"
        }
        ],
        "endpoints_links": [],
        "type": "chef-server",
        "name": "chef-server"
    },

where myregion should be the name of the openstack region defined.

Creating images sdc-aware
-------------------------

The images to be deployed by the SDC, should have some features, like to
have the chef-client installed and configured correctly with the
chef-server. In the roadmap, it is considered to avoid all this process
and to make possible any image to be SDC-aware, installing and
configuring everything in booting status.

.. code::

    mkdir /etc/chef
    mkdir /var/log/chef
    curl -L https://www.opscode.com/chef/install.sh | bash

You should copy the chef-validator.pem from the chef-server into
/etc/chef

Then, it is required to create a file called client.rb in /etc/chef. The
validation.pem should be obtained from the chef-server in the folder
/etc/chef-server and its called chef-validator.pem and rename to
validation.pem in the /etc/chef folder of the image

.. code::

    log_location           "/var/log/chef/client.log"
    ssl_verify_mode        :verify_none
    validation_client_name "chef-validator"
    validation_key         "/etc/chef/validation.pem"
    client_key             "/etc/chef/client.pem"
    chef_server_url        "https://cher-server-ip"

Finally, to start chef-client in boot time

.. code::

    chef-client -i 60 -s 6

.. |Build Status| image:: https://travis-ci.org/telefonicaid/fiware-sdc.svg
   :target: https://travis-ci.org/telefonicaid/fiware-sdc
.. |Coverage Status| image:: https://coveralls.io/repos/telefonicaid/fiware-sdc/badge.png?branch=develop
   :target: https://coveralls.io/r/telefonicaid/fiware-sdc
.. |help stackoverflow| image:: http://b.repl.ca/v1/help-stackoverflow-orange.png
   :target: http://www.stackoverflow.com

Configuring the HTTPS certificate
---------------------------------

The service is configured to use HTTPS to secure the communication between clients and the server. One central point in HTTPS security is the certificate which guarantee the server identity.

Quickest solution: using a self-signed certificate
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

The service works "out of the box" against passive attacks (e.g. a sniffer) because a self-signed certificated is generated automatically when the RPM is installed. Any certificate includes a special field call "CN" (Common name) with the identity of the host: the generated certificate uses as identity the IP of the host.

The IP used in the certificate should be the public IP (i.e. the floating IP). The script which generates the certificate knows the public IP asking to an Internet service (http://ifconfig.me/ip). Usually this obtains the floating IP of the server, but of course it wont work without a direct connection to Internet.

If you need to regenerate a self-signed certificate with a different IP address (or better, a convenient configured hostname), please run:

.. code::

    /opt/fiware-sdc/bin/generateselfsigned.sh myhost.mydomain.org

By the way, the self-signed certificate is at /etc/keystorejetty. This file wont be overwritten although you reinstall the package. The same way, it wont be removed automatically if you uninstall de package.

Advanced solution: using certificates signed by a CA
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Although a self-signed certificate works against passive attack, it is not enough by itself to prevent active attacks, specifically a "man in the middle attack" where an attacker try to impersonate the server. Indeed, any browser warns user against self-signed certificates. To avoid these problems, a certificate conveniently signed by a CA may be used.

If you need a certificate signed by a CA, the more cost effective and less intrusive practice when an organization has several services is to use a wildcard certificate, that is, a common certificate among all the servers of a DNS domain. Instead of using an IP or hostname in the CN, an expression as "*.fiware.org" is used.

This solution implies:

* The service must have a DNS name in the domain specified in the wildcard certificate. For example, if the domain is "*.fiware.org" a valid name may be "sdc.fiware.org".
* The clients should use this hostname instead of the IP
* The file /etc/keystorejetty must be replaced with another one generated from the wildcard certificate, the corresponding private key and other certificates signing the wild certificate.

It's possible that you already have a wild certificate securing your portal, but Apache server uses a different file format. A tool is provided to import a wildcard certificate, a private key and a chain of certificates, into /etc/keystorejetty:

.. code::

    # usually, on an Apache installation, the certificate files are at /etc/ssl/private
    /opt/fiware-sdc/bin/importcert.sh key.pem cert.crt chain.crt

If you have a different configuration, for example your organization has got its own PKI, please refer to: http://docs.codehaus.org/display/JETTY/How%2bto%2bconfigure%2bSSL

