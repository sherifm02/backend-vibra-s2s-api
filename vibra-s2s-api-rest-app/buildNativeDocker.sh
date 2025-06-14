
suffix="native"
file="./target/classes/mf-version.properties"

source ./_include.sh

echo "MF_ECR_REPO: ${MF_ECR_REPO}"
echo "MF_ECR_IMAGE_WITH_TAG: ${MF_ECR_IMAGE_WITH_TAG}"

#--platform linux/amd64
docker build   \
  -f src/main/docker/Dockerfile.native-micro \
  -t "${MF_LOCAL_IMAGE}":latest \
  -t "${MF_LOCAL_IMAGE}":"${MF_TAG}" . && \

docker tag "${MF_LOCAL_IMAGE}":"${MF_TAG}" "${MF_ECR_REGISTRY}"/"${MF_ECR_REPO}":"${MF_TAG}" && \

echo "${MF_ECR_IMAGE_WITH_TAG}" > ./ecr_image.txt
#echo "${MF_LOCAL_IMAGE}":"${MF_TAG}" >> ./ecr_image.txt

#docker build -f docker/native.Dockerfile -t bsg-api-native:0.0.1 .
