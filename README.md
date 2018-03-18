# Rest HDFS reading
**Rest HDFS reading** regroups tools that allow reading data on HDFS in a synchronous fashion.
The goal is to propose installation guide and benchmark instructions for a variety of solutions that answer different needs. (Performance oriented, cost savvy, etc.)

## Documentation
For more information on this project, check the [wiki pages](https://github.com/yohannj/rest-hdfs-reading/wiki).

## Solutions / Technology used
[Apache-Drill](https://drill.apache.org/), a query engine to read HDFS data.

[Apache-Ignite](https://ignite.apache.org/), used as an in memory file system over HDFS.

A WebService running on [Apache-Spark](https://spark.apache.org/) as a long-running job. Serves as an alternative to Apache-Drill to query HDFS data.

## Links
[Github issues](https://github.com/yohannj/rest-hdfs-reading/issues)
