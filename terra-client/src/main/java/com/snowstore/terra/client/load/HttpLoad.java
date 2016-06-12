package com.snowstore.terra.client.load;

import java.io.IOException;
import java.util.Iterator;
import java.util.Properties;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import com.snowstore.terra.client.util.PropertyUtil;
import com.snowstore.terra.client.util.SystemUtil;

public class HttpLoad {
	private static Logger logger = LoggerFactory.getLogger(HttpLoad.class);

	public Properties load() {
		Properties props = new Properties();
		String url;
		try {
			url = PropertyUtil.getValue("server_url");

			String acl = getacl();
			url += "?acl=" + acl;

			logger.info("从服务器读取配置，地址:" + url);
			RestTemplate restTemplate = new RestTemplate();

			String message = restTemplate.getForObject(url, String.class);
			logger.info("服务器返回信息：" + message);
			JSONObject json = new JSONObject(message);
			Iterator keys = json.keys();
			while (keys.hasNext()) {
				String key = (String) keys.next();
				String value = json.get(key).toString();
				props.put(key, value);
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
		return props;
	}

	private String getacl() {
		String acl = SystemUtil.getAcl("acl");
		if (null == acl) {
			acl = SystemUtil.getAcl("acl." + SystemUtil.getProjectName());
		}
		return acl;
	}

	public static void main(String[] args) throws IOException {
		HttpLoad ht = new HttpLoad();
		ht.load();
	}
}
