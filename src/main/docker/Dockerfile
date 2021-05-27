FROM 10.1.8.15:5000/paas_platform/nl.taas.jdk:1.8.201_update
RUN mkdir -p /usr/local/nldata/app
ENV LANG en_US.UTF-8
ENV LANGUAGE en_US:en
ENV LC_ALL en_US.UTF-8
ARG WORK_DIR=/usr/local/nldata/app
ARG DEPENDENCY=dependency
ADD start.sh /usr/local/nldata
ADD Dockerfile /
COPY ${DEPENDENCY}/BOOT-INF/lib ${WORK_DIR}/app/lib
COPY ${DEPENDENCY}/META-INF ${WORK_DIR}/app/META-INF
COPY ${DEPENDENCY}/BOOT-INF/classes ${WORK_DIR}/app
WORKDIR ${WORK_DIR}
ENTRYPOINT ["sh","/usr/local/nldata/start.sh"]
EXPOSE 8080