# FI-WARE SDC [![Build Status](https://travis-ci.org/telefonicaid/fiware-sdc.svg)](https://travis-ci.org/telefonicaid/fiware-sdc) [![Coverage Status](https://coveralls.io/repos/jesuspg/fiware-sdc/badge.png)](https://coveralls.io/r/jesuspg/fiware-sdc)

## Software Deployment and Configuration



## Building instructions
It is a a maven application:

- Compile, launch test and build all modules

        $ mvn clean install
- Create a zip with distribution in target/sdc-server-dist.zip

        $ mvn assembly:assembly -DskipTests

- You can generate a rpm o debian packages (using profiles in pom)

    for debian/ubuntu:

        $ mvn install -Pdebian -DskipTests
        (created target/sdc-server-XXXXX.deb)

    for centOS:

      $ mvn package -P rpm -DskipTests
      (created target/rpm/sdc/RPMS/noarch/fiware-sdc-XXXX.noarch.rpm)

