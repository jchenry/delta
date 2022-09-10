.PHONY: all build clean

build:
        mvn package 
clean:   
        rm linklist 

docker: build
        docker build -t r.j5y.xyz/delta:latest . 
        # docker build -t diskstation:5000/linklist .

docker-run:
	docker run --rm \
	-v ${PWD}:/local delta generate \
	-i /local/spec/openapi.yaml \
	-g go-server-lambda \
	-o /local/out/generated




