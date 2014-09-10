#!/bin/bash

#######################################################
###       Script to make an import of a database    ###
#######################################################
HOSTNAME=130.206.80.119
PORT=5432
USERNAME=postgres
FORMAT=custom
BACKUP_FILE=export_sdc.backup
EXISTING_DATABASE_NAME=postgres

function usage() {
     SCRIPT=$(basename $0)

     printf "\n" >&2
     printf "usage: ${SCRIPT} [options] \n" >&2
     printf "\n" >&2
     printf "Options:\n" >&2
     printf "\n" >&2
     printf "    -h                    show usage\n" >&2
     printf "    -v HOSTNAME           Optional paramter. Virtual Machine where the database is installed. Default value is ${HOSTNAME}\n" >&2
     printf "    -b BACKUP_FILE        Optional paramter.File where to keep the database backup. Default value is ${BACKUP_FILE}\n" >&2
     #printf "    -d DATABASE_NAME      Mandatory paramter. Database Name to make the export from\n" >&2
     #printf "    -r RELEASE            Optional parameter. Release for product. I.E. 0.ge58dffa \n" >&2
     printf "\n" >&2
     exit 1
}

while getopts ":v:b:h" opt
do
     case $opt in
         v)
             HOSTNAME=${OPTARG}
             ;;
         b)
             BACKUP_FILE=${OPTARG}
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

pg_restore --host=${HOSTNAME} --port=${PORT} --username=${USERNAME} --password -d ${EXISTING_DATABASE_NAME} -C ${BACKUP_FILE}
#pg_restore --host=130.206.80.119 --port=5432 --username=postgres --password -d postgres -C desarrollo_sdc_test_export.backup
