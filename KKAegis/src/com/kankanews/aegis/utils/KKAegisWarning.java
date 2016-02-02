package com.kankanews.aegis.utils;

import java.util.Set;

public class KKAegisWarning {
	private int size;
	@SuppressWarnings("rawtypes")
	private Set sensitive;

	public int getSize() {
		return size;
	}

	public Set getSensitive() {
		return sensitive;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public void setSensitive(@SuppressWarnings("rawtypes") Set sensitive) {
		this.sensitive = sensitive;
	}
}
