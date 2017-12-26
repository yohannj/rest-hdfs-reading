package webservice;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import input.LoadDTO;
import input.LoadTypeAdapter;
import utils.Constant;
import utils.Query;
import utils.Context;

public class LoadService {

	private static final Gson GSON = new GsonBuilder().registerTypeAdapter(LoadDTO.class, new LoadTypeAdapter()).disableHtmlEscaping().create();

	private final Context context;
	private final SparkSession spark;

	public LoadService(Context context) {
		this.context = context;
		this.spark = context.getSpark();
	}

	public void load(HttpServletResponse response, String json) throws IOException {
		LoadDTO dto = GSON.fromJson(json, LoadDTO.class);
		Dataset<Row> df = spark.read().parquet(dto.getPath());

		String viewName = dto.getViewName();
		String tmpViewName = "TMP_" + viewName + "_" + new Date().getTime();
		df.createOrReplaceTempView(tmpViewName);

		Set<String> askedAggregations = dto.getAggregationColumns();
		Set<String> dfMetrics = Arrays.stream(df.columns()).filter(c -> !askedAggregations.contains(c)).map(c -> "SUM(" + c + ") as " + c)
				.collect(Collectors.toSet());
		Query query = new Query(askedAggregations, dfMetrics, tmpViewName);

		Dataset<Row> aggregatedDf = spark.sql(query.build());
		aggregatedDf.createOrReplaceTempView(viewName);
		aggregatedDf.persist();
		aggregatedDf.count(); // Force persist to be computed to have the best performance even for the first query

		context.getAvailableView().add(viewName);

		response.setContentType(Constant.PLAIN_TEXT_CONTENT_TYPE);
		response.getWriter().print("Created");
		response.setStatus(HttpServletResponse.SC_OK);
	}
}
