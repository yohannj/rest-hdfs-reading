FROM yohannj/hadoop-base:v1

ENV SPARK_VERSION 2.3.0
ENV SPARK_HOME /opt/spark
ENV SPARK_CONF_DIR=$SPARK_HOME/conf

ADD spark-${SPARK_VERSION}-bin-hadoop2.7.tgz /

RUN \
	#wget https://dist.apache.org/repos/dist/release/spark/spark-${SPARK_VERSION}/spark-${SPARK_VERSION}-bin-hadoop2.7.tgz && \
	#tar -xzf spark-${SPARK_VERSION}-bin-hadoop2.7.tgz && \
	rm -f spark-${SPARK_VERSION}-bin-hadoop2.7.tgz && \
	mv spark-${SPARK_VERSION}-bin-hadoop2.7 $SPARK_HOME && \
	echo 'PATH=$PATH:$SPARK_HOME/bin' >> ~/.bashrc

RUN echo "export SPARK_DIST_CLASSPATH=/opt/hadoop/etc/hadoop:/opt/hadoop/share/hadoop/common/lib/*:/opt/hadoop/share/hadoop/common/*:/opt/hadoop/share/hadoop/hdfs:/opt/hadoop/share/hadoop/hdfs/lib/*:/opt/hadoop/share/hadoop/hdfs/*:/opt/hadoop/share/hadoop/mapreduce/lib/*:/opt/hadoop/share/hadoop/mapreduce/*:/opt/hadoop/share/hadoop/yarn:/opt/hadoop/share/hadoop/yarn/lib/*:/opt/hadoop/share/hadoop/yarn/*" >> /opt/spark/conf/spark-env.sh

ADD spark-defaults.conf $SPARK_CONF_DIR/spark-defaults.conf
ADD log4j.properties $SPARK_CONF_DIR/log4j.properties
