package com.snowstore.terra.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class EnvDetail {
	private Long envId;
	private String systemName;
	private String systemCode;
	private String versionName;
	private String envName;
	private LinkedHashMap<String, Long> clients = new LinkedHashMap<String, Long>();
	private LinkedHashMap<String, String> clients_time = new LinkedHashMap<String, String>();
	private String acl;
	private List<PropertyVo> propertyVos = new ArrayList<PropertyVo>();
	private List<PropertyHistoryVo> propertyHistoryVos = new ArrayList<PropertyHistoryVo>();
	
	public Long getEnvId() {
		return envId;
	}

	public void setEnvId(Long envId) {
		this.envId = envId;
	}

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public String getSystemCode() {
		return systemCode;
	}

	public void setSystemCode(String systemCode) {
		this.systemCode = systemCode;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public String getEnvName() {
		return envName;
	}

	public void setEnvName(String envName) {
		this.envName = envName;
	}

	public Map<String, Long> getClients() {
		return clients;
	}

	public void setClients(LinkedHashMap<String, Long> clients) {
		this.clients = clients;
	}

	public Map<String, String> getClients_time() {
		return clients_time;
	}

	public void setClients_time(LinkedHashMap<String, String> clients_time) {
		this.clients_time = clients_time;
	}
	public String getAcl() {
		return acl;
	}

	public void setAcl(String acl) {
		this.acl = acl;
	}

	public List<PropertyVo> getPropertyVos() {
		return propertyVos;
	}

	public void setPropertyVos(List<PropertyVo> propertyVos) {
		this.propertyVos = propertyVos;
	}

	public List<PropertyHistoryVo> getPropertyHistoryVos() {
		return propertyHistoryVos;
	}

	public void setPropertyHistoryVos(List<PropertyHistoryVo> propertyHistoryVos) {
		this.propertyHistoryVos = propertyHistoryVos;
	}
	
	
}
