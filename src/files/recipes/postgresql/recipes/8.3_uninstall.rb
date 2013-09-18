node.default[:postgresql][:version] = "8.3"
include_recipe "postgresql::server_uninstall"
