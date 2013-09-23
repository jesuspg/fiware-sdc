#!/bin/bash

echo "$1"
echo $2

cd /root/chef-repo
knife node run_list remove "$1" 'role['$2']'
