#
# Cookbook Name:: tomcat
# Attributes:: default
#
# Copyright 2011, .
#
# Author: Jesus M. Movilla
#


default["tomcat5.5"]["url_source"] = "http://apache.rediris.es/tomcat/tomcat-5/v5.5.33/bin/"
default["tomcat5.5"]["base_install"] = "/opt"

default["tomcat6.0"]["url_source"] = "http://apache.rediris.es/tomcat/tomcat-6/v6.0.32/bin/"
default["tomcat6.0"]["base_install"] = "/opt"

default["tomcat7.0"]["url_source"] = "http://apache.rediris.es/tomcat/tomcat-7/v7.0.16/bin/"
default["tomcat7.0"]["base_install"] = "/opt"

default["tomcat"]["port"] = 8080
default["tomcat"]["ssl_port"] = 8443
default["tomcat"]["ajp_port"] = 8009
default["tomcat"]["java_options"] = "-Xmx128M -Djava.awt.headless=true"
default["tomcat"]["use_security_manager"] = false
default["tomcat"]["tomcat_home"] = "/opt/apache-tomcat"