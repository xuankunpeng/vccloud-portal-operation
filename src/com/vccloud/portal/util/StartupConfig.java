package com.vccloud.portal.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class StartupConfig {

	private static Properties prop = new Properties();

	static {
		InputStream is = StartupConfig.class.getClassLoader()
				.getResourceAsStream("startup.properties");
		try {
			prop.load(is);
		} catch (IOException e) {
			// Ignore.
		}
	}

	public static boolean isRunLegacing() {
		return Boolean.valueOf(prop.getProperty("run_legacing"));
	}

	public static long getLegacingPeriod() {
		return Long.valueOf(prop.getProperty("legacing_period"));
	}

}
