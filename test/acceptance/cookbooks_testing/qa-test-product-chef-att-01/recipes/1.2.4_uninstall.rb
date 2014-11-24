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
  echo "Operation: uninstall; Product: #{node['qa-test-product-chef-att-01']['product_name']}; Version: 1.2.4;" \
  "Att01: #{node['qa-test-product-chef-att-01']['custom_att_01']};" \
  "Att02: #{node['qa-test-product-chef-att-01']['custom_att_02']}" >> uninstall_log
  rm #{node['qa-test-product-chef-att-01']['product_name']}_1.2.4_chef
  EOH
end

