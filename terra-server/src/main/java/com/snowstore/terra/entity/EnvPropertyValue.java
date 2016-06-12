package com.snowstore.terra.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "TE_T_ENV_PROPERTY_VALUE")
@EntityListeners(AuditingEntityListener.class)
public class EnvPropertyValue extends AbstractEntity {

	private static final long serialVersionUID = -3101369402947242803L;
	@Column(length = 5000)
	private String value;
	@Column(length = 5000)
	private String labelofkey;
	@ManyToOne
	private Env env;
	@ManyToOne
	private PropertyKey propertyKey;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getLabelofkey() {
		return labelofkey;
	}

	public void setLabelofkey(String labelofkey) {
		this.labelofkey = labelofkey;
	}

	public Env getEnv() {
		return env;
	}

	public void setEnv(Env env) {
		this.env = env;
	}

	public PropertyKey getPropertyKey() {
		return propertyKey;
	}

	public void setPropertyKey(PropertyKey propertyKey) {
		this.propertyKey = propertyKey;
	}

	@Override
	public EnvPropertyValue clone() {
		EnvPropertyValue copy = new EnvPropertyValue();
		copy.setPropertyKey(propertyKey);
		copy.setLabelofkey(labelofkey);
		copy.setValue(value);
		return copy;
	}
}
