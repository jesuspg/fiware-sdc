#
# Cookbook Name:: apache2
# Recipe:: default
#
# Copyright 2008-2009, Opscode, Inc.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#


######

######
#  execute "generate-module-list" do
#    if node[:kernel][:machine] == "x86_64" 
#      libdir = value_for_platform("arch" => { "default" => "lib" }, "default" => "lib64")
#    else 
#      libdir = "lib"
#    end
#    command "/usr/local/bin/apache2_module_conf_generate.pl /usr/#{libdir}/httpd/modules /etc/httpd/mods-available"
#    action :run
#  end
  
#  %w{a2ensite a2dissite a2enmod a2dismod}.each do |modscript|
#    template "/usr/sbin/#{modscript}" do
#      source "#{modscript}.erb"
#      mode 0755
#      owner "root"
#      group "root"
#    end  
#  end

######

######

service "apache2" do
  action :stop
end


######
  
# installed by default on centos/rhel, remove in favour of mods-enabled
  file "#{node[:apache][:dir]}/conf.d/charset" do
    action :delete
    backup false
  end
  file "#{node[:apache][:dir]}/conf.d/security" do
    action :delete
    backup false
  end
  
#directory "#{node[:apache][:dir]}/ssl" do
#  action :delete
#  mode 0755
#  owner "root"
#  group "root"
#  recursive true
#end

#directory "#{node[:apache][:dir]}/conf.d" do
#  action :delete
#  mode 0755
#  owner "root"
#  group "root"
#  recursive true
#end

#directory node[:apache][:cache_dir] do
#  action :delete
#  mode 0755
#  owner node[:apache][:user]
#  recursive true
#end

#directory node[:apache][:log_dir] do
#  action :delete
#  mode 0755
#  owner node[:apache][:user]
#  recursive true
#end

#directory node[:apache][:user] do
#  action :delete
#  mode 0755
#  owner node[:apache][:user]
#  recursive true
#end

#directory node[:apache][:icondir] do
#  action :delete
#  mode 0755
#  owner node[:apache][:user]
#  recursive true
#end


#directory "node[:apache][:log_dir]" do
#  action :delete
#  mode 0755
#  owner "root"
#  group "root"
#  recursive true
# end


#file "node[:apache][:binary]" do
#  owner "root"
#  group "root"
#  mode "0755"
#  action :delete
#end

######

######

#if platform?("centos", "redhat", "fedora", "suse", "arch")
#  directory node[:apache][:log_dir] do
#    mode 0755
#    action :delete
#  end
#end
  
#  cookbook_file "/usr/local/bin/apache2_module_conf_generate.pl" do
#    source "apache2_module_conf_generate.pl"
#    mode 0755
#    owner "root"
#    group "root"
#  end


#  %w{sites-available sites-enabled mods-available mods-enabled}.each do |dir|
#    directory "#{node[:apache][:dir]}/#{dir}" do
#      mode 0755
#      owner "root"
#      group "root"
#      action :delete
#      recursive true
#    end
#  end
  
#directory "node[:apache][:dir]" do
#   action :delete
#   mode 0755
#   owner "root"
#   group "root"
#   recursive true
# end



####

service "apache2" do
  case node[:platform]
  when "centos","redhat","fedora","suse"
    service_name "httpd"
    # If restarted/reloaded too quickly httpd has a habit of failing.
    # This may happen with multiple recipes notifying apache to restart - like
    # during the initial bootstrap.
    restart_command "/sbin/service httpd restart && sleep 1"
    reload_command "/sbin/service httpd reload && sleep 1"
  when "debian","ubuntu"
    service_name "apache2"
    restart_command "/usr/sbin/invoke-rc.d apache2 restart && sleep 1"
    reload_command "/usr/sbin/invoke-rc.d apache2 reload && sleep 1"
  when "arch"
    service_name "httpd"
  end
  supports value_for_platform(
    "debian" => { "4.0" => [ :restart, :reload ], "default" => [ :restart, :reload, :status ] },
    "ubuntu" => { "default" => [ :restart, :reload, :status ] },
    "centos" => { "default" => [ :restart, :reload, :status ] },
    "redhat" => { "default" => [ :restart, :reload, :status ] },
    "fedora" => { "default" => [ :restart, :reload, :status ] },
    "arch" => { "default" => [ :restart, :reload, :status ] },
    "default" => { "default" => [:restart, :reload ] }
  )
  action :disable
end

###

package "apache2" do
  case node[:platform]
  when "centos","redhat","fedora","suse"
    package_name "httpd"
  when "debian","ubuntu"
    package_name "apache2"
  when "arch"
    package_name "apache"
  end
  action :purge
end



 




