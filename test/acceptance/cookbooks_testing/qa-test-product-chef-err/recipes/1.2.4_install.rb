#
# SDC Testing
# Cookbook for testing purposes
# Recipe: Install
#


script "install" do
  interpreter "bash"
  user "root"
  cwd "/tmp"
  code <<-EOH
  echo "Operation: install; Product: #{node['product_name']}; Version: 1.2.4; Att01: #{node['custom_att_01']}; Att02: #{node['custom_att_02'" > #{node['product_name']}_1.2.4_chef
  EOH
end




