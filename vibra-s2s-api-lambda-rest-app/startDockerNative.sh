MF_ECR_BUILT_IMAGE="$(< ecr_image.txt)"

 docker run --name bsg-api-lambda-rest-native \
 -p 3333:8080 \
 -e MF_SESSION_REDIS_HOSTS=host.docker.internal:6379 \
 -e QUARKUS_DATASOURCE_JDBC_URL=jdbc:postgresql://host.docker.internal:5432/quarkus_main \
"$MF_ECR_BUILT_IMAGE"
# bsg-api-native:latest
