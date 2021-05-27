mkdir -p $LOG_PATH/$HOSTNAME
cd /usr/local/nldata/app
java $agent -cp app:app/lib/* -Duser.timezone=GMT+08 \
    $JVM_OPTS \
    -XX:OnOutOfMemoryError="kill -9 %p" \
    -XX:+HeapDumpOnOutOfMemoryError \
    -XX:HeapDumpPath=$LOG_PATH/$HOSTNAME/heap-dump.hprof \
    -XX:OnError="jstack -F %p >$LOG_PATH/$HOSTNAME/ErrorDump.log" \
    nl.paas.tool.data.pipeline.DataPipelineApplication
