FROM prom/prometheus:v2.25.1

RUN sed -i -e "s/'localhost:9090'/'localhost:9090','connect:8080'/" /etc/prometheus/prometheus.yml
