#!/bin/bash

#sanitized_recipe=`echo -n $2 | tr -d ' '` # Removes unwanted spaces

echo "$1"
echo $2

cd /root/chef-repo
knife node run_list add "$1" 'recipe['$2']'
