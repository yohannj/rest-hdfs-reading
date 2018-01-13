package main;

import org.apache.spark.SparkContext;
import org.apache.spark.sql.SparkSession;
import org.eclipse.jetty.server.Server;

import utils.EFileSystem;

public class MainHdfs {

	public static void main(String[] args) throws Exception {
		SparkSession spark = SparkSession.builder().sparkContext(SparkContext.getOrCreate()).getOrCreate();

		Server server = new Server(1337);
		server.setHandler(new RequestHandler(spark, EFileSystem.HDFS));

		server.start();
		server.join();
	}
}
