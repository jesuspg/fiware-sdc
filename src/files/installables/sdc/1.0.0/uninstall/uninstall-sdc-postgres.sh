#!/bin/bash

#echo " sudo -u postgres dropdb sdc "
#sudo -u postgres dropdb sdc

output=$(sudo -u postgres psql -d template1 -t -c "select * from pg_database where datname='sdc'") 
if [ -z "$output" ]; then 
echo 'the db does not exist yet' 
else 
sudo -u postgres dropdb sdc
fi 
