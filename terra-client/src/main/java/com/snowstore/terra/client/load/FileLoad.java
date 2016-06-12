package com.snowstore.terra.client.load;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snowstore.terra.client.util.SystemUtil;

public class FileLoad {
	private static Logger logger = LoggerFactory.getLogger(FileLoad.class);

	public Properties load() {
		logger.info("从本地文件读取配置，地址：" + (SystemUtil.getFilePath() + "backup.properties"));
		Properties props = new Properties();
		try {
			InputStream in = new BufferedInputStream(new FileInputStream((SystemUtil.getFilePath() + "backup.properties")));

			props.load(in);

			in.close();
		} catch (IOException e) {
			logger.error("读取文件错误！");
			e.printStackTrace();
		}
		return props;
	}
}
