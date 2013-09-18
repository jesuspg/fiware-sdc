#
# Cookbook Name:: war
# Recipe:: default
#
# Copyright 2011, YOUR_COMPANY_NAME
#
# All rights reserved - Do Not Redistribute


#Execution of the preinstallation script
#execute "sdc-pre" do
  #command "#node[:sdc][:sdc_install_home]/sdc-pre.sh"
#  command "/opt/sdc/app/war/sdc/0.4.0/install/sdc-pre.sh"
#  action :run
#end

include_recipe "sdc::download_files"

include_recipe "sdc::tomcat_stop"


#Delete of the previous sdc webapps deployed in tomcat
directory "#node[:sdc][:tomcat_home]/webapps/sdc*" do
  action :delete
  mode 0755
  owner "root"
  group "root"
end

#Delete of the previous sdc webapps deployed in tomcat
directory "#node[:sdc][:tomcat_home]/work/Catallina/localhost/sdc*" do
  action :delete
  mode 0755
  owner "root"
  group "root"
end

#Copy the script to the directory
cookbook_file "opt/sdc/app/sdc/#{node[:sdc][:version]}/install/sdc-postgres.sh" do
  source "sdc-postgres.sh"
  mode 0755
end

script "Prepare db" do
  interpreter "bash"
  user "root"
  cwd "/tmp"
  code <<-EOH
    mv /opt/sdc/app/sdc/#{node[:sdc][:version]}/install/postgresql*.jar #{node[:sdc][:tomcat_home]}/lib/
    /opt/sdc/app/sdc/#{node[:sdc][:version]}/install/sdc-postgres.sh
  EOH
end



directory "#{node[:sdc][:tomcat_home]}/conf/Catalina/localhost" do
  action :create
  mode 0755
  owner "root"
  group "root"
  recursive true
end

template "#{node[:sdc][:tomcat_home]}/conf/Catalina/localhost/#{node[:sdc][:application_context]}.xml" do
  source "sdc-tomcat.xml.erb"
  group "root"
  owner "root"
  variables(
    :install_home => "/opt/sdc/app/sdc/#{node[:sdc][:version]}/install",
    :application_context =>node[:sdc][:application_context],
    :driver_class_name => node[:sdc][:driver_class_name],
    :war_name => node[:sdc][:war_name],
    :postgresql_url=> node[:sdc][:postgresql_url],
    :postgresql_db_user=> node[:sdc][:postgresql_db_user],
    :postgresql_db_password => node[:sdc][:postgresql_db_password]
  )
  mode 0644
end

include_recipe "sdc::tomcat_start"


