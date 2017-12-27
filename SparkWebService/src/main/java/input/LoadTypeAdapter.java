package input;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;

import org.apache.commons.lang.NotImplementedException;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import utils.Constant;
import utils.GsonHelper;

public class LoadTypeAdapter extends TypeAdapter<LoadDTO> {

	@Override
	public void write(JsonWriter out, LoadDTO value) throws IOException {
		throw new NotImplementedException();
	}

	@Override
	public LoadDTO read(JsonReader in) throws IOException {
		LoadDTO dto = new LoadDTO();

		GsonHelper.readObject(in).forEach((k, v) -> {
			if ("AggregationColumns".equalsIgnoreCase(k)) {
				String[] columns = v.split(Constant.CSV_SEPARATOR);
				dto.setAggregationColumns(new HashSet<>(Arrays.asList(columns)));
			} else if ("Path".equalsIgnoreCase(k)) {
				dto.setPath(v);
			} else if ("ViewName".equalsIgnoreCase(k)) {
				dto.setViewName(v);
			} else {
				throw new IllegalArgumentException("Unknown label found in json: " + k);
			}
		});

		return dto;
	}

}
