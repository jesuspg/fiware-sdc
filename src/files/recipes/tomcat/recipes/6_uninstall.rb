#
# Cookbook Name:: tomcat
# Recipe:: 6_uninstall
#
# Copyright 2011, Telefonica I+D
#
# Author: Jesus M. Movilla
#
# All rights reserved - Do Not Redistribute


#script "Tomcat stop" do
#  interpreter "bash"
#  user "root"
#  cwd "/tmp"
#  code <<-EOH
#  export JRE_HOME=/usr/lib/jvm/java-6-sun-1.6.0.26/jre
#  if [ -f /opt/apache-tomcat/bin/shutdown.sh ] 
#  then 
#  /opt/apache-tomcat/bin/shutdown.sh
#  fi 
# EOH
#end

include_recipe "tomcat::tomcat_stop"

script "uninstall_tomcat6.0.32" do
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


