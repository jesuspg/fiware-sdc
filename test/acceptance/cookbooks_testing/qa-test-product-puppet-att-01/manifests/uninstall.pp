class qa-test-product-puppet-att-01::uninstall($version='default_version'){

  notify {"qa-test-product-puppet-att-01_version: ${version}":}
    
  file{"qa-test-product-puppet-att-01_${version}_puppet":
    path => "/tmp/qa-test-product-puppet-att-01_${version}_puppet",
    ensure => absent
  }
}
