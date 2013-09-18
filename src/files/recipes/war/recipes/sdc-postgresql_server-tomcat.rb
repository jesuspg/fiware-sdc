#
# Cookbook Name:: war
# Recipe:: default
#
# Copyright 2011, YOUR_COMPANY_NAME
#
# All rights reserved - Do Not Redistribute
#

script "install_ApplTomcat" do
  interpreter "bash"
  user "root"
  cwd "/tmp/app/war/sdc/install"
  code <<-EOH
  echo "  export JRE_HOME=/usr/lib/jvm/java-6-sun-1.6.0.22 "
  export JRE_HOME=/usr/lib/jvm/java-6-sun-1.6.0.22 
  
  echo " /tmp/app/war/sdc/install/sdc-pre.sh "
  /tmp/app/war/sdc/install/sdc-pre.sh
 
  echo " /opt/apache-tomcat-7.0.14/bin/catalina.sh stop "
  /opt/apache-tomcat-7.0.14/bin/catalina.sh stop
 
  echo " rm -r /opt/apache-tomcat-7.0.14/webapps/sdc*/"
  echo "  /tmp/app/war/sdc/install/sdc-postgres.sh "
  
  /tmp/app/war/sdc/install/sdc-postgres.sh
  rm -r /opt/apache-tomcat-7.0.14/webapps/sdc*/
 
  echo " mkdir -p /opt/apache-tomcat-7.0.14/conf/Catalina/localhost "
  mkdir -p /opt/apache-tomcat-7.0.14/conf/Catalina/localhost

  echo " cp /tmp/app/war/sdc/install/sdc-tomcat.xml /opt/apache-tomcat-7.0.14/conf/Catalina/localhost/sdc.xml "
  cp /tmp/app/war/sdc/install/sdc-tomcat.xml /opt/apache-tomcat-7.0.14/conf/Catalina/localhost/sdc.xml

  echo " /tmp/app/war/sdc/install/sdc-post.sh "
  /tmp/app/war/sdc/install/sdc-post.sh
  
  echo " /opt/apache-tomcat-7.0.14/bin/catalina.sh start "
  /opt/apache-tomcat-7.0.14/bin/catalina.sh start
  EOH
end
