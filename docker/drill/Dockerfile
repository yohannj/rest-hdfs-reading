FROM yohannj/hadoop-base:v1

ENV DRILL_VERSION 1.12.0
ENV DRILL_HOME /opt/drill

##################################################################

# Drill installation

ADD apache-drill-1.12.0.tar.gz /

RUN \
        #wget https://dist.apache.org/repos/dist/release/drill/drill-1.12.0/apache-drill-${DRILL_VERSION}.tar.gz && \
        #tar -xzf apache-drill-${DRILL_VERSION}.tar.gz && \
        rm -f apache-drill-${DRILL_VERSION}.tar.gz && \
        mv apache-drill-${DRILL_VERSION} $DRILL_HOME && \
        echo 'PATH=$PATH:$DRILL_HOME/bin' >> ~/.bashrc

ADD drill-override.conf $DRILL_HOME/conf/drill-override.conf

# Startup Drill

ADD run.sh /run.sh
RUN chmod a+x /run.sh

EXPOSE 8047 31010-31012

CMD bash /run.sh
