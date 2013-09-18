node.default[:sdc][:version] = "1.1.0"
node.default[:sdc][:action] = "install"

include_recipe "sdc::install_postgresql_tomcat"
