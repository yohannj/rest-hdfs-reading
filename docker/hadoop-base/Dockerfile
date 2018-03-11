FROM yohannj/ignite-jdk8-base:v1

ENV HADOOP_HOME /opt/hadoop
ENV HADOOP_CONF_DIR=$HADOOP_HOME/etc/hadoop
ENV HADOOP_VERSION 3.0.0
ENV IGNITE_HOME /opt/ignite
ENV IGNITE_SHMEM_VERSION 1.0.0
ENV IGNITE_VERSION 2.3.0
ENV JAVA_HOME /usr/lib/jvm/java-8-oracle

##################################################################

# Hadoop 3

ADD hadoop-3.0.0.tar.gz /

RUN \
	#wget https://dist.apache.org/repos/dist/release/hadoop/common/hadoop-${HADOOP_VERSION}/hadoop-${HADOOP_VERSION}.tar.gz && \
	#tar -xzf hadoop-${HADOOP_VERSION}.tar.gz && \
	rm -f hadoop-${HADOOP_VERSION}.tar.gz && \
	mv hadoop-${HADOOP_VERSION} $HADOOP_HOME && \
	echo "export JAVA_HOME=$JAVA_HOME" >> $HADOOP_HOME/etc/hadoop/hadoop-env.sh && \
	echo "export HADOOP_HOME=$HADOOP_HOME" >> $HADOOP_HOME/etc/hadoop/hadoop-env.sh && \
	echo "export HDFS_DATANODE_USER=root" >> $HADOOP_HOME/etc/hadoop/hadoop-env.sh && \
	echo "export HDFS_NAMENODE_USER=root" >> $HADOOP_HOME/etc/hadoop/hadoop-env.sh && \
	echo "export HDFS_SECONDARYNAMENODE_USER=root" >> $HADOOP_HOME/etc/hadoop/hadoop-env.sh && \
	echo "export YARN_RESOURCEMANAGER_USER=root" >> $HADOOP_HOME/etc/hadoop/yarn-env.sh && \
	echo "export YARN_NODEMANAGER_USER=root" >> $HADOOP_HOME/etc/hadoop/yarn-env.sh && \
	echo 'PATH=$PATH:$HADOOP_HOME/bin' >> ~/.bashrc

RUN mkdir $HADOOP_HOME/logs
RUN mkdir -p /hadoop/dfs/name
RUN mkdir /hadoop/dfs/data

ADD core-site.xml $HADOOP_CONF_DIR/core-site.xml
ADD hdfs-site.xml $HADOOP_CONF_DIR/hdfs-site.xml
ADD mapred-site.xml $HADOOP_CONF_DIR/mapred-site.xml
ADD yarn-site.xml $HADOOP_CONF_DIR/yarn-site.xml

# Ignite configuration

RUN \
	ln -s $HADOOP_HOME/share/hadoop/mapreduce $HADOOP_HOME/share/hadoop/mapreduce/lib && \
	ln -s $IGNITE_HOME/libs/ignite-core-${IGNITE_VERSION}.jar $HADOOP_HOME/share/hadoop/common/lib/ignite-core-${IGNITE_VERSION}.jar && \
	ln -s $IGNITE_HOME/libs/ignite-shmem-${IGNITE_SHMEM_VERSION}.jar $HADOOP_HOME/share/hadoop/common/lib/ignite-shmem-${IGNITE_SHMEM_VERSION}.jar && \
	ln -s $IGNITE_HOME/libs/ignite-hadoop/ignite-hadoop-${IGNITE_VERSION}.jar $HADOOP_HOME/share/hadoop/common/lib/ignite-hadoop-${IGNITE_VERSION}.jar
