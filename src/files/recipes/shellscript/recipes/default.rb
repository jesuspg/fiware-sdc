#
# Cookbook Name:: shellscript
# Recipe:: default
#
# Copyright 2011, YOUR_COMPANY_NAME
#
# All rights reserved - Do Not Redistribute
#Mofiied 

#template "/root/shellscripterb.sh" do
#  source "shellscript.sh.erb"
#  mode "0744"
#jjjend

#Funciona. Saca el output por panatalla STDOUT
c = system("echo HOLA") do
  action :run
end

#No funciona
e = system("/root/shellscript.sh") do
  action :run
end

Chef::Log.info("Before the script")

script "install_something" do
  interpreter "bash"
  user "root"
  cwd "/tmp"
  code <<-EOH
  echo "HOLA HOLA HOLA"
  /root/shellscript.sh
  EOH
end
