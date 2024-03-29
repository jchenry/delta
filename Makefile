.PHONY: all build clean docker

GOFMT=$(shell which gofmt)

build: docker
 
mvn-package:
	mvn package

clean:   
	rm -rf target 
	rm -rf out

docker: 
	docker build -t r.j5y.xyz/delta:latest . 

docker-run:
	docker run --rm \
	-v ${PWD}:/local r.j5y.xyz/delta:latest generate \
	-i /local/spec/openapi.yaml \
	-g go-server-lambda \
	-o /local/out/generated
	$(GOFMT) -w ./out/generated

docker-operations:
	docker run --rm \
	-v ${PWD}:/local \
	r.j5y.xyz/delta:latest generate -g go \
	-i /local/spec/openapi.yaml \
    --global-property debugOpenAPI=true