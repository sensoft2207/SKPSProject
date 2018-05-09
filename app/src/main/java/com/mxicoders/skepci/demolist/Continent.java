package com.mxicoders.skepci.demolist;

import java.util.ArrayList;

public class Continent {

	private String name;
	private ArrayList<Country> countryList = new ArrayList<Country>();

	public Continent(String name, ArrayList<Country> countryList) {
		super();
		this.name = name;
		this.countryList = countryList;
	}

	public Continent(ArrayList<Country> countryList2, ArrayList<Country> countryList) {

		this.countryList = countryList;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<Country> getCountryList() {
		return countryList;
	}

	public void setCountryList(ArrayList<Country> countryList) {
		this.countryList = countryList;
	}

	
}
