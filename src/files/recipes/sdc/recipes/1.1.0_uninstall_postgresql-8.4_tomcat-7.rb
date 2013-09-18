node.default[:sdc][:version] = "1.1.0"
node.default[:sdc][:action] = "uninstall"

include_recipe "sdc::uninstall_postgresql_tomcat"
