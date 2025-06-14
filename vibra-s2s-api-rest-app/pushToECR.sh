#  AWS_PROFILE=mf-alex-admin bash pushToECR.sh

file="./target/classes/mf-version.properties"

source ./_include.sh

MF_ECR_BUILT_IMAGE="$(< ecr_image.txt)"
#MF_ECR_BUILT_IMAGE="$(head -n1 ecr_image.txt)"

_ensure_defined "${MF_ECR_BUILT_IMAGE}" "MF_ECR_BUILT_IMAGE"

echo "about to push: ${MF_ECR_BUILT_IMAGE}" && \

aws ecr get-login-password --region "${MF_ECR_REGION}" | docker login --username AWS --password-stdin "${MF_ECR_REGISTRY}" && \

docker push "${MF_ECR_BUILT_IMAGE}"