#!/bin/bash

echo "$1"

cd /root/chef-repo
knife cookbook upload "$1"