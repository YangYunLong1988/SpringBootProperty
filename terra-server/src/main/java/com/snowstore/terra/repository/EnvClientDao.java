package com.snowstore.terra.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.snowstore.terra.entity.EnvClient;

public interface EnvClientDao extends CrudRepository<EnvClient, Long> {
	@Query("from EnvClient t where t.env.id=?1 order by created_date desc")
	List<EnvClient> findByEnvId(Long envId);
}
