package com.snowstore.terra.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "TE_T_SYSTEM")
@EntityListeners(AuditingEntityListener.class)
public class System extends AbstractEntity {

	private static final long serialVersionUID = -3462740966497097530L;
	private String systemName;
	private String systemCode;

	@OneToMany(mappedBy = "system", cascade = CascadeType.ALL)
	private Set<SystemVersion> systemVersions = new HashSet<SystemVersion>();

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

	public Set<SystemVersion> getSystemVersions() {
		return systemVersions;
	}

	public void setSystemVersions(Set<SystemVersion> systemVersions) {
		this.systemVersions = systemVersions;
	}

}
