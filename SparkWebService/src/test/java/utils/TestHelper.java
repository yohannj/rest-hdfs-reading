package utils;

import java.io.IOException;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.google.gson.Gson;

public class TestHelper {

	public static String getResources(String path) {
		try {
			return Resources.toString(Resources.getResource(path), Charsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException("Failed to retrieve resource: " + path);
		}
	}

	public static <T> T loadJson(String path, Gson gson, Class<T> clazz) {
		String json = TestHelper.getResources(path);
		return gson.fromJson(json, clazz);
	}
}
