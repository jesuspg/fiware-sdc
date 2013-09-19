
script "Tomcat stop" do
  interpreter "bash"
  user "root"
  cwd "/tmp"
  code <<-EOH
  #{node[:tomcat][:tomcat_home]}/bin/shutdown.sh
  EOH
end
