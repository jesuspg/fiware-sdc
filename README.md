# FI-WARE SDC [![Build Status](https://travis-ci.org/telefonicaid/fiware-sdc.svg)](https://travis-ci.org/telefonicaid/fiware-sdc) [![Coverage Status](https://coveralls.io/repos/jesuspg/fiware-sdc/badge.png)](https://coveralls.io/r/jesuspg/fiware-sdc) [![help stackoverflow](http://b.repl.ca/v1/help-stackoverflow-orange.png)](http://www.stackoverflow.com)

## Software Deployment and Configuration



###SDC Build and Installation

This guide tries to define the procedure to build and install the SDC  in a machine, including its requirements and possible troubleshooting that we could find during the installation. We have to talk about two nodes, one including the core functionality of the enabler itself and the other one which allocated the chef server.

### Requirements

In order to execute the SDC, it is needed to have previously installed the following software:

* Chef node
 * Chef server [http://wiki.opscode.com/display/chef/Installing+Chef+Server]. For CentOS it is possible to follow the following instructions[http://blog.frameos.org/2011/05/19/installing-chef-server-0-10-in-rhel-6-scientificlinux-6/]

* SDC node
 * open jdk 7
 * Tomcat  7.X.X [http://tomcat.apache.org/download-70.cgi]
 * PostgreSQL [http://www.postgresql.org/]
 * Webdav


### Building instructions
It is a a maven application:

- Compile, launch test and build all modules

        $ mvn clean install
- Create a zip with distribution in target/sdc-server-dist.zip

        $ mvn assembly:assembly -DskipTests

- You can generate a rpm o debian packages (using profiles in pom)

    for debian/ubuntu:

        $ mvn install -Pdebian -DskipTests
        (created target/sdc-server-XXXXX.deb)

    for centOS:

      $ mvn package -P rpm -DskipTests
      (created target/rpm/sdc/RPMS/noarch/fiware-sdc-XXXX.noarch.rpm)


## Installation instructions
### Chef server
#### Chef server Installation
The installation of the chef-server involves to install the chef-server package, which can be obtained in [http://www.getchef.com/chef/install/]. We can just execute

    rpm -Uvh chef-server-package.rpm
Verify the the hostname for the Chef server by running the hostname command. The hostname for the Chef server must be a FQDN. This means hostaname.domainame. In case it is not configure, you can do it

    hostname chef-server.localdomain
and include it in the /etc/hosts
After that, it is required to configure the certificates and other staff in the chef-server, with chef-server-ctl. This command will set up all of the required components, including Erchef, RabbitMQ, and PostgreSQL.

    sudo chef-server-ctl reconfigure
In order to test verify the installation of Chef Server 11.x by running the following command:

    sudo chef-server-ctl test
After that, you can obtain the different certificates for the different clients in /etc/chef-server. There you can find a chef-validator.pem (needed for all the nodes), the chef-server-gui for the GUI.. You can copy them in order to use them later.
The next step is to configure a client in the chef-server so that you can execute the chef-server CLI. To do that, you need to install the chef-client

    curl -L https://www.opscode.com/chef/install.sh | sudo bash
and configure it with the following command. You can accept all the default

    knife configure --initial
The validation_key attribute in the knife.rb file must specify the path to the validation key. The validation_client_name attribute defaults to chef-validator (which is the chef-validator.pem private key created by the open source Chef server on startup). When prompted for the URL for the Chef server, use the FQDN for the Chef server
Once you have a client configured, you can run the CLI. Just one example:

    knife client list

#### Chef server cookbook repository
The FI-WARE cookbook repository is in FI-WARE SVN repository. To upload the recipes into the chef server you need:
* To dowload the svn repository:
  svn checkout  https://forge.fi-ware.org/scmrepos/svn/testbed/trunk/cookbooks
* Inside the cookbooks folder, create a file update with the following content. It will update the repository and upload into the chef-server
  svn update
  knife cookbook upload --all -o BaseRecipes/
  knife cookbook upload --all -o BaseSoftware/
  knife cookbook upload --all -o GESoftware/

### Database configuration
The SDC node needs to have PostgreSQL installed in service mode and a database created called SDC. For CentOS, these are the instructions:
Firsly, it is required to install the PostgreSQL [http://wiki.postgresql.org/wiki/YUM_Installation].
<pre style="white-space: pre-wrap;
white-space: -moz-pre-wrap; white-space: -pre-wrap; white-space: -o-pre-wrap;
word-wrap: break-word">
yum install postgreql postgresql-server posgresql-cotrib
</pre>

#### Start Postgresql

Type the following commands to install the postgresql as service and restarted
<pre>
# chkconfig --add postgresql
# chkconfig postgresql on
# service postgresql start
</pre>

Then, you need to configure postgresql to allow for accessing. In /var/lib/pgsql/data/postgresql.conf
  listen_addresses = '0.0.0.0'
in /var/lib/pgsql/data/pg_hba.conf, you need to add
  host    all         all          0.0.0.0/0            md5
Restart the postgres
  service postgresql restart

#### Create the DB
Connect to Postgresql Server using
<pre>
# su - postgres
</pre>

Connect as postgres to the postgres database and set the password for user postgres using alter user as below:
<pre>
$ psql postgres postgres;
$ alter user postgres with password 'postgres';
</pre>

Create the SDC DB
<pre>
createdb sdc
</pre>

Check that the database has been created correctly:
  su - postgres
  psql -U postgres sdc -h localhost

Then we need to create the database tables for the sdc. To do that obtain the files from [https://github.com/telefonicaid/fiware-sdc/blob/develop/migrations/src/main/resources] and execute
  psql -d sdc -a -f db-initial.sql
  psql -d sdc -a -f db-changelog.sql


#### Install and configure WebDav

These instructions are based on [http://www.howtoforge.com/how-to-set-up-webdav-with-apache2-on-centos-5.5]

The webdav is the component of the SDC-Server that stores the installables of the available software (products and applications) to be installed in the nodes.

Make sure Apache2 is installed and the optional DAV modules are enabled


     yum install httpd
     vi /etc/httpd/conf/httpd.conf
     [...] LoadModule dav_module modules/mod_dav.so
     [...] LoadModule dav_fs_module modules/mod_dav_fs.so [...]

Then create the system startup links for Apache and start it:

    chkconfig --levels 235 httpd on
    /etc/init.d/httpd start<pre>

Create a Virtual host in /etc/apache2/sites-available/sdc.com

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

We need now to create the directory where all the files managed by our WebDav are going to be:

    mkdir /opt/sdc/webdav
    chown www-data /opt/sdc/webdav
    a2ensite sdc.com
    apache2ctl configtest
    /etc/init.d/apache2 reload

Now we are interested in setup a Basic Authentication mechanism in our WebDav server. Enable the authentication module and create the password file

    htpasswd -c /etc/apache2/passwd/passwords root

You will be prompted to introduce the password: temporal

After, we introduce the WebDAV section into the Virtual host:

    # Note Alias goes to our DocumentRoot. Alias /webdav /opt/sdc/webdav
    # But we apply different settings
    <Location /webdav>
      Dav on
      AuthType Basic  
      AuthName "SDC Server Webdav"
      AuthUserFile /etc/apache2/passwd/passwords
      Require user root
   </Location>

We reconfigure apache and reload it

    apache2ctl configtest /etc/init.d/apache2 reload

In order to test if the webdav has been configured in a good way, with a explorer go to http://{IP}/webdav/. Finally, create the directories product and application in the webdav. This directories will be visible trough the url: http://{IP}/webdav/product and http://{IP}/webdav/application

#### Configure SDC application
Once the prerequisites are satisfied, you change the context file. To do that, change sdc.xml found in distribution file and store it in folder $SDC_HOME/webapps/.

See the snipet bellow to know how it works:


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


You also have to add the provided scripts found in the dist file (in folder /opt/sdc/scripts/) in the same folder (or everywhere you want if you prefer to change the default configuration).


#### Configure SDC application
The configuration of SDC is in configuration_properties table. There, it is required to configure:
* openstack-tcloud.keystone.url: This is the url where the keystone-proxy is deployed
* openstack-tcloud.keystone.user: the admmin user
* openstack-tcloud.keystone.password: the admin password
* openstack-tcloud.keystone.tenant: the admin tenant
* sdc_manager_url: the final url, mainly http://sdc-ip:8080/sdc

The last step is to create a sdc client in the chef-server, so that, the SDC can communicate with the chef-server. To do that, we can use the chef-server-web-ui, which is usually deployed on https://chef-server-ip, go to https://chef-server-ip/clients and create a sdc client as administrator. Then, it is required to copy the private key.

In the sdc machine, it is required to copy this private key in /etc/chef/sdc.pem (you can configure the path also in the properties)

#### Register SDC application into keystone
The last step involves to regiter the SDC and chef-server endpoints into the keystone endpoint catalogue. To do that, you should write into the config.js in the keystone-proxy the following lines:

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

## Creating images sdc-aware
The images to be deployed by the SDC, should have some features, like to have the chef-client installed and configured correctly with the chef-server. In the roadmap, it is considered to avoid all this process and to make possible any image to be SDC-aware, installing and configuring everything in booting status.

    mkdir /etc/chef
    mkdir /var/log/chef
    curl -L https://www.opscode.com/chef/install.sh | bash

You should copy the chef-validator.pem from the chef-server into /etc/chef

Then, it is required to create a file called client.rb in /etc/chef. The validation.pem should be obtained from the chef-server in the folder /etc/chef-server and its called chef-validator.pem and rename to validation.pem in the /etc/chef folder of the image

    log_location           "/var/log/chef/client.log"
    ssl_verify_mode        :verify_none
    validation_client_name "chef-validator"
    validation_key         "/etc/chef/validation.pem"
    client_key             "/etc/chef/client.pem"
    chef_server_url        "https://cher-server-ip"


Finally, to start chef-client in boot time

    chef-client -i 60 -s 6
