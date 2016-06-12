package com.snowstore.terra.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.snowstore.terra.entity.Env;

public interface EnvDao extends CrudRepository<Env, Long> {
	List<Env> findByAcl(String acl);
}
