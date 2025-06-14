
SAM_CLI_TELEMETRY=0 sam local start-api \
  --template sam.jvm-local.yaml \
  --log-file local-log.txt \
  --debug
  #  --template sam.native.yaml \
#  --template sam.native-orig.yaml \
#  curl -ivv "localhost:3000/cwguestlogin.do?gameId=game1&bankId=bank1"
#  curl -ivv "localhost:3000/cwstartgamev2.do?gameId=game1&bankId=megafair&mode=real&token=user_123456"
