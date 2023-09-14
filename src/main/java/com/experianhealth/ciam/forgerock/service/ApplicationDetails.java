package com.experianhealth.ciam.forgerock.service;

public class ApplicationDetails {
    private String appId;
    public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getAppDescription() {
		return appDescription;
	}
	public void setAppDescription(String appDescription) {
		this.appDescription = appDescription;
	}
	private String appName;
    private String appDescription;
    // Other fields, getters, setters, and constructors
}
