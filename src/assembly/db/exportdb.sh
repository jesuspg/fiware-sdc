#!/bin/bash

#######################################################
###       Script to make an export of a database    ###
#######################################################
HOSTNAME=130.206.80.119
PORT=5432
USERNAME=postgres
FORMAT=custom
BACKUP_FILE=export_sdc.backup
DATABASE_NAME=sdc

function usage() { 
     SCRIPT=$(basename $0) 
     
     printf "\n" >&2 
     printf "usage: ${SCRIPT} [options] \n" >&2 
     printf "\n" >&2 
     printf "Options:\n" >&2 
     printf "\n" >&2 
     printf "    -h                    show usage\n" >&2 
     printf "    -v HOSTNAME           Optional parameter. Virtual Machine where the database is installed. Default value is ${HOSTNAME}\n" >&2 
     printf "    -b BACKUP_FILE        Optional paramdter. File where to keep the database backup. Default value is ${BACKUP_FILE}\n" >&2 
     printf "    -d DATABASE_NAME      Optional parameter. Database Name to make the export from. Default Value is ${DATABASE_NAME}\n" >&2 
     #printf "    -r RELEASE            Optional parameter. Release for product. I.E. 0.ge58dffa \n" >&2 
     printf "\n" >&2 
     exit 1 
}

while getopts ":v:b:d:h" opt 
do 
     case $opt in 
         v) 
             HOSTNAME=${OPTARG} 
             ;; 
         b) 
             BACKUP_FILE=${OPTARG} 
             ;; 
         d) 
             DATABASE_NAME=${OPTARG} 
             ;;
		 h) 
             usage 
             ;; 
         *) 
             echo "invalid argument: '${OPTARG}'" 
             exit 1 
             ;; 
     esac 
done

pg_dump --host=${HOSTNAME} --port=${PORT} --username=${USERNAME} --password --format=custom --blobs --verbose --file=${BACKUP_FILE} ${DATABASE_NAME}

