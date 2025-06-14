
#AWS_PROFILE=mf-alex-admin AWS_REGION=us-east-1 SAM_CLI_TELEMETRY=0 bash deploy-sam.sh 
#AWS_PROFILE=mf-alex-admin AWS_REGION=ap-southeast-2 SAM_CLI_TELEMETRY=0 bash deploy-sam.sh prod
source ../_include.sh
file="../target/classes/mf-version.properties"
version="$(_prop 'version' "${file}")"
_ensure_defined "${version}" "version"


stage=${1}
if [ -z "$stage" ]; then 
    echo "stage is blank"
    exit -1
fi

json_file_name="api-vibra-parameters.json"

# https://github.com/aws/aws-sam-cli/blob/develop/designs/sam-config.md


parameters=("StageParam"="${stage}" "DockerImageTagParam"="${version}")
parameters+=($(jq -r '.[] | [.ParameterKey, .ParameterValue] |  "\(.[0])=\(.[1])"' ${json_file_name} ))

echo "${parameters[@]}"

# sam build --use-container --build-image public.ecr.aws/sam/build-provided.al2 \
# -t sam.native-deploy.yaml \
# --parameter-overrides "${parameters[@]}" \
# --debug 

# exit;


sam deploy -t sam.native-deploy.yaml -g \
--parameter-overrides "${parameters[@]}" \
--stack-name "api-vibra-sam-${stage}" \
--region ${AWS_REGION} \
--debug 
#  \
# --profile mf-alex-admin