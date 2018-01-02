#!/bin/bash

echo "Retrieving latest drill version"
read -r -d '' RETRIEVE_LATEST_VERSION << END
my \$newest_version;
while(<>) {
	s/href="(drill.*?)\/"/\$newest_version = \$newest_version gt \$1 ? \$newest_version : \$1/e
}
print \$newest_version
END

LATEST_VERSION=$(curl -s http://apache.mirrors.ovh.net/ftp.apache.org/dist/drill/ | perl -E "$RETRIEVE_LATEST_VERSION")
if [[ ! $LATEST_VERSION == drill* ]]; then
	echo "Failed to determine latest version"
	exit -1;
fi

echo "Latest version is $LATEST_VERSION"
if [ -d "apache-$LATEST_VERSION/" ]; then
	echo "Found folder apache-$LATEST_VERSION/. Drill seems to have already been installed. Installation will not pursue."
	exit -1;
fi

if [ ! -f "apache-$LATEST_VERSION.tar.gz" ]; then
	echo "Downloading latest version of Apache Drill"
	curl -O http://apache.mirrors.ovh.net/ftp.apache.org/dist/drill/$LATEST_VERSION/apache-$LATEST_VERSION.tar.gz
	echo "Download finished"
else
	echo "Latest version has already been downloaded"
fi

echo "Extracting archive"
tar -xzf apache-$LATEST_VERSION.tar.gz
echo "Extraction done"
rm apache-$LATEST_VERSION.tar.gz
echo "Archive deleted"
ln -sfn apache-$LATEST_VERSION apache-drill
echo "Symbolic link apache-drill created/updated to latest version"

echo "Installation successful"
exit 0;
