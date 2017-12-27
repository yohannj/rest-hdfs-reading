package webservice;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

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

		Dataset<String> stringifiedRows = spark.sql(q.build()).map(row -> row.mkString(","), Encoders.STRING());
		String report2 = stringifiedRows.reduce((s1, s2) -> s1 + System.lineSeparator() + s2);

		response.setContentType(Constant.PLAIN_TEXT_CONTENT_TYPE);
		response.getWriter().print(report2);
		response.setStatus(HttpServletResponse.SC_OK);
	}

	private void ensureViewExist(String viewName) throws QueryException {
		if (!context.getAvailableView().contains(viewName)) {
			throw new QueryException("Tried to find view " + viewName + " but it does not exist");
		}
	}
}
