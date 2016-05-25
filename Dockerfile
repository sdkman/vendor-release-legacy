FROM java:8

MAINTAINER Marco Vermeulen

RUN mkdir /release

ADD build/libs /release

ENTRYPOINT java -Djava.security.egd=file:/dev/./urandom -jar /release/application.jar

