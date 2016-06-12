FROM 172.16.200.22:5000/jdk7
RUN mkdir -p /app
WORKDIR /app

ADD terra-server/target/terra-server-1.0.0.jar app.jar
COPY docker-entrypoint.sh /app/
EXPOSE 8889
ENTRYPOINT [docker-entrypoint.sh]
CMD []