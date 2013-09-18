#Tomcat startup
#execute "#node[:sdc][:tomcat_home]/bin/startup.sh" do
#  command "#node[:sdc][:tomcat_home]/bin/startup.sh" 
#  action :run
#end

script "Tomcat start" do
  interpreter "bash"
  user "root"
  cwd "/tmp"
  code <<-EOH
  #{node[:sdc][:tomcat_home]}/bin/startup.sh
  EOH
end
