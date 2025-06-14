#!/bin/sh

file="./target/classes/mf-version.properties"

source ./_include.sh

function get-bsg-api-image() {
  get-image "vibra-s2s-api" "${_tag_name_from_pom}" "vibra-s2s-api/vibra-s2s-api-rest-app-native"
}

function get-image() {
  _repo_name="${1}"
  _tag_name="${2}"
  _local_name="${3}"

  _full_image_src_url="$AWS_SOURCE_ACCOUNT".dkr.ecr.us-east-1.amazonaws.com/"${_repo_name}":"${_tag_name}"
 echo "About to pull: ${_full_image_src_url}"
 docker pull "${_full_image_src_url}"

 _local_image_with_tag="${_local_name}":"${_tag_name}"
 echo "Tagging as: ${_local_image_with_tag}"
 docker tag "${_full_image_src_url}" "${_local_image_with_tag}"

 MF_ECR_REPO="${_local_name}"
 MF_ECR_IMAGE_WITH_TAG="${MF_ECR_REGISTRY}"/"${MF_ECR_REPO}":"${_tag_name}"

 echo "Tagging as: ${MF_ECR_IMAGE_WITH_TAG}"
 docker tag "${_full_image_src_url}" "${MF_ECR_IMAGE_WITH_TAG}"

 echo "${MF_ECR_IMAGE_WITH_TAG}" > ./ecr_image.txt
}

_pom_file="../pom.xml"
_tag_name_from_pom=$(xmllint --xpath "/*[local-name()='project']/*[local-name()='version']/text()" "${_pom_file}")
#_tag_name="3.2.5.Final-20240526-13-24_23-23"

export AWS_SOURCE_ACCOUNT=339712747708


# AWS_PROFILE=mf-stg-org-mf-source-2 AWS_REGION=us-east-1 aws ecr list-images --repository-name=foundation
# AWS_PROFILE=mf-stg-org-mf-source-2 AWS_REGION=us-east-1 aws ecr describe-repositories
# AWS_PROFILE=mf-stg-org-mf-source-2 AWS_REGION=us-east-1 aws ecr batch-get-image --repository-name="${_repo_name}" --image-ids="imageTag=${_tag_name}"

 AWS_PROFILE=mf-stg-org-mf-source-2 aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin "$AWS_SOURCE_ACCOUNT".dkr.ecr.us-east-1.amazonaws.com

get-bsg-api-image




