script "Update password" do
  interpreter "bash"
  user "root"
  cwd "/tmp"
  code <<-EOH
    sudo -u postgres psql -c "ALTER USER postgres WITH PASSWORD '#{node[:postgresql][:password]}'" -d template1
  EOH
end

 
