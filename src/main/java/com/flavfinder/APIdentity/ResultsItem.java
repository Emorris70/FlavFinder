package com.flavfinder.APIdentity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResultsItem{

	@JsonProperty("matchConfidence")
	private MatchConfidence matchConfidence;

	@JsonProperty("address")
	private Address address;

	@JsonProperty("position")
	private Position position;

	public void setMatchConfidence(MatchConfidence matchConfidence){
		this.matchConfidence = matchConfidence;
	}

	public MatchConfidence getMatchConfidence(){
		return matchConfidence;
	}

	public void setAddress(Address address){
		this.address = address;
	}

	public Address getAddress(){
		return address;
	}

	public void setPosition(Position position){
		this.position = position;
	}

	public Position getPosition(){
		return position;
	}

	@Override
 	public String toString(){
		return 
			"ResultsItem{" + 
			"matchConfidence = '" + matchConfidence + '\'' + 
			",address = '" + address + '\'' + 
			",position = '" + position + '\'' + 
			"}";
		}
}