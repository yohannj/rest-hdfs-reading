package webservice;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.SparkSession;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import input.QueryDTO;
import input.QueryTypeAdapter;
import utils.Constant;
import utils.Context;
import utils.Query;
import utils.QueryException;

public class QueryService {

	private static final Logger LOGGER = LogManager.getLogger(QueryService.class);
	private static final Gson GSON = new GsonBuilder().registerTypeAdapter(QueryDTO.class, new QueryTypeAdapter()).disableHtmlEscaping().create();

	private final Context context;
	private final SparkSession spark;

	public QueryService(Context context) {
		this.context = context;
		this.spark = context.getSpark();
	}

	public void query(HttpServletResponse response, String json) throws QueryException, IOException {
		Query q = GSON.fromJson(json, QueryDTO.class).getQuery();
		ensureViewExist(q.getViewName());

		Dataset<String> rows = spark.sql(q.build()).map(row -> row.mkString(","), Encoders.STRING()).persist();
		rows.count(); //Force persist for benchmark

		long start;
		long end;
		StringBuilder sb;

		//Test 1: group all input data locally
		start = System.nanoTime();

		sb = new StringBuilder();
		rows.collectAsList().forEach(row -> sb.append(row).append(System.lineSeparator()));
		String report1 = sb.toString();

		end = System.nanoTime();
		LOGGER.info("Elapsed time for test 1: " + (end - start) + "ns");

		//Test 2: reduce
		start = System.nanoTime();

		String report2 = rows.reduce((s1, s2) -> s1 + System.lineSeparator() + s2);

		end = System.nanoTime();
		LOGGER.info("Elapsed time for test 2: " + (end - start) + "ns");

		response.getWriter().print(report1 + System.lineSeparator() + System.lineSeparator() + report2);
		response.setStatus(HttpServletResponse.SC_OK);
		response.setContentType(Constant.PLAIN_TEXT_CONTENT_TYPE);
	}

	private void ensureViewExist(String viewName) throws QueryException {
		if (!context.getAvailableView().contains(viewName)) {
			throw new QueryException("Tried to find view " + viewName + " but it does not exist");
		}
	}
}
