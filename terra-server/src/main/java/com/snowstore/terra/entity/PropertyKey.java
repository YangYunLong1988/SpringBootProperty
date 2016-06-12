package com.snowstore.terra.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "TE_T_PROPERTY_KEY")
@EntityListeners(AuditingEntityListener.class)
@BatchSize(size = 100)
public class PropertyKey extends AbstractEntity {

	private static final long serialVersionUID = 4466410002337977748L;
	private String propertyKey;
	@Column(length = 5000)
	private String defaultValue;
	private String memo;
	private String status;
	@ManyToOne
	private SystemVersion systemVersion;
	
	public enum Status {
		正常,已删除
	}
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPropertyKey() {
		return propertyKey;
	}

	public void setPropertyKey(String propertyKey) {
		this.propertyKey = propertyKey;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public SystemVersion getSystemVersion() {
		return systemVersion;
	}

	public void setSystemVersion(SystemVersion systemVersion) {
		this.systemVersion = systemVersion;
	}

	@Override
	public PropertyKey clone() {
		PropertyKey copy = new PropertyKey();
		copy.setDefaultValue(defaultValue);
		copy.setMemo(memo);
		copy.setPropertyKey(propertyKey);
		copy.setStatus(status);
		return copy;

	}

}
