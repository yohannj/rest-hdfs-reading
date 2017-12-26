package input;

import utils.ParseException;
import utils.Query;

public class QueryDTO {

	private Query query;

	public QueryDTO() {
	}

	public Query getQuery() {
		return query;
	}

	public void parseQuery(String query) throws ParseException {
		this.query = new Query(query);
	}
}
