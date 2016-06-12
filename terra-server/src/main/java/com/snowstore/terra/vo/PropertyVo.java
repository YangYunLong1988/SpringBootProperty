package com.snowstore.terra.vo;

import java.util.Date;

import org.apache.commons.lang.StringEscapeUtils;

import com.fasterxml.jackson.annotation.JsonFormat;

public class PropertyVo {
	private Long envPropertyValueId;
	private String key;
	private String value;
	private String memo;
	private String labelofkey;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date updateTime;

	public Long getEnvPropertyValueId() {
		return envPropertyValueId;
	}

	public void setEnvPropertyValueId(Long envPropertyValueId) {
		this.envPropertyValueId = envPropertyValueId;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getLabelofkey() {
		return labelofkey;
	}

	public void setLabelofkey(String labelofkey) {
		this.labelofkey = labelofkey;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getShortValue() {
		if (value.length() > 80) {
			return StringEscapeUtils.escapeHtml(value.substring(0, 80)) + "...";
		}
		return StringEscapeUtils.escapeHtml(value);
	}

	public String getEscapedValue() {
		return StringEscapeUtils.escapeHtml(value);
	}
}