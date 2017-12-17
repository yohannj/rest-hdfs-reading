package main;

import java.io.IOException;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import input.LoadDTO;
import input.LoadTypeAdapter;
import input.QueryDTO;
import input.QueryTypeAdapter;

public class WebService extends AbstractHandler {

	private static final Logger LOGGER = LogManager.getLogger(WebService.class);
	private static final String PLAIN_TEXT_CONTENT_TYPE = "text/plain; chartset=utf-8";
	private static final Gson GSON = new GsonBuilder().registerTypeAdapter(LoadDTO.class, new LoadTypeAdapter())
			.registerTypeAdapter(QueryDTO.class, new QueryTypeAdapter()).disableHtmlEscaping().create();

	private final SparkSession spark;

	public WebService(SparkSession spark) {
		this.spark = spark;
	}

	@Override
	public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		LOGGER.info(target);

		String body = request.getReader().lines().collect(Collectors.joining("\n"));
		switch (target) {
		case "/load":
			load(body);
			response.setStatus(HttpServletResponse.SC_OK);
			response.setContentType(PLAIN_TEXT_CONTENT_TYPE);
			baseRequest.setHandled(true);
			break;
		case "/query":
			//TODO
			response.setStatus(HttpServletResponse.SC_OK);
			response.setContentType(PLAIN_TEXT_CONTENT_TYPE);
			baseRequest.setHandled(true);
			break;
		default:
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			baseRequest.setHandled(true);
			break;
		}
	}

	private String load(String body) {
		LoadDTO dto = GSON.fromJson(body, LoadDTO.class);

		String viewName = dto.getViewName();
		Dataset<Row> df = spark.read().parquet(dto.getPath()); //TODO add group by
		df.createOrReplaceTempView(viewName);
		return viewName;
	}

	@SuppressWarnings("unused")
	private void manageException(Request baseRequest, HttpServletResponse response, Exception e) {
		LOGGER.error("Failed to manage request", e);
		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		baseRequest.setHandled(true);
	}
}
