#
# Cookbook Name:: tomcat
# Recipe:: 6.0_install
#
# Copyright 2011, Telefonica I+D
#
# Author: Jesus M. Movilla
#
# All rights reserved - Do Not Redistribute

script "install_tomcat6.0.33" do
  interpreter "bash"
  user "root"
  cwd node["tomcat6.0"]["base_install"] 
  code <<-EOH
  if [ -d /opt/apache-tomcat ]
  then
  echo "Already installed"
  else 
  wget --user root --password temporal http://109.231.82.11/webdav/product/tomcat/6/apache-tomcat-6.0.33.tar.gz
  gunzip apache-tomcat-6.0.33.tar.gz
  tar xvf apache-tomcat-6.0.33.tar
  mv apache-tomcat-6.0.33 apache-tomcat
  fi
  EOH
end

script "Tomcat start" do
  interpreter "bash"
  user "root"
  cwd "/tmp"
  code <<-EOH
  export JRE_HOME=/usr/lib/jvm/java-6-sun-1.6.0.26/jre
  if [ -d /opt/apache-tomcat ]
  then
  /opt/apache-tomcat/bin/startup.sh
  fi
  EOH
end
