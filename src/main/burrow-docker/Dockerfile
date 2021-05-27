# stage 2: runner
FROM alpine:3.13

LABEL maintainer="LinkedIn Burrow https://github.com/linkedin/Burrow"

COPY burrow /app/
COPY config /etc/burrow/
RUN chmod  -R  755 /app
CMD ["/app/burrow", "--config-dir", "/etc/burrow"]