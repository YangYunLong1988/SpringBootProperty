package com.snowstore.terra.vo;

public class SystemTree {
	private String id;
	private String text;
	private String icon;
	private State state = new State();
	private SystemTree[] children;
	private String li_attr;
	private String a_attr;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public SystemTree[] getChildren() {
		return children;
	}

	public void setChildren(SystemTree[] children) {
		this.children = children;
	}

	public String getLi_attr() {
		return li_attr;
	}

	public void setLi_attr(String li_attr) {
		this.li_attr = li_attr;
	}

	public String getA_attr() {
		return a_attr;
	}

	public void setA_attr(String a_attr) {
		this.a_attr = a_attr;
	}

}

class State {
	private boolean opened = false;
	private boolean disabled = false;
	private boolean selected = false;

	public boolean isOpened() {
		return opened;
	}

	public void setOpened(boolean opened) {
		this.opened = opened;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

}
