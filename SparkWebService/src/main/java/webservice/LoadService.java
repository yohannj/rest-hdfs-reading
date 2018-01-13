package webservice;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import input.LoadDTO;
import input.LoadTypeAdapter;
import utils.Constant;
import utils.Context;
import utils.EFileFormat;
import utils.EFileSystem;
import utils.Query;

public class LoadService {

	private static final Logger LOGGER = LogManager.getLogger(LoadService.class);
	private static final Gson GSON = new GsonBuilder().registerTypeAdapter(LoadDTO.class, new LoadTypeAdapter()).disableHtmlEscaping().create();

	private final Context context;
	private final SparkSession spark;
	private final EFileSystem fileSystem;

	public LoadService(Context context, EFileSystem fileSystem) {
		this.context = context;
		this.spark = context.getSpark();
		this.fileSystem = fileSystem;
	}

	public void load(HttpServletResponse response, String json) throws IOException {
		LoadDTO dto = GSON.fromJson(json, LoadDTO.class);
		Dataset<Row> df;
		switch (dto.getFormat()) {
		case CSV:
			df = spark.read().option("header", "true").option("inferSchema", "true").csv(dto.getPath());
			break;
		case PQT:
			LOGGER.info(org.apache.ignite.hadoop.fs.v1.IgniteHadoopFileSystem.class);
			LOGGER.info(org.apache.ignite.hadoop.fs.v2.IgniteHadoopFileSystem.class);

			df = spark.read().parquet(dto.getPath());
			break;
		default:
			throw new IllegalArgumentException(
					"Invalid format. Expected any of " + String.join(",", EFileFormat.names()) + " but found " + dto.getFormat().name());
		}

		String viewName = dto.getViewName();

		switch (fileSystem) {
		case HDFS:
			String tmpViewName = "TMP_" + viewName + "_" + new Date().getTime();
			df.createOrReplaceTempView(tmpViewName);

			Set<String> groupByColumns = dto.getAggregationColumns();
			Set<String> selectColumns = Arrays.stream(df.columns()).map(c -> groupByColumns.contains(c) ? c : "SUM(" + c + ") as " + c)
					.collect(Collectors.toSet());

			Query query = new Query(groupByColumns, selectColumns, tmpViewName);

			Dataset<Row> aggregatedDf = spark.sql(query.build());
			aggregatedDf.createOrReplaceTempView(viewName);
			aggregatedDf.persist();
			aggregatedDf.count(); // Force persist to be computed to have the best performance even for the first query
			break;
		case IGFS:
			df.createOrReplaceTempView(viewName);
			break;
		default:
			throw new IllegalArgumentException(
					"Invalid file system. Expected any of " + String.join(",", EFileSystem.names()) + " but found " + dto.getFormat().name());
		}

		context.getAvailableView().add(viewName);

		response.setContentType(Constant.PLAIN_TEXT_CONTENT_TYPE);
		response.getWriter().print("Created");
		response.setStatus(HttpServletResponse.SC_OK);
	}

}
