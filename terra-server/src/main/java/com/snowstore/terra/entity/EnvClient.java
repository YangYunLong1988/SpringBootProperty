package com.snowstore.terra.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "TE_T_ENV_CLIENT")
@EntityListeners(AuditingEntityListener.class)
public class EnvClient extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	private String ip;

	private Date time = new Date();

	@ManyToOne
	private Env env;

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public Env getEnv() {
		return env;
	}

	public void setEnv(Env env) {
		this.env = env;
	}

}
