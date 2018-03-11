FROM yohannj/spark-base:v1

RUN \
        ssh-keygen -t rsa -P '' -f ~/.ssh/id_rsa && \
        cat ~/.ssh/id_rsa.pub >> ~/.ssh/authorized_keys && \
        chmod 0600 ~/.ssh/authorized_keys

ADD ssh_config /root/.ssh/config
ADD run.sh /run.sh
RUN chmod a+x /run.sh

EXPOSE 1337 1338 4040-4050 8081

CMD bash /run.sh
