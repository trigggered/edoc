#!/bin/sh
     
###########################################################################################
# Export Oracle paths and take a variable and pass the username and password
# of Oracle database
###########################################################################################
 
export ORACLE_BASE=/u01/app/oracle

export ORACLE_HOME=$ORACLE_BASE/product/11.2.0
export LD_LIBRARY_PATH=$ORACLE_HOME/lib
export ORACLE_PATH=$ORACLE_HOME/bin
export PATH=$ORACLE_HOME/bin:$PATH

ORACLE_SID=edoct
ORA_USR=P00090730
ORA_PWD=P00090730
ORACLE_ACCESS=$ORA_USR@$ORACLE_SID/$ORA_PWD

LOADER_HOME=/home/oracle/hr_loader
CTL_FOLDER=$LOADER_HOME/ctl
IN_FOLDER=$LOADER_HOME/in
LOG_OUT=$LOADER_HOME/log
ARCH_DIR=$LOADER_HOME/arch/ 

###########################################################################################
# Get all the files which are not like '%_loaded'
# For example : if the file name is test223221_loaded.txt then this will not be taken
# as this file is already loaded.
###########################################################################################
#FILES=`ls /home/oracle/in/*.CSV`;
FILES=`ls $IN_FOLDER/*.csv`; 

###########################################################################################
# Get all the files which are not like '%_loaded' and start the Oracle SQL Loader to
# to load the data
#
###########################################################################################
 
for file in ${FILES[@]}
do

filename=$(basename "$file" .csv)
in_file_name=$(basename "$file")
#echo ${filename:7}
ctlfname=${filename:7}".ctl"
logfile=${filename:7}"_loaded.log"
###########################################################################################
# SQL LOADER command to load the file from
###########################################################################################
#echo "Loaded data "$(basename "$file")
echo "Loaded data from file:"$in_file_name
sqlldr $ORACLE_ACCESS control=$CTL_FOLDER/$ctlfname log=$LOG_OUT/$logfile data=$file
echo "Move file "$in_file_name "into " $ARCH_DIR
mv $file $ARCH_DIR
done
