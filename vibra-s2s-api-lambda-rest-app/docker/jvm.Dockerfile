FROM  public.ecr.aws/lambda/java:11

RUN yum install -y lsof

ADD target/vibra-s2s-api-*.*.*-runner.jar /var/task/lib/my-service.jar
ADD target/lib/  /var/task/lib/

CMD ["io.quarkus.amazon.lambda.runtime.QuarkusStreamHandler::handleRequest"]