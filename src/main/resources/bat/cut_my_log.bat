#!/bin/bash
LOG_PATH="/var/log/nginx/"
RECORD_TIME=$(date -d "yesterday" +%Y-%m-%d+%H:%M)
PID=/var/run/nginx/nginx.pid

# Move the current access.log file to a new file with yesterday's timestamp
mv ${LOG_PATH}/access.log ${LOG_PATH}/access.${RECORD_TIME}.log

# Move the current error.log file to a new file with yesterday's timestamp
mv ${LOG_PATH}/error.log ${LOG_PATH}/error.${RECORD_TIME}.log

# Send a signal to the Nginx main process to reopen log files
kill -USR1 `cat $PID`