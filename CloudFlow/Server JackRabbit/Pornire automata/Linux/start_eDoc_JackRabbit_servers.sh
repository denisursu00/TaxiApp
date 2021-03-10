EDOC_JACKRABBIT_SERVERS_LIFECYCLE_LOG_FILE=/opt/JackRabbitServers_lifecycle.log

EDOC_JACKRABBIT_SERVER_PATHS[0]=/opt/JackRabbit_eDocIntern_test/
EDOC_JACKRABBIT_SERVER_PATHS[1]=/opt/JackRabbitServerDev/
EDOC_JACKRABBIT_SERVER_PATHS[2]=/opt/JackRabbitServerTest/
EDOC_JACKRABBIT_SERVER_PATHS[3]=/opt/JackRabbitServer_eDocDemoSIGEDIA_test/

EDOC_JACKRABBIT_SERVERS_COUNT=${#EDOC_JACKRABBIT_SERVER_PATHS[@]}
EDOC_JACKRABBIT_SERVER_AVERAGE_START_TIME_SECONDS=15
EDOC_JACKRABBIT_SERVERS_TOTAL_WAIT_TIME_SECONDS=$(($EDOC_JACKRABBIT_SERVERS_COUNT * $EDOC_JACKRABBIT_SERVER_AVERAGE_START_TIME_SECONDS))

EDOC_JACKRABBIT_WAIT_FOR_ORACLE_TIME_SECONDS=90

LAST_WORKING_DIRECTORY=`pwd`

echo ------------------------------------------------------ >> $EDOC_JACKRABBIT_SERVERS_LIFECYCLE_LOG_FILE

echo "[`date`] Astept sa porneasca Oracle $EDOC_JACKRABBIT_WAIT_FOR_ORACLE_TIME_SECONDS secunde..." >> $EDOC_JACKRABBIT_SERVERS_LIFECYCLE_LOG_FILE
sleep $EDOC_JACKRABBIT_WAIT_FOR_ORACLE_TIME_SECONDS
echo "[`date`] Am terminat asteptarea dupa Oracle." >> $EDOC_JACKRABBIT_SERVERS_LIFECYCLE_LOG_FILE

for JACKRABBIT_SERVER_PATH in "${EDOC_JACKRABBIT_SERVER_PATHS[@]}"
do
	cd $JACKRABBIT_SERVER_PATH
	START_JACKRABBIT_SERVER_COMMAND=`find -name 'start_server_Unix_*.sh'`

	echo "[`date`] Pornesc server-ul JackRabbit din [$JACKRABBIT_SERVER_PATH] prin comanda [$START_JACKRABBIT_SERVER_COMMAND]." >> $EDOC_JACKRABBIT_SERVERS_LIFECYCLE_LOG_FILE
	$START_JACKRABBIT_SERVER_COMMAND >> $EDOC_JACKRABBIT_SERVERS_LIFECYCLE_LOG_FILE &
done

echo "[`date`] Astept $EDOC_JACKRABBIT_SERVERS_TOTAL_WAIT_TIME_SECONDS secunde pana pornesc serverele JackRabbit..." >> $EDOC_JACKRABBIT_SERVERS_LIFECYCLE_LOG_FILE
sleep $EDOC_JACKRABBIT_SERVERS_TOTAL_WAIT_TIME_SECONDS
echo "[`date`] Am terminat asteptarea dupa pornirea serverelor JackRabbit..." >> $EDOC_JACKRABBIT_SERVERS_LIFECYCLE_LOG_FILE

cd $LAST_WORKING_DIRECTORY