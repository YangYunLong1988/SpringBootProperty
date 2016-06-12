package com.snowstore.terra.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.snowstore.terra.entity.HistoryEnvPropertyValue;


public interface HistoryEnvPropertyValueDao extends CrudRepository<HistoryEnvPropertyValue, Long> {
	
	@Query("from HistoryEnvPropertyValue t where t.envId=?1 order by created_date desc")
	List<HistoryEnvPropertyValue> findByEnvId(String envId);
}
