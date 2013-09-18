#
# Cookbook Name:: tomcat
# Recipe:: 6.0_restore
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
  export JRE_HOME=/usr/lib/jvm/java-6-openjdk/jre
  /opt/apache-tomcat/bin/shutdown.sh
  fi
  EOH
end

script "restore_tomcat6.0" do
  interpreter "bash"
  user "root"
  cwd "/opt/apache-tomcat/"
  code <<-EOH
  tar xvf /tmp/conf.tar conf/
  tar xvf /tmp/webapps.tar webapps/
  tar xvf /tmp/logs.tar logs/
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
  export JRE_HOME=/usr/lib/jvm/java-6-openjdk/jre
  /opt/apache-tomcat/bin/startup.sh
  fi
  EOH
end