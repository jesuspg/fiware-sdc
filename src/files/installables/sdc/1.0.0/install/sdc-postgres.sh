#!/bin/bash

#echo " sudo -u postgres createdb sdc "
#sudo -u postgres createdb sdc

output=$(sudo -u postgres psql -d template1 -t -c "select * from pg_database where datname='sdc'") 
if [ -z "$output" ]; then 
sudo -u postgres createdb sdc
else 
echo 'the db already exists' 
fi 
