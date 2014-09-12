#!/bin/bash

#######################################################
###       Script to make an export of a database    ###
#######################################################
HOSTNAME=130.206.80.119
PORT=5432
FORMAT=custom
BACKUP_FILE=/tmp/export_sdc.backup
EXISTING_DATABASE_NAME=postgres

function usage() {
     SCRIPT=$(basename $0)

     printf "\n" >&2
     printf "usage: ${SCRIPT} [options] \n" >&2
     printf "\n" >&2
     printf "Options:\n" >&2
     printf "\n" >&2
     printf "    -h                    show usage\n" >&2
     printf "    -v HOSTNAME           Optional parameter. Virtual Machine where the database is installed. Default value is ${HOSTNAME}\n" >&2
     printf "    -p PORT               Optional parameter. Port where the postgres database listens. Default value is ${PORT}\n" >&2
     printf "    -b BACKUP_FILE        Optional parameter.File where to keep the database backup. Default value is ${BACKUP_FILE}\n" >&2
     printf "\n" >&2
     exit 1
}

while getopts ":v:p:b:h" opt
do
     case $opt in
         v)
             HOSTNAME=${OPTARG}
             ;;
         p)
             PORT=${OPTARG}
             ;;
         b)
             BACKUP_FILE=${OPTARG}
             ;;
         h)
             usage
             ;;
         *)
             echo "invalid argument: '${OPTARG}'"
             echo "add -h argument for help"
             exit 1
             ;;
     esac
done

pg_restore --host=${HOSTNAME} --port=${PORT} -d ${EXISTING_DATABASE_NAME} -C ${BACKUP_FILE}
