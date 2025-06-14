#supports x86_64
# FROM  public.ecr.aws/lambda/provided

#supports both x86_64 and arm64
FROM  public.ecr.aws/lambda/provided:al2
ADD target/vibra-s2s-api-*.*.*-runner /var/runtime/bootstrap
RUN chmod ugo+x /var/runtime/bootstrap

CMD ["io.quarkus.amazon.lambda.runtime.QuarkusStreamHandler::handleRequest"]