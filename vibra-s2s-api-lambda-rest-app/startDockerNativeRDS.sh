secret_json=$(AWS_PROFILE=mf-alex-admin AWS_REGION=ap-southeast-2  aws secretsmanager get-secret-value --secret-id prod/aurora-pg/postgres-rds-prod)
#echo $secret_json
#secret_json='{ "ARN": "arn:aws:secretsmanager:ap-southeast-2:951632447595:secret:prod/aurora-pg/postgres-rds-prod-d86xsx", "Name": "prod/aurora-pg/postgres-rds-prod", "VersionId": "7c09e891-2720-4cc4-992a-51aedb63ba56", "SecretString": "{\"dbClusterIdentifier\":\"postgres-rds-prod-auroradbcluster-zmcgrzvradhp\",\"username\":\"master\"}", "VersionStages": [ "AWSCURRENT" ], "CreatedDate": "2022-12-28T16:21:42.389000+02:00" }'
secret_json=$(jq -r '.SecretString' <<< ${secret_json} )
echo $secret_json

DATASOURCE_USERNAME=$( jq -r '.["username"]' <<< ${secret_json} )
echo "DATASOURCE_USERNAME: ${DATASOURCE_USERNAME}"

DATASOURCE_HOST=$( jq -r '.["host"]' <<< ${secret_json} )
echo "DATASOURCE_HOST: ${DATASOURCE_HOST}"

DATASOURCE_PORT=$( jq -r '.["port"]' <<< ${secret_json} )
echo "DATASOURCE_PORT: ${DATASOURCE_PORT}"

DATASOURCE_PASSWORD=$( jq -r '.["password"]' <<< ${secret_json} )
#echo "DATASOURCE_PASSWORD: ${DATASOURCE_PASSWORD}"

DATASOURCE_DBNAME=$( jq -r '.["dbname"]' <<< ${secret_json} )
echo "DATASOURCE_DBNAME: ${DATASOURCE_DBNAME}"

#exit -1;

 docker run --name bsg-api-native-rds \
 -p 3333:8080 \
 -e MF_SESSION_REDIS_HOSTS=host.docker.internal:6379 \
 -e QUARKUS_DATASOURCE_JDBC_URL="jdbc:postgresql://host.docker.internal:5433/${DATASOURCE_DBNAME}?sslmode=require" \
 -e QUARKUS_DATASOURCE_USERNAME="${DATASOURCE_USERNAME}" \
 -e QUARKUS_DATASOURCE_PASSWORD="${DATASOURCE_PASSWORD}" \
 bsg-api-native:0.0.1
