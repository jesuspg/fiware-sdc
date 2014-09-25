#
# SDC Testing
# Cookbook for testing purposes
# Recipe: Install
#




script "uninstall" do
  interpreter "bash"
  user "root"
  cwd "/tmp"
  code <<-EOH
  echo "Operation: uninstall; Product: #{node['product_name']}; Version: 1.2.3; Att01: #{node['custom_att_01']}; Att02: #{node['custom_att_02']}" >> uninstall_log
  rm #{node['product_name']}_1.2.3_chef
  EOH
end

