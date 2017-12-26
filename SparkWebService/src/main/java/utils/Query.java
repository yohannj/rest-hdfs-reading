package utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Query {

	private static final Pattern QUERY_PARSER = Pattern.compile("^SELECT (.*?) FROM (\\w+)(?: GROUP BY (.*?))?$", Pattern.CASE_INSENSITIVE);
	private static final Pattern AGGREGATION_PARSER = Pattern.compile("^\\w+$");
	private static final Pattern METRIC_PARSER = Pattern.compile("^(\\w+)\\((\\w+)\\)$");
	private static final String FIELD_SEPARATOR = ",";

	private List<String> selectFields;
	private List<String> aggregations;
	private String viewName;

	/**
	 * Basic constructor that will create a query in the form
	 * <code>SELECT aggregations,computedMetrics FROM viewName GROUP BY aggregations</code>
	 * 
	 * @param aggregations
	 * @param computedMetrics
	 * @param viewName
	 */
	public Query(Collection<String> aggregations, Collection<String> computedMetrics, String viewName) {
		this.selectFields = new ArrayList<>();
		this.selectFields.addAll(computedMetrics);
		this.aggregations = new ArrayList<>(aggregations);
		this.viewName = viewName;
	}

	/**
	 * Constructor that will parse the given query and ensure it is
	 * valid/handled
	 * 
	 * @param unparsedQuery
	 * @throws ParseException
	 */
	public Query(String unparsedQuery) throws ParseException {
		String singleLineQuery = unparsedQuery.replaceAll("\\s+", " ").replaceAll("\\s?,\\s?", FIELD_SEPARATOR).trim();

		Matcher m = QUERY_PARSER.matcher(singleLineQuery);
		if (m.find()) {
			this.selectFields = Arrays.stream(m.group(1).split(FIELD_SEPARATOR)).collect(Collectors.toList());
			this.aggregations = m.group(3) == null ? Collections.emptyList() : Arrays.stream(m.group(3).split(FIELD_SEPARATOR)).collect(Collectors.toList());
			this.viewName = m.group(2);
		} else {
			throw new ParseException(
					"Unable to parse input query. Expected a query in the form 'SELECT column1, column2, sum(column3) FROM my_view GROUP BY column1, column2'.");
		}

		ensureQueryValid();
	}

	public String getViewName() {
		return viewName;
	}

	public String build() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT ").append(String.join(Constant.CSV_SEPARATOR, this.selectFields)).append(" FROM ").append(viewName);

		if (!aggregations.isEmpty()) {
			query.append(" GROUP BY ").append(String.join(Constant.CSV_SEPARATOR, this.aggregations));
		}

		return query.toString();
	}

	private void ensureQueryValid() throws ParseException {
		for (String agg : aggregations) {
			Matcher m = AGGREGATION_PARSER.matcher(agg);
			if (!m.find()) {
				throw new ParseException("Found an incorrectly formatted aggregate in GROUP BY: " + agg);
			}

			if (!selectFields.contains(agg)) {
				throw new ParseException("Found " + agg + " in GROUP BY but missing in SELECT columns.");
			}
		}

		for (String field : selectFields) {
			Matcher m = METRIC_PARSER.matcher(field);
			if (!m.find() && !aggregations.contains(field)) {
				throw new ParseException("Expected " + field + " to be an aggregation but missing in GROUP BY columns.");
			}
		}
	}

}
