#Tomcat stop
#execute "#node[:sdc][:tomcat_home]/bin/shutdown.sh" do
#  command "#node[:sdc][:tomcat_home]/bin/shutdown.sh" 
#  action :run
#end

script "Tomcat stop" do
  interpreter "bash"
  user "root"
  cwd "/tmp"
  code <<-EOH
  #{node[:sdc][:tomcat_home]}/bin/shutdown.sh
  EOH
end
