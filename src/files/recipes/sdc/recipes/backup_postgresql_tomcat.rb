#Makes a backup of the data base


script "Backup the DB" do
  interpreter "bash"
  user "root"
  cwd "/tmp"
  code <<-EOH
    sudo -u postgres pg_dump --inserts sdc > /tmp/sdc-dump.sql
  EOH
end

