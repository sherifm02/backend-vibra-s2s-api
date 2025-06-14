 docker run --name bsg-api \
 -p 3333:8080 \
 -e MF_SESSION_REDIS_HOSTS=host.docker.internal:6379 \
 bsg-api-jvm:0.0.1