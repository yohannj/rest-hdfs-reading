FROM ubuntu:16.04

ENV IGNITE_HOME /opt/ignite
ENV IGNITE_VERSION 2.3.0
ENV JAVA_HOME /usr/lib/jvm/java-8-oracle


RUN apt-get update && apt-get install -y --no-install-recommends \
		ssh \
		unzip \
		curl \
		libsnappy-dev \
		software-properties-common

##################################################################

# Java 8

RUN \
	echo oracle-java8-installer shared/accepted-oracle-license-v1-1 select true | debconf-set-selections && \
	add-apt-repository -y ppa:webupd8team/java && \
	apt-get update && \
	apt-get install -y oracle-java8-installer && \
	rm -rf /var/lib/apt/lists/* && \
	rm -rf /var/cache/oracle-jdk8-installer


# Ignite

RUN \
	curl -O https://dist.apache.org/repos/dist/release/ignite/${IGNITE_VERSION}/apache-ignite-hadoop-${IGNITE_VERSION}-bin.zip && \
	unzip -q apache-ignite-hadoop-${IGNITE_VERSION}-bin.zip && \
	rm apache-ignite-hadoop-${IGNITE_VERSION}-bin.zip && \
	mv apache-ignite-hadoop-${IGNITE_VERSION}-bin ${IGNITE_HOME}
