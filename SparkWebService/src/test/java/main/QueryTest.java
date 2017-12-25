package main;

import static org.junit.Assert.assertEquals;

import java.util.Collections;

import org.junit.Test;

public class QueryTest {

	@Test
	public void aggregationAndMetric() {
		Query q = new Query(Collections.singleton("foo"), Collections.singleton("SUM(bar)"), "my_view");

		assertEquals("SELECT foo,SUM(bar) FROM my_view GROUP BY foo", q.build());
	}
}
