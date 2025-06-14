source ../_include.sh
file="../target/classes/mf-version.properties"
version="$(_prop 'version' "${file}")"
_ensure_defined "${version}" "version"

_port="7070"
_base_name="vibra-s2s-api/vibra-s2s-api-lambda-rest-app-native"
_image_with_tag="${_base_name}:${version}"

echo "Using _image_with_tag: ${_image_with_tag} 

*******************************************
"


json_file_name="local-parameters.json"
parameters=( )
parameters+=("LocalDockerImageTagParam"="${_image_with_tag}")
parameters+=($(jq -r '.[] | [.ParameterKey, .ParameterValue] |  "\(.[0])=\(.[1])"' ${json_file_name} ))

echo "${parameters[@]}"

SAM_CLI_TELEMETRY=0 sam local start-api \
  --invoke-image "HelloWorldFunction=${_image_with_tag}"\
  --port="${_port}" \
  --debug \
  --warm-containers eager \
  --template sam-local-run.yaml \
  --parameter-overrides "${parameters[@]}"


#  curl -ivv -H "X-Forwarded-For: 23.123.45.33" -XGET "localhost:8080/cwstartgamev2.do?gameId=game1&bankId=megafair&mode=real&token=user_123456"