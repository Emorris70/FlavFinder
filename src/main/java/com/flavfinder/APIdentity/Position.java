package com.flavfinder.APIdentity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the position JSON object field.
 *
 * @author EmileM
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Position{

	@JsonProperty("lon")
	private Object lon;

	@JsonProperty("lat")
	private Object lat;

	public void setLon(Object lon){
		this.lon = lon;
	}

	public Object getLon(){
		return lon;
	}

	public void setLat(Object lat){
		this.lat = lat;
	}

	public Object getLat(){
		return lat;
	}

	@Override
 	public String toString(){
		return 
			"Position{" + 
			"lon = '" + lon + '\'' + 
			",lat = '" + lat + '\'' + 
			"}";
		}
}