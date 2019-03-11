FROM openjdk:8-jdk-alpine
VOLUME /tmp
RUN mkdir /work
COPY . /work
WORKDIR /work
RUN /work/gradlew build -x test
RUN mv /work/build/libs/*.jar /work/app.jar

ENV JAVA_OPTS="-XX:+UseCompressedOops -XX:+UseContainerSupport -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=70 -XX:+ScavengeBeforeFullGC -XX:+CMSScavengeBeforeRemark"

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/work/app.jar"]