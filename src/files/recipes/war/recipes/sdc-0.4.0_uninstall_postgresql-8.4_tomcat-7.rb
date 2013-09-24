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

script "uninstall-sdc-pre" do
  interpreter "bash"
  user "root"
  cwd "/tmp"
  code <<-EOH
  /opt/sdc/app/war/sdc/0.4.0/uninstall/uninstall-sdc-pre.sh
  EOH
end


#Tomcat stop
#execute "#node[:war][:tomcat_home]/bin/shutdown.sh" do
#  command #node[:war][:tomcat_home]/bin/shutdown.sh"
#  action :run
#end

#Tomcat stop
script "Tomcat stop" do
  interpreter "bash"
  user "root"
  cwd "/tmp"
  code <<-EOH
  /opt/apache-tomcat/bin/shutdown.sh
  EOH
end


#Delete the deployment tomcat descriptor
#directory "{#node[:war][:tomcat_home]}/conf/Catalina/localhost/{#node[:war][:application_context]}.xml" do
file "/opt/apache-tomcat/conf/Catalina/localhost/sdc.xml" do
  action :delete
  mode 0755
  owner "root"
  group "root"
end

#Execution of the script to delete the sdc database
#execute "uninstall-sdc-postgres" do
#  command "#node[:war][:sdc_uninstall_home]/uninstall-sdc-postgres.sh"
#  action :run
#end

#Execution of the script to delete the sdc database
script "uninstall-sdc-postgres" do
  interpreter "bash"
  user "root"
  cwd "/tmp"
  code <<-EOH
  /opt/sdc/app/war/sdc/0.4.0/uninstall/uninstall-sdc-postgres.sh
  EOH
end


#Execution of the postuninstallation script
#execute "uninstall-sdc-post" do
#  command "#node[:war][:sdc_uninstall_home]/uninstall-sdc-post.sh"
#  action :run
#end

#Execution of the postuninstallation script
script "uninstall-sdc-post" do
  interpreter "bash"
  user "root"
  cwd "/tmp"
  code <<-EOH
  /opt/sdc/app/war/sdc/0.4.0/uninstall/uninstall-sdc-post.sh
  EOH
end


#Tomcat startup
#execute "#node[:war][:tomcat_home]/bin/startup.sh" do
#  command #node[:war][:tomcat_home]/bin/startup.sh"
#  action :run
#end

script "Tomcat startup" do
  interpreter "bash"
  user "root"
  cwd "/tmp"
  code <<-EOH
  /opt/apache-tomcat/bin/startup.sh
  EOH
end
