package input;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;

import org.apache.commons.lang.NotImplementedException;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import utils.Constant;
import utils.EFileFormat;
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
			switch (k.toLowerCase()) {
			case "aggregationcolumns":
				String[] columns = v.split(Constant.CSV_SEPARATOR);
				dto.setAggregationColumns(new HashSet<>(Arrays.asList(columns)));
				break;
			case "path":
				dto.setPath(v);
				break;
			case "viewname":
				dto.setViewName(v);
				break;
			case "format":
				dto.setFormat(EFileFormat.valueOf(v.toUpperCase()));
				break;
			default:
				throw new IllegalArgumentException("Unknown label found in json: " + k);
			}
		});

		return dto;
	}

}
