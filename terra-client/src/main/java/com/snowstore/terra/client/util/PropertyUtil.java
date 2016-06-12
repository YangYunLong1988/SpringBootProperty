package com.snowstore.terra.client.util;

import java.io.IOException;
import java.util.Properties;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public class PropertyUtil {
	private static Properties props = null;
	private static Properties localProps = null;

	public static String getValue(String key) throws IOException {
		if (null == props) {
			Resource resource = new ClassPathResource("/terra.properties");
			props = PropertiesLoaderUtils.loadProperties(resource);

		}
		return props.getProperty(key);
	}

	public static String getLocalValue(String key) throws IOException {
		if (null == localProps) {
			Resource resource = new ClassPathResource("/terra_local.properties");
			localProps = PropertiesLoaderUtils.loadProperties(resource);
		}
		return localProps.getProperty(key);
	}

}
