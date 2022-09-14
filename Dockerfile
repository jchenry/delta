FROM maven:3.6.0-jdk-11-slim AS build
COPY src /home/app/src
COPY spec /home/app/spec
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

FROM openapitools/openapi-generator-cli
COPY --from=build /home/app/target/go-server-lambda-openapi-generator-0.0.0-SNAPSHOT.jar \
     /opt/openapi-generator/modules/go-server-lambda-openapi-generator/target/go-server-lambda-openapi-generator-0.0.0-SNAPSHOT.jar
COPY spec/openapi.yaml /opt/openapi.yaml
COPY scripts/docker-entrypoint.sh /usr/local/bin/docker-entrypoint.sh
RUN ln -s /usr/local/bin/docker-entrypoint.sh /usr/local/bin/openapi-generator
ENTRYPOINT ["/usr/local/bin/docker-entrypoint.sh"]
