package utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonParseException;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

public class GsonHelper {

	private GsonHelper() {
	}

	public static String readName(JsonReader in) throws IOException {
		JsonToken token = in.peek();
		if (token == JsonToken.NAME) {
			return in.nextName();
		} else if (token == JsonToken.NULL) {
			in.nextNull();
			return null;
		} else {
			throw new JsonParseException("Expected to find a label, but found " + token);
		}
	}

	public static String readString(JsonReader in) throws IOException {
		JsonToken token = in.peek();
		switch (token) {
		case BOOLEAN:
			return Boolean.toString(in.nextBoolean());
		case NULL:
			in.nextNull();
			return null;
		case NUMBER:
		case STRING:
			return in.nextString();
		default:
			throw new JsonParseException("Expected to read a string, but found " + token);
		}
	}

	/**
	 * Expects all value to be a string
	 * 
	 * @param in
	 * @return the read object as a dictionary
	 * @throws IOException
	 */
	public static Map<String, String> readObject(JsonReader in) throws IOException {
		JsonToken token = in.peek();
		if (token == JsonToken.BEGIN_OBJECT) {
			Map<String, String> res = new HashMap<>();

			in.beginObject();
			while (!in.peek().equals(JsonToken.END_OBJECT)) {
				res.put(readName(in), readString(in));
			}
			in.endObject();

			return res;
		} else if (token == JsonToken.NULL) {
			in.nextNull();
			return new HashMap<>();
		} else {
			throw new JsonParseException("Expected to find ");
		}
	}

}
