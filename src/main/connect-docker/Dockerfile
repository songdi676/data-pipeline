FROM 10.1.8.15:5000/paas_platform/debezium/connect:1.4.2.Final
ADD ./confluentinc-kafka-connect-jdbc-10.0.2 ${MAVEN_DEP_DESTINATION}/kafka-connect-jdbc
COPY jmx_prometheus_javaagent-0.15.0.jar /kafka/jmx_prometheus_javaagent.jar
COPY config.yml /kafka/config.yml
