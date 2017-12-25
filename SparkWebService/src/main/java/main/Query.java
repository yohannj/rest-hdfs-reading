package main;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import utils.Constant;

public class Query {

	private List<String> aggregations;
	private List<String> metrics;
	private String viewName;

	public Query(Collection<String> aggregations, Collection<String> computedMetrics, String viewName) {
		this.aggregations = new ArrayList<>(aggregations);
		this.metrics = new ArrayList<>(computedMetrics);
		this.viewName = viewName;
	}

	public String build() {
		String queryAggregations = String.join(Constant.CSV_SEPARATOR, this.aggregations);
		String querySelectColumns = queryAggregations + Constant.CSV_SEPARATOR + String.join(Constant.CSV_SEPARATOR, this.metrics);

		StringBuilder query = new StringBuilder();
		query.append("SELECT ").append(querySelectColumns).append(" FROM ").append(viewName);

		if (!aggregations.isEmpty()) {
			query.append(" GROUP BY ").append(queryAggregations);
		}

		return query.toString();
	}

}
