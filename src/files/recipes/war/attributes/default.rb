# Where the various parts of tomcat6 are
case platform
when "centos"
  set[:tomcat][:start]           = "/opt/apache-tomcat-7.0.12/bin/catalina.sh restart"  
  set[:tomcat][:webapp_base_dir] = "/opt/apache-tomcat-7.0.12/webapp/"
  set[:tomcat][:webapp_xml_dir]  = "/opt/apache-tomcat-7.0.12/conf/Catalina/localhost/"
  set[:tomcat][:port]            = 8080
  set[:tomcat][:ssl_port]        = 8433
else
  set[:tomcat][:start]           = "/opt/apache-tomcat-7.0.12/bin/catalina.sh restart"  
  set[:tomcat][:webapp_base_dir] = "/opt/apache-tomcat-7.0.12/webapp/"
  set[:tomcat][:port]            = 8080
  set[:tomcat][:ssl_port]        = 8433
end

set_unless[:tomcat][:version]          = "7.0.12"