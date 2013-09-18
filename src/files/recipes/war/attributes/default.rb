# Where the various parts of tomcat6 are
# General settings

#Apache Home
default[:war][:tomcat_home] = "/opt/apache-tomcat-7.0.14"

#SDC install home.
default[:war][:application_context] = "sdc"
default[:war][:driver_class_name] = "org.postgresql.Driver"
default[:war][:sdc_install_home] = "/opt/sdc/app/war/sdc/install"
default[:war][:sdc_uninstall_home] = "/opt/sdc/app/war/sdc/uninstall"
default[:war][:sdc_home] = "/opt/sdc/app/war/sdc"
default[:war][:sdc_home_scripts] = "/opt/sdc/scripts"
default[:war][:sdc_war_name] = "sdc-server-rest-api.war"

#Postgresql attributes
#Postgresql url
default[:war][:postgresql_url] = "jdbc:postgresql://localhost:5432/sdc"
#User postgresql is listening
default[:war][:postgresql_db_user] = "postgres"
#Password postgresql is listening
default[:war][:postgresql_db_password] = "postgres"
#Postgresql url
default[:war][:postgresql_url] = "jdbc:postgresql://localhost:5432/sdc"
