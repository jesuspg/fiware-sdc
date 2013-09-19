#
# Cookbook Name:: tomcat
# Recipe:: restore_7.0
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
  if [ -f /opt/apache-tomcat/bin/shutdown.sh ]
  then
  export JRE_HOME=/usr/lib/jvm/java-6-sun-1.6.0.26/jre
  /opt/apache-tomcat/bin/shutdown.sh
  fi
  EOH
end

script "restore_tomcat7.0" do
  interpreter "bash"
  user "root"
  cwd "/opt/apache-tomcat/"
  code <<-EOH
  tar xvf /tmp/conf.tar conf/
  tar xvf /tmp/webapps.tar webapps/
  tar xvf /tmp/logs.tar logs/
  cp /tmp/postgres* lib/
  rm /tmp/conf.tar
  rm /tmp/webapps.tar
  rm /tmp/logs.tar 
  EOH
end

script "Tomcat start" do
  interpreter "bash"
  user "root"
  cwd "/tmp"
  code <<-EOH
  if [ -f /opt/apache-tomcat/bin/startup.sh ]
  then
  export JRE_HOME=/usr/lib/jvm/java-6-sun-1.6.0.26/jre
  /opt/apache-tomcat/bin/startup.sh
  fi
  EOH
end