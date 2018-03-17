package utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Query {

	private static final Pattern QUERY_PARSER;
	private static final Pattern AGGREGATION_PARSER = Pattern.compile("^\\w+$");
	private static final Pattern METRIC_PARSER = Pattern.compile("^(?<aggFunc>\\w+)\\((?<fieldName>\\w+)\\)$");
	private static final String FIELD_SEPARATOR = ",";
	static {
		StringBuilder queryParserBuilder = new StringBuilder();
		queryParserBuilder.append("^");
		queryParserBuilder.append("SELECT (?<selectFields>.*?)");
		queryParserBuilder.append(" FROM (?<viewName>\\w+)");
		queryParserBuilder.append("(?: WHERE (?<whereClause>.*?))?");
		queryParserBuilder.append("(?: GROUP BY (?<groupByClause>.*?))?");
		queryParserBuilder.append("(?: ORDER BY (?<orderByClause>.*?))?");
		queryParserBuilder.append("$");

		QUERY_PARSER = Pattern.compile(queryParserBuilder.toString(), Pattern.CASE_INSENSITIVE);
	}

	private List<String> selectFields;
	private List<String> groupByClause;
	private List<String> orderByClause;
	private String whereClause;
	private String viewName;

	/**
	 * Basic constructor that will create a query in the form
	 * <code>SELECT aggregations,computedMetrics FROM viewName GROUP BY aggregations</code>
	 * 
	 * @param aggregations
	 * @param selectFields
	 * @param viewName
	 */
	public Query(Collection<String> aggregations, Collection<String> selectFields, String viewName) {
		this.selectFields = new ArrayList<>();
		this.selectFields.addAll(selectFields);
		this.groupByClause = new ArrayList<>(aggregations);
		this.orderByClause = Collections.emptyList();
		this.whereClause = "";
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
			this.selectFields = readList(m, "selectFields");
			this.whereClause = Optional.ofNullable(m.group("whereClause")).orElse("");
			this.groupByClause = readList(m, "groupByClause");
			this.orderByClause = readList(m, "orderByClause");
			this.viewName = m.group("viewName");
		} else {
			throw new ParseException(
					"Unable to parse input query. Expected a query in the form 'SELECT column1, column2, sum(column3) FROM my_view WHERE column1 > x GROUP BY column1, column2'.");
		}

		ensureQueryValid();
	}

	public String getViewName() {
		return viewName;
	}

	public String build() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT ").append(String.join(Constant.CSV_SEPARATOR, selectFields)).append(" FROM ").append(viewName);

		if (!whereClause.isEmpty()) {
			query.append(" WHERE ").append(whereClause);
		}

		if (!groupByClause.isEmpty()) {
			query.append(" GROUP BY ").append(String.join(Constant.CSV_SEPARATOR, groupByClause));
		}

		if (!orderByClause.isEmpty()) {
			query.append(" ORDER BY ").append(String.join(Constant.CSV_SEPARATOR, orderByClause));
		}

		return query.toString();
	}

	private List<String> readList(Matcher m, String groupName) {
		String val = m.group(groupName);
		return val == null ? Collections.emptyList() : Arrays.stream(val.split(FIELD_SEPARATOR)).collect(Collectors.toList());
	}

	/**
	 * Some basic check to ensure the query is valid
	 * 
	 * @throws ParseException
	 */
	private void ensureQueryValid() throws ParseException {
		for (String agg : groupByClause) {
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
			if (!field.equals("*") && !m.find() && !groupByClause.contains(field)) {
				throw new ParseException("Expected " + field + " to be an aggregation but missing in GROUP BY columns.");
			}
		}
	}

}
