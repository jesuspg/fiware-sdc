class qa-test-product-puppet-att-01::install($version='default_version'){

	notify {"qa-test-product-puppet-att-01_version: ${version}":}

  $custom_att_01=hiera('custom_att_01', 'att_01_default')
  $custom_att_02=hiera('custom_att_02', 'att_02_default')

  file {"qa-test-product-puppet-att-01_${version}_puppet":
	  path => "/tmp/qa-test-product-puppet-att-01_${version}_puppet",
	  ensure => present,
	  mode => 0640,
	  content => "Operation: install; Product: qa-test-product-puppet-att-01; Version: ${version}; Att01: ${custom_att_01}; Att02: ${custom_att_02}"
  }
}
