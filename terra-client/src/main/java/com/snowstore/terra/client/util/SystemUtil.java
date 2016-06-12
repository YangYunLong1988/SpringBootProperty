package com.snowstore.terra.client.util;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SystemUtil {
	static final Properties PROPERTIES = new Properties(System.getProperties());
	private static Logger logger = LoggerFactory.getLogger(SystemUtil.class);

	public static String getIP() {
		InetAddress addr;
		try {
			addr = InetAddress.getLocalHost();
			String ip = addr.getHostAddress().toString();// 获得本机IP
			return ip;
		} catch (UnknownHostException e) {

			e.printStackTrace();
			return null;
		}

	}

	public static String getFilePath() {
		SystemUtil util = new SystemUtil();
		String path = util.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		path = path.substring(0, path.indexOf("terra-client"));
		return path;
	}

	public static String getProjectName() {

		String path = getFilePath();
		logger.info("path= " + path);
		String separator = path.substring(path.length() - 1, path.length());
		String pName = path.substring(0, path.lastIndexOf(separator));
		pName = pName.substring(0, pName.lastIndexOf(separator));
		pName = pName.substring(0, pName.lastIndexOf(separator));
		pName = pName.substring(pName.lastIndexOf(separator) + 1, pName.length());
		return pName;
	}

	public static void main(String args[]) {
		System.out.println(getProjectName());
		System.out.println(getAcl("acl.mars"));
	}

	public static String getAcl(String aclName) {
		aclName = "-D" + aclName + "=";
		List<String> inputArguments = ManagementFactory.getRuntimeMXBean().getInputArguments();
		logger.info("aclname= " + aclName);
		for (String str : inputArguments) {
			logger.info("inputArguments= " + str);
			if (str.indexOf(aclName) >= 0) {
				return str.substring(str.indexOf(aclName) + aclName.length(), str.length());
			}
		}

		Map map = System.getenv();
		Iterator it = map.entrySet().iterator();
		while (it.hasNext()) {
			Entry entry = (Entry) it.next();
			if (entry.getKey().equals("acl")) {
				logger.info("System Environment:" + entry.getKey() + "=" + entry.getValue());
				return entry.getValue().toString();
			}
		}

		return null;
	}
}
