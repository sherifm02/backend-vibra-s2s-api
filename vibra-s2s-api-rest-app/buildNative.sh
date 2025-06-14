mvn -X -e install -Dnative -DskipTests \
  -Dquarkus.native.container-runtime=docker \
  -Dquarkus.native.container-build=true \
  -Dquarkus.native.builder-image=quay.io/quarkus/ubi-quarkus-mandrel-builder-image:jdk-21
# -Dquarkus.native.remote-container-build=true \
# -Dquarkus.native.debug.enabled=true \
# -Dquarkus.native.native-image-xmx=6g
# -Dquarkus.native.builder-image=graalvm \\
# -Dquarkus.native.container-runtime=docker \

# -Dquarkus.native.builder-image=quay.io/quarkus/ubi-quarkus-mandrel-builder-image:22.3.0.1-Final-java17-amd64
# -Dquarkus.native.container-runtime-options="--platform=linux/amd64" \

# -Dquarkus.native.builder-image=quay.io/quarkus/ubi-quarkus-mandrel-builder-image:22.3-java17 - takes aarch64 on mac m2

# -Dquarkus.native.builder-image=quay.io/quarkus/ubi-quarkus-native-image:22.2-java17
# -Dquarkus.native.builder-image=quay.io/quarkus/ubi-quarkus-mandrel:22.3-java17
#https://github.com/quarkusio/quarkus/issues/29124
#https://quay.io/repository/quarkus/ubi-quarkus-mandrel-builder-image?tab=tags
