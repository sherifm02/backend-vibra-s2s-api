suffix="native"
file="./target/classes/mf-version.properties"

source ./_include.sh

docker_name="${artifactId}-${suffix}"
docker_image="${MF_LOCAL_IMAGE}:${MF_TAG}"

echo "docker_name: ${docker_name}"
echo "docker_image: ${docker_image}"

docker stop $docker_name
docker rm $docker_name

#--platform linux/amd64
 docker run --name "${docker_name}" $MF_LINUX_DOCKER_PATCH \
 -p 9091:8080 \
 -p 9092:8081 \
 -e QUARKUS_HTTP_PORT=8080 \
 -e MF_SERVICE_NAME=vibra-s2s-api \
 -e MF_MASTER_PASSWORD_PASS1='' \
 -e MF_CONFIG_ENV=dev \
 -e MF_ENVIRONMENT_NAME=local-native-"$USER" \
 -e MF_OTEL_COLLECTOR_HOST=host.docker.internal \
 -e MF_SESSION_REDIS_HOSTS=host.docker.internal:6379 \
 -e MF_SESSION_REDIS_USERNAME=default \
 -e MF_SESSION_REDIS_PASSWORD=redis1redis1 \
 -e MF_SESSION_REDIS_PROTOCOL=redis:// \
 -e QUARKUS_REDIS_REDIS_SESSION_TLS_ENABLED=false \
 -e QUARKUS_DATASOURCE_JDBC_URL=jdbc:postgresql://host.docker.internal:5432/quarkus_main_3_1_0 \
 -e QUARKUS_DATASOURCE_USERNAME=master \
 -e QUARKUS_DATASOURCE_PASSWORD=postgres-local \
 -e QUARKUS_REST_CLIENT_BSG_BETRESULT_API_MFMOCK_URL=https://prod.bsg-mock.mfgapi.com \
 -e MF_REGISTRATION_TRANSACTION_MODE=http \
 -e MF_REGISTRATION_TRANSACTION_API=http://host.docker.internal:5568 \
  "${docker_image}"
# -e QUARKUS_HIBERNATE_ORM_DATABASE_GENERATION=none \
# -e QUARKUS_DATASOURCE_JDBC=false \


#docker run -it --platform linux/amd64 --entrypoint bash mf-api-rest-app-native:1.0.20230413_0