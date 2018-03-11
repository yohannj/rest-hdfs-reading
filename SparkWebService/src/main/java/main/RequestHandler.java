package main;

import java.io.IOException;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.spark.sql.SparkSession;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import utils.Context;
import utils.EFileSystem;
import webservice.LoadService;
import webservice.QueryService;

public class RequestHandler extends AbstractHandler {

	private static final Logger LOGGER = LogManager.getLogger(RequestHandler.class);

	private final LoadService loadService;
	private final QueryService queryService;

	public RequestHandler(SparkSession spark, EFileSystem filesystem) {
		Context c = new Context(spark);
		this.loadService = new LoadService(c, filesystem);
		this.queryService = new QueryService(c);
	}

	@Override
	public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		LOGGER.info("New request on " + target);

		if (!"POST".equalsIgnoreCase(request.getMethod())) {
			response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			baseRequest.setHandled(true);
			return;
		}

		try {
			String body = request.getReader().lines().collect(Collectors.joining("\n"));
			switch (target) {
			case "/load":
				loadService.load(response, body);
				break;
			case "/query":
				queryService.query(response, body);
				break;
			default:
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				baseRequest.setHandled(true);
				break;
			}

		} catch (Throwable t) { // NOSONAR whatever the issue is, we have to properly handle it
			manageThrowable(response, t);
		}

		baseRequest.setHandled(true);
	}

	private void manageThrowable(HttpServletResponse response, Throwable t) {
		LOGGER.error("Failed to manage request", t);
		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	}

}
