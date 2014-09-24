class qa-test-product-puppet-01::install($version='default_version', $custom_att_01='att_01_default', $custom_att_02='att_02_default'){

	notify {"qa-test-product-puppet-01_version: ${version}":}

  file {"qa-test-product-puppet-01_${version}_puppet":
	  path => "/tmp/qa-test-product-puppet-01_${version}_puppet",
	  ensure => present,
	  mode => 0640,
	  content => "Operation: install; Product: qa-test-product-puppet-01; Version: ${version}; Att01: ${custom_att_01}; Att02: ${custom_att_02}"
  }
}
