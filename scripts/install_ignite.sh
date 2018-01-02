#!/bin/bash

if [ ! 	-d "$HADOOP_HOME" ]; then
	echo "Hadoop must first be installed. Installation will not pursue."
	exit -1;
fi

if [ -d "apache-ignite-2.3.0-src" ]; then
	echo "Found folder apache-ignite-2.3.0/. Ignite seems to have already been installed. Installation will not pursue."
	exit -1;
fi

if [ ! -f "apache-ignite-2.3.0-src.zip" ]; then
	echo "Downloading apache ignite version 2.3.0 sources"
	curl -O http://apache.mediamirrors.org//ignite/2.3.0/apache-ignite-2.3.0-src.zip
	echo "Download finished"
else
	echo "Sources of ignite version 2.3.0 has already been downloaded"
fi

echo "Extracting archive"
unzip -q apache-ignite-2.3.0-src.zip
echo "Extraction done"
rm apache-ignite-2.3.0-src.zip
echo "Archive deleted"

# Following lines were found on https://ignite.apache.org/download.cgi#sources, part Building the Binaries
cd apache-ignite-2.3.0-src
mvn clean package -DskipTests -Dignite.edition=hadoop -Dhadoop.version=3.0.0
cd ..

ln -sfn apache-ignite-2.3.0-src apache-ignite
echo "Symbolic link apache-ignite created/updated to version 2.3.0"

# Seems like ignite is not looking for hadoop jars at the right place
ln -sfn $HADOOP_HOME/share/hadoop/mapreduce $HADOOP_HOME/share/hadoop/mapreduce/lib
echo "Symbolic link from /home/hadoop/hadoop/share/hadoop/mapreduce/lib to /home/hadoop/hadoop/share/hadoop/mapreduce created/updated"

echo "Installation successful"
exit 0;