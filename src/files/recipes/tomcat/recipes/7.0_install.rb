#
# Cookbook Name:: tomcat
# Recipe:: 7.0_install
#
# Copyright 2011, Telefonica I+D
#
# Author: Jesus M. Movilla
#
# All rights reserved - Do Not Redistribute

script "install_tomcat7.0.16" do
  interpreter "bash"
  user "root"
  cwd "/opt"
  code <<-EOH
  if [ -d /opt/apache-tomcat ]
  then
  echo "Already installed"
  else 
  wget http://apache.rediris.es/tomcat/tomcat-7/v7.0.16/bin/apache-tomcat-7.0.16.tar.gz
  gunzip apache-tomcat-7.0.16.tar.gz
  tar xvf apache-tomcat-7.0.16.tar
  mv apache-tomcat-7.0.16 apache-tomcat
  fi
  EOH
end

script "Tomcat start" do
  interpreter "bash"
  user "root"
  cwd "/tmp"
  code <<-EOH
  if [ -f /opt/apache-tomcat-7.0.16/bin/startup.sh ]
  then
    export JRE_HOME=/usr/lib/jvm/java-6-openjdk/jre
   /opt/apache-tomcat/bin/startup.sh
  fi
  EOH
end
