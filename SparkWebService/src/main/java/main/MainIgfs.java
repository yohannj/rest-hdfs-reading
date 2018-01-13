package main;

import org.apache.spark.SparkContext;
import org.apache.spark.sql.SparkSession;
import org.eclipse.jetty.server.Server;

import utils.EFileSystem;

public class MainIgfs {

	public static void main(String[] args) throws Exception {
		SparkSession spark = SparkSession.builder().sparkContext(SparkContext.getOrCreate()).getOrCreate();
		spark.sparkContext().hadoopConfiguration().set("fs.defaultFS", "igfs://igfs@/");
		spark.sparkContext().hadoopConfiguration().set("fs.igfs.impl", "org.apache.ignite.hadoop.fs.v1.IgniteHadoopFileSystem");
		spark.sparkContext().hadoopConfiguration().set("fs.AbstractFileSystem.igfs.impl", "org.apache.ignite.hadoop.fs.v2.IgniteHadoopFileSystem");

		Server server = new Server(1338);
		server.setHandler(new RequestHandler(spark, EFileSystem.IGFS));

		server.start();
		server.join();
	}
}
