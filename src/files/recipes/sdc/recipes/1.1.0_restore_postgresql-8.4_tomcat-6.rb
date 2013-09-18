node.default[:sdc][:version] = "1.1.0"
node.default[:sdc][:action] = "restore"
include_recipe "sdc::restore_postgresql_tomcat"
