#!/bin/bash

usage()
{
    echo "Install Apache Ignite 2.3.0"
    echo ""
    echo "$0"
    echo -e "\t-hdfsmaster IP or name and port of the HDFS master node, example: -hdfsmaster=localhost:9000"
    echo ""
}

while [ "$1" != "" ]; do
    PARAM=`echo $1 | awk -F= '{print $1}'`
    VALUE=`echo $1 | awk -F= '{print $2}'`
    case $PARAM in
        -h | --help)
            usage
            exit
            ;;
        -hdfsmaster)
            HDFS_MASTER=$VALUE
            ;;
        *)
            echo "ERROR: unknown parameter \"$PARAM\""
            usage
            exit 1
            ;;
    esac
    shift
done

if [ -z ${HDFS_MASTER} ]; then
	usage
	exit -1;
fi

if [ ! -d "$HADOOP_HOME" ]; then
	echo "HADOOP_HOME environment variable not set. Installation will not pursue."
	exit -1;
fi

if [ -d "apache-ignite-hadoop-2.3.0-bin" ]; then
	echo "Found folder apache-ignite-hadoop-2.3.0-bin/. Ignite seems to have already been installed. Installation will not pursue."
	exit -1;
fi

if [ ! -f "apache-ignite-hadoop-2.3.0-bin.zip" ]; then
	echo "Downloading apache ignite version 2.3.0 sources"
	curl -O http://apache.mediamirrors.org//ignite/2.3.0/apache-ignite-hadoop-2.3.0-bin.zip
	echo "Download finished"
else
	echo "Sources of ignite version 2.3.0 has already been downloaded"
fi

echo "Extracting archive"
unzip -q apache-ignite-hadoop-2.3.0-bin.zip
echo "Extraction done"
rm apache-ignite-hadoop-2.3.0-bin.zip
echo "Archive deleted"

ln -sfn apache-ignite-hadoop-2.3.0-bin apache-ignite
echo "Symbolic link apache-ignite created/updated to version 2.3.0"

# Seems like ignite is not looking for hadoop jars at the right place
ln -sfn $HADOOP_HOME/share/hadoop/mapreduce $HADOOP_HOME/share/hadoop/mapreduce/lib
echo "Symbolic link from $HADOOP_HOME/share/hadoop/mapreduce/lib to $HADOOP_HOME/share/hadoop/mapreduce created/updated"

ln -sfn "$(pwd)/apache-ignite/libs/ignite-core-2.3.0.jar" $HADOOP_HOME/share/hadoop/common/lib/ignite-core-2.3.0.jar
ln -sfn "$(pwd)/apache-ignite/libs/ignite-shmem-1.0.0.jar" $HADOOP_HOME/share/hadoop/common/lib/ignite-shmem-1.0.0.jar
ln -sfn "$(pwd)/apache-ignite/libs/ignite-hadoop/ignite-hadoop-2.3.0.jar" $HADOOP_HOME/share/hadoop/common/lib/ignite-hadoop-2.3.0.jar

perl -0777 -i -pe 's/\n.*?<!--(\n.*?<property name="secondaryFileSystem">\n(?:.*\n)+?).*-->\n/$1/g' apache-ignite/config/default-config.xml
perl -i -pe "s/your_hdfs_host:9000/${HDFS_MASTER}\//g" apache-ignite/config/default-config.xml
echo "apache-ignite/config/default-config.xml updated"

echo "Installation successful"
exit 0;
