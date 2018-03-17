package utils;

import java.util.Arrays;

public enum EFileSystem {
	HDFS,
	IGFS,
	IN_MEMORY;

	public static String[] names() {
		return Arrays.stream(EFileFormat.values()).map(EFileFormat::name).toArray(String[]::new);
	}
}
