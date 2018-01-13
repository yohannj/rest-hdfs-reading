package utils;

import java.util.Arrays;

public enum EFileFormat {
	CSV,
	PQT;

	public static String[] names() {
		return Arrays.stream(EFileFormat.values()).map(EFileFormat::name).toArray(String[]::new);
	}
}
