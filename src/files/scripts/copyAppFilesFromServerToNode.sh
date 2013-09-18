#!/bin/bash

ssh root@$1 "mkdir -p $2"
scp -r $3 root@$1:$2
