mvn -X -e install -Dnative -DskipTests -Dquarkus.native.container-build=true \
 -Dquarkus.native.container-runtime=docker \
 -Dquarkus.native.builder-image=quay.io/quarkus/ubi-quarkus-mandrel-builder-image:jdk-21
# -Dquarkus.native.builder-image=quay.io/quarkus/ubi-quarkus-native-image:22.2-java17
# -Dquarkus.native.builder-image=quay.io/quarkus/ubi-quarkus-mandrel:22.3-java17
 #https://github.com/quarkusio/quarkus/issues/29124
