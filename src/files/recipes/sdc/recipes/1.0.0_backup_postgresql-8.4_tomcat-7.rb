node.default[:sdc][:version] = "1.0.0"
node.default[:sdc][:action] = "backup"
include_recipe "sdc::backup_postgresql_tomcat"
