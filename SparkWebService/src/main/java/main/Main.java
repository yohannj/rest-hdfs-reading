package main;

import org.apache.spark.SparkContext;
import org.apache.spark.sql.SparkSession;
import org.eclipse.jetty.server.Server;

public class Main {

	public static void main(String[] args) throws Exception {
		SparkSession spark = SparkSession.builder().sparkContext(SparkContext.getOrCreate()).getOrCreate();

		Server server = new Server(1337);
		server.setHandler(new WebService(spark));

		server.start();
		server.join();
	}
}
