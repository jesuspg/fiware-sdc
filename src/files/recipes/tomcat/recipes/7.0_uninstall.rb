#
# Cookbook Name:: tomcat
# Recipe:: 7.0_uninstall
#
# Copyright 2011, Telefonica I+D
#
# Author: Jesus M. Movilla
#
# All rights reserved - Do Not Redistribute


script "Tomcat stop" do
  interpreter "bash"
  user "root"
  cwd "/tmp"
  code <<-EOH
  export JRE_HOME=/usr/lib/jvm/java-6-openjdk/jre
  if [ -f /opt/apache-tomcat/bin/shutdown.sh ] 
  then 
  /opt/apache-tomcat/bin/shutdown.sh
  fi 
 EOH
end

script "uninstall_tomcat7.0.16" do
  interpreter "bash"
  user "root"
  cwd "/opt"
  code <<-EOH
  sleep 10
  rm /opt/apache-tomcat-*.gz
  rm /opt/apache-tomcat-*.tar
  rm -r /opt/apache-tomcat
  EOH
end

