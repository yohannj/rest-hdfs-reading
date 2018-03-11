FROM yohannj/hadoop-base:v1

RUN \
        ssh-keygen -t rsa -P '' -f ~/.ssh/id_rsa && \
        cat ~/.ssh/id_rsa.pub >> ~/.ssh/authorized_keys && \
        chmod 0600 ~/.ssh/authorized_keys

ADD ssh_config /root/.ssh/config
ADD run.sh /run.sh
RUN chmod a+x /run.sh

ADD default-config.xml $IGNITE_HOME/config/default-config.xml

RUN mkdir $IGNITE_HOME/hdfs-config
ADD core-site.xml $IGNITE_HOME/hdfs-config/core-site.xml
ADD log4j.properties $IGNITE_HOME/hdfs-config/log4j.properties
ADD mapred-site.xml $IGNITE_HOME/hdfs-config/mapred-site.xml

EXPOSE 10500 11211 31100-31200 47100-47200 47500-47600 49128

CMD bash /run.sh
