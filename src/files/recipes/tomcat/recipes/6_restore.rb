#
# Cookbook Name:: tomcat
# Recipe:: 6_restore
#
# Copyright 2011, Telefonica I+D
#
# Author: Jesus M. Movilla
#
# All rights reserved - Do Not Redistribute

script "restore_tomcat6.0" do
  interpreter "bash"
  user "root"
  cwd "/opt/apache-tomcat/"
  code <<-EOH
  sleep 10
  cp /tmp/postgresql* /opt/apache-tomcat/lib/
  tar xvf /tmp/conf.tar conf/
  #tar xvf /tmp/webapps.tar webapps/
  #tar xvf /tmp/logs.tar logs/
  #rm /tmp/conf.tar
  #rm /tmp/webapps.tar
  #rm /tmp/logs.tar 
  sleep 10
  EOH
end

include_recipe "tomcat::tomcat_stop"
include_recipe "tomcat::tomcat_start"

#script "Tomcat stop" do
#  interpreter "bash"
#  user "root"
#  cwd "/tmp"
#  code <<-EOH
#  sleep 10
#  if [ -f /opt/apache-tomcat/bin/shutdown.sh ]
#  then
#  export JRE_HOME=/usr/lib/jvm/java-6-sun-1.6.0.26/jre
#  /opt/apache-tomcat/bin/shutdown.sh
#  fi
#  sleep 10
#  EOH
#end

#script "Tomcat start" do
#  interpreter "bash"
#  user "root"
#  cwd "/tmp"
#  code <<-EOH
#  sleep 10
#  if [ -f /opt/apache-tomcat/bin/startup.sh ] 
#  then
#  export JRE_HOME=/usr/lib/jvm/java-6-sun-1.6.0.26/jre
#  /opt/apache-tomcat/bin/startup.sh
#  fi 
#  EOH
#end