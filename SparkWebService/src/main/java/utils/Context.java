package utils;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.spark.sql.SparkSession;

public class Context {

	private final SparkSession spark;
	private final Set<String> availableView;

	public Context(SparkSession spark) {
		this.spark = spark;
		this.availableView = Collections.synchronizedSet(new HashSet<>());
	}

	public SparkSession getSpark() {
		return spark;
	}

	public Set<String> getAvailableView() {
		return availableView;
	}

}
