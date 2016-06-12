package com.snowstore.terra.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.snowstore.terra.compare.TerraComparator;
import com.snowstore.terra.entity.Env;
import com.snowstore.terra.entity.EnvClient;
import com.snowstore.terra.entity.EnvPropertyValue;
import com.snowstore.terra.entity.HistoryEnvPropertyValue;
import com.snowstore.terra.entity.PropertyKey;
import com.snowstore.terra.entity.SystemVersion;
import com.snowstore.terra.repository.EnvClientDao;
import com.snowstore.terra.repository.EnvDao;
import com.snowstore.terra.repository.EnvPropertyValueDao;
import com.snowstore.terra.repository.HistoryEnvPropertyValueDao;
import com.snowstore.terra.repository.PropertyKeyDao;
import com.snowstore.terra.vo.EnvDetail;
import com.snowstore.terra.vo.PropertyHistoryVo;
import com.snowstore.terra.vo.PropertyVo;

@Service
@Transactional
public class EnvService {

	@Autowired
	private EnvDao envDao;
	@Autowired
	private EnvClientDao envClientDao;
	@Autowired
	private EnvPropertyValueDao envPropertyValueDao;
	@Autowired
	private PropertyKeyDao propertyKeyDao;
	@Autowired
	private HistoryEnvPropertyValueDao historyEnvPropertyValueDao;

	public EnvDetail getEvnDetail(Long envId) {
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		EnvDetail envDetail = new EnvDetail();
		Env env = envDao.findOne(envId);
		envDetail.setEnvId(envId);
		envDetail.setSystemName(env.getSystemVersion().getSystem().getSystemName());
		envDetail.setSystemCode(env.getSystemVersion().getSystem().getSystemCode());
		envDetail.setVersionName(env.getSystemVersion().getVersionName());
		envDetail.setEnvName(env.getEnvName());
		envDetail.setAcl(env.getAcl());
		List<EnvClient> clients = envClientDao.findByEnvId(envId);
		LinkedHashMap<String, Long> clientMap = new LinkedHashMap<String, Long>();
		LinkedHashMap<String, String> clientMap_time = new LinkedHashMap<String, String>();
		for (EnvClient envClient : clients) {
			if (clientMap.containsKey(envClient.getIp())) {
				clientMap.put(envClient.getIp(), clientMap.get(envClient.getIp()).longValue() + 1);
				System.out.println(envClient.getTime());

				clientMap_time.put(df.format(envClient.getTime()).toString(), envClient.getIp());
			} else {
				clientMap.put(envClient.getIp(), Long.valueOf(1));
				clientMap_time.put(df.format(envClient.getTime()).toString(), envClient.getIp());
			}
		}
		envDetail.setClients(clientMap);
		envDetail.setClients_time(clientMap_time);

		List<EnvPropertyValue> envPropertyValues = envPropertyValueDao.findByEnvId(envId);
		for (EnvPropertyValue envPropertyValue : envPropertyValues) {
			PropertyVo propertyVo = new PropertyVo();
			propertyVo.setEnvPropertyValueId(envPropertyValue.getId());
			propertyVo.setKey(envPropertyValue.getPropertyKey().getPropertyKey());
			propertyVo.setValue(envPropertyValue.getValue());
			propertyVo.setLabelofkey(envPropertyValue.getLabelofkey());
			propertyVo.setMemo(envPropertyValue.getPropertyKey().getMemo());
			propertyVo.setCreateTime(envPropertyValue.getCreatedDate() == null ? null : envPropertyValue
					.getCreatedDate());
			propertyVo.setUpdateTime(envPropertyValue.getLastModifiedDate() == null ? null : envPropertyValue
					.getLastModifiedDate());
			envDetail.getPropertyVos().add(propertyVo);
		}

		String[] sortBy = new String[] { "labelofkey","key" };
		int orderBy = 1;// 1:升序，-1：降序
		TerraComparator PropertyCmp = new TerraComparator(sortBy, orderBy);
		Collections.sort(envDetail.getPropertyVos(), PropertyCmp);

		return envDetail;
	}

	public EnvDetail getPropertyHistory(String envId) {
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		EnvDetail envDetailhistory=new EnvDetail();
		List<HistoryEnvPropertyValue> all = historyEnvPropertyValueDao.findByEnvId(envId);
		for (HistoryEnvPropertyValue historyEnvPropertyValue : all) {
			
			PropertyHistoryVo propertyHistoryVo=new PropertyHistoryVo();
			propertyHistoryVo.setModifiedby(historyEnvPropertyValue.getLastModifiedBy());
			propertyHistoryVo.setKey(historyEnvPropertyValue.getHistorykey());
			propertyHistoryVo.setPreviousvalue(historyEnvPropertyValue.getPreviousvalue());
			propertyHistoryVo.setValue(historyEnvPropertyValue.getHistoryvalue());
			propertyHistoryVo.setAction(historyEnvPropertyValue.getStatus());
			propertyHistoryVo.setComments(historyEnvPropertyValue.getComments());
			propertyHistoryVo.setUserupdateTime(df.format(historyEnvPropertyValue.getLastModifiedDate()));
			envDetailhistory.getPropertyHistoryVos().add(propertyHistoryVo);
			
		}
		 return envDetailhistory;
	}
	
	public void updateAcl(Long envId) {
		Env env = envDao.findOne(envId);
		env.setAcl(generateAcl());
	}

	public Long updateProperty(Long envPropertyId, String propertyValue, String propertyMemo,String propertyLabel) {
		EnvPropertyValue property = envPropertyValueDao.findOne(envPropertyId);
		if(!property.getValue().equals(propertyValue)){
			//1.Update History: save history of value
			HistoryEnvPropertyValue newhistory=new HistoryEnvPropertyValue();	
			newhistory.setPreviousvalue(property.getValue());
			newhistory.setHistoryvalue(propertyValue);
			newhistory.setHistorykey(property.getPropertyKey().getPropertyKey());
			newhistory.setStatus("Update item");
			newhistory.setEnvId(property.getEnv().getId().toString());
			historyEnvPropertyValueDao.save(newhistory);
			
			//save the new value
			property.setValue(propertyValue); 
		}
		
		List<EnvPropertyValue> all = envPropertyValueDao.findByPropertyKey(property.getPropertyKey());
		for (EnvPropertyValue envPropertyValue : all) {	
			if(!propertyLabel.equals(envPropertyValue.getLabelofkey())){
			envPropertyValue.setLabelofkey(propertyLabel);
			}
		}
		if(!StringUtils.equals(property.getPropertyKey().getMemo(), propertyMemo)){
			property.getPropertyKey().setMemo(propertyMemo);
		}
		return property.getEnv().getId();
	}

	public Long deleteProperty(Long envPropertyId) {
		EnvPropertyValue property = envPropertyValueDao.findOne(envPropertyId);
		List<EnvPropertyValue> all = envPropertyValueDao.findByPropertyKey(property.getPropertyKey());

		// 2.Delete action and his History: save history of value, key from every envid except
		// prd

		for (EnvPropertyValue envPropertyValue : all) {
			HistoryEnvPropertyValue newhistory = new HistoryEnvPropertyValue();
			newhistory.setHistoryvalue(envPropertyValue.getValue());
			newhistory.setHistorykey(property.getPropertyKey().getPropertyKey());
			newhistory.setStatus("Delete item");

			if (!property.getEnv().getEnvName().contains("prd")) {// 不是从prd环境删除属性
				if (!envPropertyValue.getEnv().getEnvName().contains("prd")) {
					newhistory.setEnvId(envPropertyValue.getEnv().getId().toString());
					historyEnvPropertyValueDao.save(newhistory);

					envPropertyValueDao.delete(envPropertyValue);
				} else {
					continue;
				}
			} else {// 从prd环境删除属性
				if (envPropertyValue.getEnv().getEnvName().contains("prd")) {
					newhistory.setEnvId(envPropertyValue.getEnv().getId().toString());
					historyEnvPropertyValueDao.save(newhistory);

					envPropertyValueDao.delete(envPropertyValue);
				} else {
					continue;
				}
			}
		}
		propertyKeyDao.findOne(property.getPropertyKey().getId()).setStatus("已删除");
		return property.getEnv().getId();
	}

	public Long addProperty(Long envId, String propertyKey, String propertyValue, String propertyLabel) {
		Env env = envDao.findOne(envId);
		SystemVersion systemVersion = env.getSystemVersion();
		PropertyKey newPropertyKey = new PropertyKey();
		newPropertyKey.setPropertyKey(propertyKey);
		newPropertyKey.setStatus("正常");
		newPropertyKey.setDefaultValue(propertyValue);
		newPropertyKey.setSystemVersion(systemVersion);
		propertyKeyDao.save(newPropertyKey);

		for (Env e : systemVersion.getEnvs()) {
			EnvPropertyValue newEnvPropertyValue = new EnvPropertyValue();
			newEnvPropertyValue.setEnv(e);
			newEnvPropertyValue.setPropertyKey(newPropertyKey);
			if (env.getEnvName().contains("prd")) {	//由生产环境添加属性
				if (e.getEnvName().contains("prd")) {
					newEnvPropertyValue.setValue(propertyValue);
					newEnvPropertyValue.setLabelofkey(propertyLabel);
				} else {
					continue;
				}
			} else {//由除生产环境之外的环境添加属性
				if (e.getEnvName().contains("prd")) {
					continue;
				} else {
					newEnvPropertyValue.setValue(propertyValue);
					newEnvPropertyValue.setLabelofkey(propertyLabel);
				}
			}

			envPropertyValueDao.save(newEnvPropertyValue);

			// 3.Create History: save history of value, key...
			HistoryEnvPropertyValue newhistory = new HistoryEnvPropertyValue();
			if (env.getEnvName().contains("prd")) {	//由生产环境添加属性
				if (e.getEnvName().contains("prd")) {
					newhistory.setHistoryvalue(propertyValue);
					newhistory.setEnvId(e.getId().toString());
					newhistory.setHistorykey(propertyKey);
					newhistory.setStatus("Create item");
					historyEnvPropertyValueDao.save(newhistory);
				} else {
					continue;
				}
			} else {//由除生产环境之外的环境添加属性
				if (e.getEnvName().contains("prd")) {
					continue;
				} else {
					newhistory.setHistoryvalue(propertyValue);
					newhistory.setEnvId(e.getId().toString());
					newhistory.setHistorykey(propertyKey);
					newhistory.setStatus("Create item");
					historyEnvPropertyValueDao.save(newhistory);
				}
			}

		}
		return envId;
	}

	public byte[] export(Long envId) {
		Env env = envDao.findOne(envId);
		Properties properties = new Properties();
		for (EnvPropertyValue value : env.getEnvPropertyValues()) {
			properties.put(value.getPropertyKey().getPropertyKey(), value.getValue());
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			properties.store(out, env.getSystemVersion().getSystem().getSystemName() + "\n"
					+ env.getSystemVersion().getSystem().getSystemCode() + "\n"
					+ env.getSystemVersion().getVersionName() + "\n" + env.getEnvName());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return out.toByteArray();
	}

	public String generateAcl() {
		String random = RandomStringUtils.random(32, true, true);
		if (envDao.findByAcl(random).size() == 0) {
			return random;
		} else {
			return generateAcl();
		}
	}

	public Map<String, String> export(String acl, String ip) {
		Map<String, String> map = new HashMap<String, String>();
		Env env = envDao.findByAcl(acl).get(0);
		if (env == null) {
			throw new RuntimeException("配置提取码不正确");
		}
		List<EnvPropertyValue> envPropertyValues = envPropertyValueDao.findByEnvId(env.getId());
		for (EnvPropertyValue envPropertyValue : envPropertyValues) {
			map.put(envPropertyValue.getPropertyKey().getPropertyKey(), envPropertyValue.getValue());
		}

		EnvClient envClient = new EnvClient();
		envClient.setEnv(env);
		envClient.setIp(ip);
		envClientDao.save(envClient);
		return map;
	}

}
