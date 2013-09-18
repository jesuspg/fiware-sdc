#
# Cookbook Name:: war
# Recipe:: default
#
# Copyright 2011, YOUR_COMPANY_NAME
#
# All rights reserved - Do Not Redistribute
#

#Execution of the download files from webdav

script "download_files" do
  interpreter "bash"
  user "root"
  cwd "/tmp"
  code <<-EOH
    mkdir -p /tmp/sdc/
    cd /tmp/sdc/
    wget -nd -r -l 1 --user #{node[:sdc][:webdav_user]} --password #{node[:sdc][:webdav_password]} #{node[:sdc][:webdav_url]}applications/sdc/#{node[:sdc][:version]}/#{node[:sdc][:action]}/
  EOH
end


#Move the scrips to /opt/...
script "copy_files" do
  interpreter "bash"
  user "root"
  cwd "/tmp"
  code <<-EOH
    mkdir -p /opt/sdc/app/sdc/#{node[:sdc][:version]}/#{node[:sdc][:action]}/
    mv /tmp/sdc/*  /opt/sdc/app/sdc/#{node[:sdc][:version]}/#{node[:sdc][:action]}/
  EOH
end


