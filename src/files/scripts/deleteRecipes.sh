#!/bin/bash

echo "$1"
echo "$2"

cd /root/chef-repo
#knife cookbook delete "$1" "$2" -y
knife cookbook delete "$1" -y

rm -r /root/chef-repo/cookbooks/"$1"
