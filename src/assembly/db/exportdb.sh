#!/bin/bash

#######################################################
###       Script to make an export of a database    ###
#######################################################
FORMAT=custom
BACKUP_FILE=/tmp/export_sdc.backup

vflag=false
pflag=false
dflag=false

function usage() { 
     SCRIPT=$(basename $0) 
     
     printf "\n" >&2 
     printf "usage: ${SCRIPT} [options] \n" >&2 
     printf "\n" >&2 
     printf "Options:\n" >&2 
     printf "\n" >&2 
     printf "    -h                    show usage\n" >&2 
     printf "    -v HOSTNAME           Mandatory parameter. Virtual Machine where the database is installed.\n" >&2 
     printf "    -p PORT               Mandatory parameter. Port where the postgres database listens.\n" >&2 
     printf "    -b BACKUP_FILE        Mandatory paramdter. File where to keep the database backup.\n" >&2 
     printf "    -d DATABASE_NAME      Mandatory parameter. Database Name to make the export from.\n" >&2 
     printf "\n" >&2 
     exit 1 
}

while getopts ":v:p:b:d:h" opt 
do 
     case $opt in 
         v) 
             vflag=true;HOSTNAME=${OPTARG} 
             ;; 
         p) 
             pflag=true;PORT=${OPTARG} 
             ;; 
         b) 
             BACKUP_FILE=${OPTARG} 
             ;; 
         d) 
             dflag=true;DATABASE_NAME=${OPTARG} 
             ;;
		 h) 
             usage 
             ;; 
         *) 
             echo "invalid argument: '${OPTARG}'\n"
	     echo "add -h argument for help" 
             exit 1 
             ;; 
     esac 
done

if ! $vflag
then
    echo "-v must be included to specify host where the database is" >&2
    exit 1
fi

if ! $pflag
then
    echo "-p must be included to specify database port" >&2
    exit 1
fi

if ! $dflag
then
    echo "-d must be included to specify database name to be exported" >&2
    exit 1
fi

pg_dump --host=${HOSTNAME} --port=${PORT} --format=custom --blobs --verbose --file=${BACKUP_FILE} ${DATABASE_NAME}

