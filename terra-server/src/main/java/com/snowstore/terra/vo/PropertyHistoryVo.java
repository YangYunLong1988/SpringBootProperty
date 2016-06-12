package com.snowstore.terra.vo;



public class PropertyHistoryVo {
	private String modifiedby;
	private String key;
	private String previousvalue;
	private String value;
	private String action;
	private String comments;
	private String userupdateTime;
	
	public String getModifiedby() {
		return modifiedby;
	}
	public void setModifiedby(String modifiedby) {
		this.modifiedby = modifiedby;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}	
	public String getPreviousvalue() {
		return previousvalue;
	}
	public void setPreviousvalue(String previousvalue) {
		this.previousvalue = previousvalue;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	
	public String getUserupdateTime() {
		return userupdateTime;
	}
	public void setUserupdateTime(String userupdateTime) {
		this.userupdateTime = userupdateTime;
	}
	
	

	

	
	
}
