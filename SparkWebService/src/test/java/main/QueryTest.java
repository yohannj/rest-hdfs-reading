package main;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import utils.ParseException;
import utils.Query;

public class QueryTest {

	@Rule
	public final ExpectedException exception = ExpectedException.none();

	@Test
	public void aggregationAndMetric() {
		Query q = new Query(Collections.singleton("foo"), Arrays.asList("foo", "SUM(bar)"), "my_view");

		assertEquals("SELECT foo,SUM(bar) FROM my_view GROUP BY foo", q.build());
	}

	@Test
	public void parseMetrics() throws ParseException {
		Query q = new Query("SELECT SUM(bar) FROM my_view");

		assertEquals("SELECT SUM(bar) FROM my_view", q.build());
	}

	@Test
	public void parseMetricsAndAggregations() throws ParseException {
		Query q = new Query("SELECT foo, SUM(bar) FROM my_view GROUP BY foo");

		assertEquals("SELECT foo,SUM(bar) FROM my_view GROUP BY foo", q.build());
	}

	@Test
	public void parseWhereClause() throws ParseException {
		Query q = new Query("SELECT SUM(bar) FROM my_view WHERE bar > 2");

		assertEquals("SELECT SUM(bar) FROM my_view WHERE bar > 2", q.build());
	}

	@Test
	public void parseFullQuery() throws ParseException {
		Query q = new Query("SELECT foo, SUM(bar) FROM my_view WHERE a IN (1, 2, 3) OR 2 < bar GROUP BY foo");

		assertEquals("SELECT foo,SUM(bar) FROM my_view WHERE a IN (1,2,3) OR 2 < bar GROUP BY foo", q.build());
	}

	@Test
	public void missingGroupBy() throws ParseException {
		exception.expect(ParseException.class);
		new Query("SELECT foo, SUM(bar) FROM my_view");
	}

	@Test
	public void missingAggregateInSelectColumns() throws ParseException {
		exception.expect(ParseException.class);
		new Query("SELECT SUM(bar) FROM my_view GROUP BY foo");
	}

	@Test
	public void weirdAggregate() throws ParseException {
		exception.expect(ParseException.class);
		new Query("SELECT a(,SUM(bar) FROM my_view GROUP BY a(");
	}

	@Test
	public void weirdMetric1() throws ParseException {
		exception.expect(ParseException.class);
		new Query("SELECT SUMbar) FROM my_view");
	}

	@Test
	public void weirdMetric2() throws ParseException {
		exception.expect(ParseException.class);
		new Query("SELECT SUM(bar FROM my_view");
	}

}
