package input;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import utils.TestHelper;

public class LoadTypeAdapterTest {

	private static final Gson GSON = new GsonBuilder().registerTypeAdapter(LoadDTO.class, new LoadTypeAdapter()).disableHtmlEscaping().create();

	private static final Class<LoadDTO> CLAZZ = LoadDTO.class;

	@Test
	public void allField() {
		LoadDTO dto = TestHelper.loadJson("json/loadAllField.json", GSON, CLAZZ);
		Set<String> aggregationColumns = dto.getAggregationColumns();

		assertEquals(2, aggregationColumns.size());
		assertTrue(aggregationColumns.contains("foo"));
		assertTrue(aggregationColumns.contains("bar"));

		assertEquals("/some/path", dto.getPath());
		assertEquals("some_view_name", dto.getViewName());
	}

	@Test
	public void nullAggregations() {
		LoadDTO dto = TestHelper.loadJson("json/loadNoAggregations.json", GSON, CLAZZ);
		Set<String> aggregationColumns = dto.getAggregationColumns();

		assertTrue(aggregationColumns.isEmpty());
		assertEquals("/some/path", dto.getPath());
		assertEquals("some_view_name", dto.getViewName());
	}

}
