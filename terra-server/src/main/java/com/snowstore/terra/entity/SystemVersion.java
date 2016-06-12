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
@Table(name = "TE_SYSTEM_VERSION")
@EntityListeners(AuditingEntityListener.class)
public class SystemVersion extends AbstractEntity {

	private static final long serialVersionUID = -3462740966497097530L;
	private String versionName;

	@ManyToOne
	private System system;
	@OneToMany(mappedBy = "systemVersion", cascade = CascadeType.ALL)
	private Set<Env> envs = new HashSet<Env>();
	@OneToMany(mappedBy = "systemVersion", cascade = CascadeType.ALL)
	private Set<PropertyKey> propertyKeys = new HashSet<PropertyKey>();

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public System getSystem() {
		return system;
	}

	public void setSystem(System system) {
		this.system = system;
	}

	public Set<Env> getEnvs() {
		return envs;
	}

	public void setEnvs(Set<Env> envs) {
		this.envs = envs;
	}

	public Set<PropertyKey> getPropertyKeys() {
		return propertyKeys;
	}

	public void setPropertyKeys(Set<PropertyKey> propertyKeys) {
		this.propertyKeys = propertyKeys;
	}

	@Override
	public SystemVersion clone() {
		SystemVersion copy = new SystemVersion();
		copy.setVersionName(this.getVersionName());
		for (Env env : this.envs) {
			Env clone = env.clone();
			clone.setSystemVersion(copy);
			copy.envs.add(clone);
		}
		for (PropertyKey key : this.propertyKeys) {
			PropertyKey clone = key.clone();
			clone.setSystemVersion(copy);
			copy.propertyKeys.add(clone);
		}
		return copy;
	}

}
