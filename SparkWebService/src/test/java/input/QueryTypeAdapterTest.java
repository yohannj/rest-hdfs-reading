package input;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import utils.TestHelper;

public class QueryTypeAdapterTest {

	private static final Gson GSON = new GsonBuilder().registerTypeAdapter(QueryDTO.class, new QueryTypeAdapter()).disableHtmlEscaping().create();

	private static final Class<QueryDTO> CLAZZ = QueryDTO.class;

	@Test
	public void query() {
		QueryDTO dto = TestHelper.loadJson("json/query.json", GSON, CLAZZ);

		assertEquals("SELECT * FROM my_view", dto.getQuery().build());
	}

}
