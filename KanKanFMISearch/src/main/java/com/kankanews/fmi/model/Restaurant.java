package com.kankanews.fmi.model;

public class Restaurant {
	private String id;
	private String xkzh;
	private String dwmc;
	private String jcrq;
	private String jcjg;
	private String dz;
	private String dwdz;

	public Restaurant(String id, String xkzh, String dwmc, String jcrq,
			String jcjg, String dz, String dwdz) {
		super();
		this.id = id;
		this.xkzh = xkzh;
		this.dwmc = dwmc;
		this.jcrq = jcrq;
		this.jcjg = jcjg;
		this.dz = dz;
		this.dwdz = dwdz;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getXkzh() {
		return xkzh;
	}

	public void setXkzh(String xkzh) {
		this.xkzh = xkzh;
	}

	public String getDwmc() {
		return dwmc;
	}

	public void setDwmc(String dwmc) {
		this.dwmc = dwmc;
	}

	public String getJcrq() {
		return jcrq;
	}

	public void setJcrq(String jcrq) {
		this.jcrq = jcrq;
	}

	public String getJcjg() {
		return jcjg;
	}

	public void setJcjg(String jcjg) {
		this.jcjg = jcjg;
	}

	public String getDz() {
		return dz;
	}

	public void setDz(String dz) {
		this.dz = dz;
	}

	public String getDwdz() {
		return dwdz;
	}

	public void setDwdz(String dwdz) {
		this.dwdz = dwdz;
	}

}
