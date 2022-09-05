# generate_custom:
# 	docker run --rm \
# 	-v ${PWD}:/local openapitools/openapi-generator-cli generate \
# 	-i /local/spec/openapi.yaml \
# 	-g go-server \
# 	-t /local/custom_template \
# 	-o /local/out/custom_generated \
#     -c /local/custom_template/config.yaml
generate:
	docker run --rm \
	-v ${PWD}:/local delta generate \
	-i /local/spec/openapi.yaml \
	-g go-server-lambda \
	-o /local/out/generated
# template:
# 	docker run --rm \
# 	-v ${PWD}:/local openapitools/openapi-generator-cli author template \
# 	-g go-server \
# 	-o /local/out/template
# meta:
# 	docker run --rm \
# 	-v ${PWD}:/local openapitools/openapi-generator-cli meta \
# 	-o /local/out/src -l go -n go-server-lambda -p me.jchenry.delta.openapi.generator

# build: generate

compile:
	mvn package 
build: compile
	docker build -t delta . 
clean: 
	rm -rf out/ target/


