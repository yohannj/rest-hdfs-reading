package input;

import java.io.IOException;

import org.apache.commons.lang.NotImplementedException;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import utils.GsonHelper;

public class QueryTypeAdapter extends TypeAdapter<QueryDTO> {

	@Override
	public void write(JsonWriter out, QueryDTO value) throws IOException {
		throw new NotImplementedException();
	}

	@Override
	public QueryDTO read(JsonReader in) throws IOException {
		QueryDTO dto = new QueryDTO();

		GsonHelper.readObject(in).forEach((k, v) -> {
			if ("Query".equalsIgnoreCase(k)) {
				dto.setQuery(v);
			} else {
				throw new IllegalArgumentException("Unknown label found in json: " + k);
			}
		});

		return dto;
	}

}
