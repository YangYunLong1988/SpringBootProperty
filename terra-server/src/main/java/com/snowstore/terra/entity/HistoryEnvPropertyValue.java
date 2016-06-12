package com.snowstore.terra.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;

import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "TE_T_ENV_PROPERTY_VALUE_HISTORY")
@EntityListeners(AuditingEntityListener.class)

public class HistoryEnvPropertyValue extends AbstractEntity {

	private static final long serialVersionUID = -3101369402947242803L;
	@Column(length = 5000)
	private String historykey;
	@Column(length = 5000)
	private String previousvalue;
	@Column(length = 5000)
	private String historyvalue;
	@Column(length = 5000)
	private String status;//only RUD
	@Column(length = 5000)
	private String comments;
	
	@Column(length = 5000)
	private String envId;

	public String getHistorykey() {
		return historykey;
	}

	public void setHistorykey(String historykey) {
		this.historykey = historykey;
	}

	public String getPreviousvalue() {
		return previousvalue;
	}

	public void setPreviousvalue(String previousvalue) {
		this.previousvalue = previousvalue;
	}

	public String getHistoryvalue() {
		return historyvalue;
	}

	public void setHistoryvalue(String historyvalue) {
		this.historyvalue = historyvalue;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getEnvId() {
		return envId;
	}

	public void setEnvId(String envId) {
		this.envId = envId;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	



}
