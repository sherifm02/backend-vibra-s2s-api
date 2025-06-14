source ./_include.sh

suffix="native"
file="./target/classes/mf-version.properties"

artifactId="$(_prop 'artifactId' "${file}")"
version="$(_prop 'version' "${file}")"
parentArtifactId="$(_prop 'parentArtifactId' "${file}")"

_ensure_defined "${artifactId}" "artifactId"
_ensure_defined "${version}" "version"
_ensure_defined "${parentArtifactId}" "parentArtifactId"

docker_name="${artifactId}-${suffix}"
docker_image="${docker_name}:${version}"

MF_TAG="${version}"
MF_LOCAL_IMAGE="${parentArtifactId}/${artifactId}-${suffix}"
MF_ECR_REPO="${MF_LOCAL_IMAGE}"
MF_ECR_IMAGE_WITH_TAG="${MF_ECR_REGISTRY}"/"${MF_ECR_REPO}":"${MF_TAG}"

echo "MF_ECR_REPO: ${MF_ECR_REPO}"
echo "MF_ECR_IMAGE_WITH_TAG: ${MF_ECR_IMAGE_WITH_TAG}"

#--platform linux/amd64
docker build   \
  -f docker/native.Dockerfile \
  -t "${MF_LOCAL_IMAGE}":latest \
  -t "${MF_LOCAL_IMAGE}":"${MF_TAG}" . && \

docker tag "${MF_LOCAL_IMAGE}":"${MF_TAG}" "${MF_ECR_REGISTRY}"/"${MF_ECR_REPO}":"${MF_TAG}" && \

echo "${MF_ECR_IMAGE_WITH_TAG}" > ./ecr_image.txt

#docker build -f docker/native.Dockerfile -t bsg-api-native:0.0.1 .
