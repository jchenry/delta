FROM openapitools/openapi-generator-cli
RUN mkdir -p /opt/openapi-generator/modules/go-server-lambda-openapi-generator/target/
COPY target/go-server-lambda-openapi-generator-0.0.0-SNAPSHOT.jar \
     /opt/openapi-generator/modules/go-server-lambda-openapi-generator/target/go-server-lambda-openapi-generator-0.0.0-SNAPSHOT.jar
COPY spec/openapi.yaml /opt/openapi.yaml
COPY scripts/docker-entrypoint.sh /usr/local/bin/
RUN ln -s /usr/local/bin/docker-entrypoint.sh /usr/local/bin/openapi-generator
ENTRYPOINT ["/usr/local/bin/docker-entrypoint.sh"]
