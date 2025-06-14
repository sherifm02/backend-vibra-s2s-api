MF_ECR_ACCOUNT="951632447595"
#MF_ECR_REGION="us-east-1"
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