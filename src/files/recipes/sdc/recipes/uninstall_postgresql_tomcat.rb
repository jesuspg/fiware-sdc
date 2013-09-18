#
# Cookbook Name:: war
# Recipe:: default
#
# Copyright 2011, YOUR_COMPANY_NAME
#
# All rights reserved - Do Not Redistribute
#

#Execution of the preuninstallation script
#execute "uninstall-sdc-pre" do
#  command "#node[:war][:sdc_uninstall_home]/uninstall-sdc-pre.sh"
#  action :run
#end
include_recipe "sdc::tomcat_stop"

#Delete the deployment tomcat descriptor
#directory "{#node[:war][:tomcat_home]}/conf/Catalina/localhost/{#node[:war][:application_context]}.xml" do
file "#{node[:sdc][:tomcat_home]}/conf/Catalina/localhost/#{node[:sdc][:application_context]}.xml" do
  action :delete
  mode 0755
  owner "root"
  group "root"
end

#Move the scrips to /opt/...
script "copy_files" do
  interpreter "bash"
  user "root"
  cwd "/tmp"
  code <<-EOH
    mkdir -p /opt/sdc/app/sdc/#{node[:sdc][:version]}/#{node[:sdc][:action]}/
  EOH
end


#Copy the script to the directory
cookbook_file "/opt/sdc/app/sdc/#{node[:sdc][:version]}/#{node[:sdc][:action]}/uninstall-sdc-postgres.sh" do
  source "uninstall-sdc-postgres.sh"
  mode 0755
end
#Drop the database
script "uninstall-sdc-postgres" do
  interpreter "bash"
  user "root"
  cwd "/tmp"
  code <<-EOH
    /opt/sdc/app/sdc/#{node[:sdc][:version]}/#{node[:sdc][:action]}/uninstall-sdc-postgres.sh
  EOH
end

include_recipe "sdc::tomcat_start"
