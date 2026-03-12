package com.flavfinder.APIdentity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Address{

	@JsonProperty("streetName")
	private String streetName;

	@JsonProperty("countrySubdivisionName")
	private String countrySubdivisionName;

	@JsonProperty("streetNumber")
	private String streetNumber;

	@JsonProperty("countryCode")
	private String countryCode;

	@JsonProperty("postalCode")
	private String postalCode;

	@JsonProperty("municipality")
	private String municipality;

	@JsonProperty("countrySubdivisionCode")
	private String countrySubdivisionCode;

	public void setStreetName(String streetName){
		this.streetName = streetName;
	}

	public String getStreetName(){
		return streetName;
	}

	public void setCountrySubdivisionName(String countrySubdivisionName){
		this.countrySubdivisionName = countrySubdivisionName;
	}

	public String getCountrySubdivisionName(){
		return countrySubdivisionName;
	}

	public void setStreetNumber(String streetNumber){
		this.streetNumber = streetNumber;
	}

	public String getStreetNumber(){
		return streetNumber;
	}

	public void setCountryCode(String countryCode){
		this.countryCode = countryCode;
	}

	public String getCountryCode(){
		return countryCode;
	}

	public void setPostalCode(String postalCode){
		this.postalCode = postalCode;
	}

	public String getPostalCode(){
		return postalCode;
	}

	public void setMunicipality(String municipality){
		this.municipality = municipality;
	}

	public String getMunicipality(){
		return municipality;
	}

	public void setCountrySubdivisionCode(String countrySubdivisionCode){
		this.countrySubdivisionCode = countrySubdivisionCode;
	}

	public String getCountrySubdivisionCode(){
		return countrySubdivisionCode;
	}

	@Override
 	public String toString(){
		return 
			"Address{" + 
			"streetName = '" + streetName + '\'' + 
			",countrySubdivisionName = '" + countrySubdivisionName + '\'' + 
			",streetNumber = '" + streetNumber + '\'' + 
			",countryCode = '" + countryCode + '\'' + 
			",postalCode = '" + postalCode + '\'' + 
			",municipality = '" + municipality + '\'' + 
			",countrySubdivisionCode = '" + countrySubdivisionCode + '\'' + 
			"}";
		}
}