package com.dlohaiti.dlokiosk;

public class Reading {
	
	private Measurements name;
	private String value;
	private MeasurementLocation samplingSite;
	
	public Reading(Measurements name, String value, MeasurementLocation samplingSite) {
		this.name = name;
		this.value = value;
		this.samplingSite = samplingSite;
	}
	
	public Measurements getName() {
		return name;
	}
	
	public String getValue() {
		return value;
	}
	
	public String getSamplingSite() {
		return samplingSite.name();
	}	

}
