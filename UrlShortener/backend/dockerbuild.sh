#!/bin/bash

systemctl start postgres.service
flyway migrate -url=jdbc:postgresql//localhost:5432/url_shortener -user=${URL_SHORTENER_DB_USER} -password=${URL_SHORTENER_DB_PASSWORD} -locations="classpath:db/migration/"
mvn generate-sources
mvn package
docker build . -t adarah/url_shortener:0.0.3
