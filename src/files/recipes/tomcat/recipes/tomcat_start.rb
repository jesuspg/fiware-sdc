
script "Tomcat start" do
  interpreter "bash"
  user "root"
  cwd "/tmp"
  code <<-EOH
  #{node[:tomcat][:tomcat_home]}/bin/startup.sh
  EOH
end