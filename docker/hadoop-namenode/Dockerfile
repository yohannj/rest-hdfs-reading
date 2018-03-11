FROM yohannj/hadoop-base:v1

VOLUME /hadoop/dfs/name

RUN \
        ssh-keygen -t rsa -P '' -f ~/.ssh/id_rsa && \
        cat ~/.ssh/id_rsa.pub >> ~/.ssh/authorized_keys && \
        chmod 0600 ~/.ssh/authorized_keys

ADD ssh_config /root/.ssh/config
ADD run.sh /run.sh
RUN chmod a+x /run.sh

EXPOSE 9000 9820 9870-9871

CMD bash /run.sh
