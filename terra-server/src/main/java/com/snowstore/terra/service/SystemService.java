package com.snowstore.terra.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.snowstore.terra.compare.TerraComparator;
import com.snowstore.terra.entity.Env;
import com.snowstore.terra.entity.EnvPropertyValue;
import com.snowstore.terra.entity.HistoryEnvPropertyValue;
import com.snowstore.terra.entity.PropertyKey;
import com.snowstore.terra.entity.System;
import com.snowstore.terra.entity.SystemVersion;
import com.snowstore.terra.repository.EnvClientDao;
import com.snowstore.terra.repository.EnvDao;
import com.snowstore.terra.repository.EnvPropertyValueDao;
import com.snowstore.terra.repository.HistoryEnvPropertyValueDao;
import com.snowstore.terra.repository.PropertyKeyDao;
import com.snowstore.terra.repository.SystemDao;
import com.snowstore.terra.repository.SystemVersionDao;
import com.snowstore.terra.vo.SystemTree;

@Service
@Transactional
public class SystemService {

	@Autowired
	private SystemDao systemDao;
	@Autowired
	private SystemVersionDao systemVersionDao;
	@Autowired
	private EnvDao envDao;
	@Autowired
	private EnvClientDao envClientDao;
	@Autowired
	private EnvPropertyValueDao envPropertyValueDao;
	@Autowired
	private EnvService envService;
	@Autowired
	private PropertyKeyDao propertyKeyDao;
	@Autowired
	private HistoryEnvPropertyValueDao historyEnvPropertyValueDao;

	public Long createSystem(String properties, String systemName, String systemCode, String systemVersion, String systemEnv) throws JsonParseException, JsonMappingException, IOException {
		System system = new System();
		system.setSystemName(systemName);
		system.setSystemCode(systemCode);

		SystemVersion version = new SystemVersion();
		version.setSystem(system);
		version.setVersionName(systemVersion);

		system.getSystemVersions().add(version);

		Env env = new Env();
		env.setAcl(envService.generateAcl());
		env.setEnvName(systemEnv);
		env.setSystemVersion(version);

		version.getEnvs().add(env);

		ObjectMapper mapper = new ObjectMapper();
		Map map = mapper.readValue(properties, Map.class);
		for (Object key : map.keySet()) {
			PropertyKey propertyKey = new PropertyKey();
			propertyKey.setDefaultValue((String) map.get(key));
			propertyKey.setPropertyKey(key.toString());
			propertyKey.setSystemVersion(version);
			version.getPropertyKeys().add(propertyKey);

			EnvPropertyValue envPropertyValue = new EnvPropertyValue();
			envPropertyValue.setEnv(env);
			envPropertyValue.setPropertyKey(propertyKey);
			envPropertyValue.setLabelofkey("");
			envPropertyValue.setValue((String) map.get(key));
			env.getEnvPropertyValues().add(envPropertyValue);
		}
		systemDao.save(system);

		// Create envID history begain:
		HistoryEnvPropertyValue newhistory = new HistoryEnvPropertyValue();
		newhistory.setHistoryvalue("N/A");
		newhistory.setHistorykey("N/A");
		newhistory.setStatus("Create env");
		newhistory.setEnvId(env.getId().toString());
		newhistory.setComments("新建环境，名字是" + env.getEnvName());
		historyEnvPropertyValueDao.save(newhistory);

		return env.getId();

	}

	public SystemTree[] getSystemAstree() {
		Iterable<System> all = systemDao.findAll();
		List<SystemTree> result = new ArrayList<SystemTree>();
		for (System system : all) {
			SystemTree root = new SystemTree();
			root.setId("system" + system.getId().toString());
			root.setText(system.getSystemName()+"	("+system.getSystemCode()+")");
			List<SystemTree> versionNodes = new ArrayList<SystemTree>();
			for (SystemVersion version : system.getSystemVersions()) {
				SystemTree versionNode = new SystemTree();
				versionNode.setId("version" + version.getId().toString());
				versionNode.setText(version.getVersionName());
				versionNodes.add(versionNode);
				List<SystemTree> envNodes = new ArrayList<SystemTree>();
				for (Env env : version.getEnvs()) {
					UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
					if (!principal.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CONFIGADMINGROUP")) && env.getEnvName().startsWith("prd")) {
						continue;
					}
					SystemTree envNode = new SystemTree();
					envNode.setId("env" + env.getId().toString());
					envNode.setText(env.getEnvName());
					envNode.setIcon("glyphicon glyphicon-file");
					envNodes.add(envNode);
				}
				// Environment sort
				String[] sortBy = new String[] { "text" };
				int orderBy = 1;// 1:升序，-1：降序
				TerraComparator myCmp = new TerraComparator(sortBy, orderBy);
				Collections.sort(envNodes, myCmp);

				versionNode.setChildren(envNodes.toArray(new SystemTree[envNodes.size()]));

			}
			// Version sort
			String[] sortBy = new String[] { "text" };
			int orderBy = -1;// 1:升序，-1：降序
			TerraComparator myCmp = new TerraComparator(sortBy, orderBy);
			Collections.sort(versionNodes, myCmp);

			root.setChildren(versionNodes.toArray(new SystemTree[versionNodes.size()]));
			result.add(root);
		}
		// System sort
		String[] sortBy = new String[] { "text" };
		int orderBy = 1;// 1:升序，-1：降序
		TerraComparator myCmp = new TerraComparator(sortBy, orderBy);
		Collections.sort(result, myCmp);

		return result.toArray(new SystemTree[result.size()]);
	}

	public void renameVersionOrEnv(String nodeId, String text) {
		if (nodeId.startsWith("version")) {
			SystemVersion version = systemVersionDao.findOne(Long.valueOf(nodeId.substring(7)));
			version.setVersionName(text);
		}
		if (nodeId.startsWith("env")) {
			Env env = envDao.findOne(Long.valueOf(nodeId.substring(3)));
			// history remark begain:
			HistoryEnvPropertyValue newhistory = new HistoryEnvPropertyValue();
			newhistory.setHistoryvalue("N/A");
			newhistory.setHistorykey("N/A");
			newhistory.setStatus("Rename env");
			newhistory.setEnvId(env.getId().toString());
			newhistory.setComments("重命名环境：旧的名字是" + env.getEnvName() + "；新的名字是" + text);
			historyEnvPropertyValueDao.save(newhistory);

			env.setEnvName(text);
		}
	}

	public void copyVersionOrEnv(String nodeId) {
		if (nodeId.startsWith("version")) {
			SystemVersion version = systemVersionDao.findOne(Long.valueOf(nodeId.substring(7)));
			SystemVersion clone = version.clone();
			for (PropertyKey key : clone.getPropertyKeys()) {
				for (Env env : clone.getEnvs()) {
					env.setAcl(envService.generateAcl());
					for (EnvPropertyValue value : env.getEnvPropertyValues()) {
						if (value.getPropertyKey().getPropertyKey().equals(key.getPropertyKey())) {
							value.setPropertyKey(key);
						}
					}
				}
			}
			clone.setSystem(version.getSystem());
			clone.setVersionName("new " + clone.getVersionName());
			systemVersionDao.save(clone);
		}
		if (nodeId.startsWith("env")) {
			Env env = envDao.findOne(Long.valueOf(nodeId.substring(3)));
			Env clone = env.clone();
			clone.setSystemVersion(env.getSystemVersion());
			clone.setEnvName("new " + clone.getEnvName());
			clone.setAcl(envService.generateAcl());
			envDao.save(clone);
			// history remark begain:
			HistoryEnvPropertyValue newhistory = new HistoryEnvPropertyValue();
			newhistory.setHistoryvalue("被复制的环境的所有Value");
			newhistory.setHistorykey("被复制的环境的所有Key");
			newhistory.setStatus("Copy env");
			newhistory.setEnvId(clone.getId().toString());
			newhistory.setComments("新环境的ID为：" + clone.getId().toString() + "；被复制的环境的ID为：" + nodeId.substring(3));
			historyEnvPropertyValueDao.save(newhistory);
		}
	}

	public void deleteVersionOrEnv(String nodeId) {
		if (nodeId.startsWith("version")) {
			deleteVersion(Long.valueOf(nodeId.substring(7)));
		}
		if (nodeId.startsWith("env")) {
			// history remark begain:
			Env env = envDao.findOne(Long.valueOf(nodeId.substring(3)));
			HistoryEnvPropertyValue newhistory = new HistoryEnvPropertyValue();
			newhistory.setHistoryvalue("被删除的环境的所有Value");
			newhistory.setHistorykey("被删除的环境的所有Key");
			newhistory.setStatus("Delete Env");
			newhistory.setEnvId(env.getId().toString());
			newhistory.setComments("被删除的环境的ID：" + env.getId());
			historyEnvPropertyValueDao.save(newhistory);

			deleteEnv(Long.valueOf(nodeId.substring(3)));
		}
	}

	public void deleteVersion(Long versionId) {
		SystemVersion systemVersion = systemVersionDao.findOne(versionId);
		System system = systemVersion.getSystem();
		system.getSystemVersions().remove(systemVersion);
		systemVersionDao.delete(systemVersion);
		if (system.getSystemVersions().size() == 0) {
			systemDao.delete(system);
		}
	}

	public void deleteEnv(Long envId) {
		Env env = envDao.findOne(envId);
		SystemVersion systemVersion = env.getSystemVersion();
		systemVersion.getEnvs().remove(env);
		envDao.delete(env);
		if (systemVersion.getEnvs().size() == 0) {
			deleteVersion(systemVersion.getId());
		}
	}

	public void validateSystemCode(String systemCode) {
		System system = systemDao.findOneBySystemCode(systemCode);
		if (system != null) {
			throw new RuntimeException("系统编号已被[" + system.getSystemName() + "]占用");
		}
	}

	public void validatePropertyKey(Long envId, String propertyKey) {
		Env env = envDao.findOne(envId);
		SystemVersion systemVersion = env.getSystemVersion();
		List<PropertyKey> key = propertyKeyDao.findBySystemVersionAndPropertyKey(systemVersion, propertyKey);
		if (key == null) {// PropertyKey表没有对应的key,P.S:PropertyKey会有重复
			return;
		} else {// PropertyKey有对应的Key,查询EnvPropertyValue中是否有记录
			for (PropertyKey tmpPropertyKey : key) {
				EnvPropertyValue item = envPropertyValueDao.findByEnvAndPropertyKey(env, tmpPropertyKey);
				if (item != null) {
					throw new RuntimeException("配置项已存在");
				}
			}
		}
	}
}
