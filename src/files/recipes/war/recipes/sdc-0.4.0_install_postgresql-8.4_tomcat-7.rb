#
# Cookbook Name:: war
# Recipe:: default
#
# Copyright 2011, YOUR_COMPANY_NAME
#
# All rights reserved - Do Not Redistribute


#Execution of the preinstallation script
#execute "sdc-pre" do
  #command "#node[:war][:sdc_install_home]/sdc-pre.sh"
#  command "/opt/sdc/app/war/sdc/0.4.0/install/sdc-pre.sh"
#  action :run
#end

script "sdc-pre" do
  interpreter "bash"
  user "root"
  cwd "/tmp"
  code <<-EOH
  /opt/sdc/app/war/sdc/0.4.0/install/sdc-pre.sh
  EOH
end

#Tomcat stop
#execute "#node[:war][:tomcat_home]/bin/shutdown.sh" do
#  command "#node[:war][:tomcat_home]/bin/shutdown.sh" 
#  action :run
#end

script "Tomcat stop" do
  interpreter "bash"
  user "root"
  cwd "/tmp"
  code <<-EOH
  /opt/apache-tomcat/bin/shutdown.sh
  EOH
end

#Delete of the previous sdc webapps deployed in tomcat
directory "#node[:war][:tomcat_home]/webapps/sdc*" do
  action :delete
  mode 0755
  owner "root"
  group "root"
end

#Execution of the script to create the sdc database
#execute "sdc-postgres" do
#  command "#node[:war][:sdc_install_home]/sdc-postgres.sh"
#  action :run
#end

script "sdc-postgres" do
  interpreter "bash"
  user "root"
  cwd "/tmp"
  code <<-EOH
  /opt/sdc/app/war/sdc/0.4.0/install/sdc-postgres.sh
  EOH
end


directory "#{node[:war][:tomcat_home]}/conf/Catalina/localhost" do
  action :create
  mode 0755
  owner "root"
  group "root"
  recursive true
end

template "#{node[:war][:tomcat_home]}/conf/Catalina/localhost/#{node[:war][:application_context]}.xml" do
  source "sdc-tomcat.xml.erb"
  group "root"
  owner "root"
  variables(
    :sdc_install_home => node[:war][:sdc_install_home],
    :application_context =>node[:war][:application_context],
    :driver_class_name => node[:war][:driver_class_name],
    :sdc_war_name => node[:war][:sdc_war_name],
    :postgresql_url=> node[:war][:postgresql_url],
    :postgresql_db_user=> node[:war][:postgresql_db_user],
    :postgresql_db_password => node[:war][:postgresql_db_password]
  )
  mode 0644
end

#Execution of the postinstallation script
#execute "sdc-post" do
#  command "#node[:war][:sdc_install_home]/sdc-post.sh"
#  action :run
#end

#Execution of the postinstallation script
script "sdc-post" do
  interpreter "bash"
  user "root"
  cwd "/tmp"
  code <<-EOH
  /opt/sdc/app/war/sdc/0.4.0/install/sdc-post.sh
  EOH
end


#Tomcat startup
#execute "#node[:war][:tomcat_home]/bin/startup.sh" do
#  command "#node[:war][:tomcat_home]/bin/startup.sh" 
#  action :run
#end

script "Tomcat start" do
  interpreter "bash"
  user "root"
  cwd "/tmp"
  code <<-EOH
  /opt/apache-tomcat/bin/startup.sh
  EOH
end


