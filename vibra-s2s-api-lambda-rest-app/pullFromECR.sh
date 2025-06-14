#  AWS_PROFILE=mf-alex-admin bash pullFromECR.sh

source ./_include.sh

MF_ECR_BUILT_IMAGE="$(< ecr_image.txt)"

_ensure_defined "${MF_ECR_BUILT_IMAGE}" "MF_ECR_BUILT_IMAGE"

echo "about to pull: ${MF_ECR_BUILT_IMAGE}" && \

aws ecr get-login-password --region "${MF_ECR_REGION}" | docker login --username AWS --password-stdin "${MF_ECR_REGISTRY}" && \

docker pull "${MF_ECR_BUILT_IMAGE}"

#docker inspect adcf28451c8f | jq -r '.[] | [.Architecture]| join("")'