# Where the various parts of tomcat6 are
# General settings

#Apache Home
default[:sdc][:tomcat_home] = "/opt/apache-tomcat"

#SDC install home.
default[:sdc][:application_context] = "sdc"
default[:war][:driver_class_name] = "org.postgresql.Driver"
#default[:war][:sdc_uninstall_home] = "/opt/sdc/app/war/sdc/0.4.0/uninstall"
default[:sdc][:war_name] = "sdc-server-rest-api.war"

#Postgresql attributes
#Postgresql url
default[:sdc][:postgresql_url] = "jdbc:postgresql://localhost:5432/sdc"
#User postgresql is listening
default[:sdc][:postgresql_db_user] = "postgres"
#Password postgresql is listening
default[:sdc][:postgresql_db_password] = "postgres"
#Postgresql url
default[:sdc][:postgresql_url] = "jdbc:postgresql://localhost:5432/sdc"

#Webdav url
default[:sdc][:webdav_url] = "http://109.231.82.11/webdav/"
#Webdav user
default[:sdc][:webdav_user] = "root"
#Webdav password
default[:sdc][:webdav_password] = "temporal"
