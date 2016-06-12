package com.snowstore.terra.client.save;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snowstore.terra.client.util.SystemUtil;

public class FileSave {
	private static Logger logger = LoggerFactory.getLogger(FileSave.class);

	public void save(Properties props) {

		try {
			FileOutputStream oFile;
			logger.info("备份文件地址：" + SystemUtil.getFilePath() + "backup.properties");
			oFile = new FileOutputStream(SystemUtil.getFilePath() + "backup.properties");

			props.store(oFile, new Date() + " save  file");
			oFile.close();
			logger.info("备份文件完成");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
