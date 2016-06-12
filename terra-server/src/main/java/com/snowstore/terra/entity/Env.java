package com.snowstore.terra.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "TE_T_ENV")
@EntityListeners(AuditingEntityListener.class)
public class Env extends AbstractEntity {

	private static final long serialVersionUID = -3462740966497097530L;
	private String envName;
	private String acl;
	@ManyToOne
	private SystemVersion systemVersion;
	@OneToMany(mappedBy = "env", cascade = CascadeType.ALL)
	private Set<EnvPropertyValue> envPropertyValues = new HashSet<EnvPropertyValue>();
	@OneToMany(mappedBy = "env", cascade = CascadeType.ALL)
	private Set<EnvClient> envClients = new HashSet<EnvClient>();

	public String getEnvName() {
		return envName;
	}

	public void setEnvName(String envName) {
		this.envName = envName;
	}

	public String getAcl() {
		return acl;
	}

	public void setAcl(String acl) {
		this.acl = acl;
	}

	public SystemVersion getSystemVersion() {
		return systemVersion;
	}

	public void setSystemVersion(SystemVersion systemVersion) {
		this.systemVersion = systemVersion;
	}

	public Set<EnvPropertyValue> getEnvPropertyValues() {
		return envPropertyValues;
	}

	public void setEnvPropertyValues(Set<EnvPropertyValue> envPropertyValues) {
		this.envPropertyValues = envPropertyValues;
	}

	public Set<EnvClient> getEnvClients() {
		return envClients;
	}

	public void setEnvClients(Set<EnvClient> envClients) {
		this.envClients = envClients;
	}

	@Override
	public Env clone() {
		Env copy = new Env();
		copy.setAcl(this.acl);
		copy.setEnvName(this.envName);
		for (EnvPropertyValue envPropertyValue : envPropertyValues) {
			EnvPropertyValue clone = envPropertyValue.clone();
			clone.setEnv(copy);
			copy.envPropertyValues.add(clone);
		}
		return copy;
	}

}
