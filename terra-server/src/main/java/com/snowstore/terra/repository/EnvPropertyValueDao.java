package com.snowstore.terra.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.snowstore.terra.entity.Env;
import com.snowstore.terra.entity.EnvPropertyValue;
import com.snowstore.terra.entity.PropertyKey;

public interface EnvPropertyValueDao extends CrudRepository<EnvPropertyValue, Long> {

	List<EnvPropertyValue> findByEnvId(Long envId);

	List<EnvPropertyValue> findByPropertyKey(PropertyKey propertyKey);
	
	EnvPropertyValue findByEnvAndPropertyKey(Env env,PropertyKey key);
}
