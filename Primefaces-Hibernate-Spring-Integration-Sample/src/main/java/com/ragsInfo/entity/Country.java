package com.ragsInfo.entity;
// Generated Dec 26, 2018 5:01:43 PM by Hibernate Tools 5.3.0.Beta2

import java.util.HashSet;
import java.util.Set;

/**
 * Country generated by hbm2java
 */
public class Country implements java.io.Serializable {

	private Integer countryId;
	private String country;
	private Set companies = new HashSet(0);
	private Set states = new HashSet(0);

	public Country() {
	}

	public Country(String country) {
		this.country = country;
	}

	public Country(String country, Set companies, Set states) {
		this.country = country;
		this.companies = companies;
		this.states = states;
	}

	public Integer getCountryId() {
		return this.countryId;
	}

	public void setCountryId(Integer countryId) {
		this.countryId = countryId;
	}

	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Set getCompanies() {
		return this.companies;
	}

	public void setCompanies(Set companies) {
		this.companies = companies;
	}

	public Set getStates() {
		return this.states;
	}

	public void setStates(Set states) {
		this.states = states;
	}

}
