FROM yohannj/spark-base:v1

ENV SPARK_MASTER_PORT 7077
ENV SPARK_MASTER_WEBUI_PORT 8080

RUN \
        ssh-keygen -t rsa -P '' -f ~/.ssh/id_rsa && \
        cat ~/.ssh/id_rsa.pub >> ~/.ssh/authorized_keys && \
        chmod 0600 ~/.ssh/authorized_keys

ADD ssh_config /root/.ssh/config
ADD run.sh /run.sh
RUN chmod a+x /run.sh

EXPOSE 8080 7077 6066

CMD bash /run.sh
