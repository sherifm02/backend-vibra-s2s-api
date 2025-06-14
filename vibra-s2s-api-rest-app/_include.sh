MF_ECR_ACCOUNT="951632447595"
MF_ECR_REGION="ap-southeast-2"
MF_ECR_REGISTRY="${MF_ECR_ACCOUNT}.dkr.ecr.${MF_ECR_REGION}.amazonaws.com"


function _prop {
  key="${1}"
  file="${2}"
  grep "${key}" ${file} | cut -d'=' -f2
}

function _ensure_defined() {
  if [ -z "$1" ]; then
      echo "$2 is blank"
      exit -1
  fi
}

_ensure_defined "${file}" "file"

artifactId="$(_prop 'artifactId' "${file}")"
#version="$(_prop 'version' "${file}")"
_pom_file="../pom.xml"
version=$(xmllint --xpath "/*[local-name()='project']/*[local-name()='version']/text()" "${_pom_file}")
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