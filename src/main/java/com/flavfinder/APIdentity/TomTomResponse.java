package com.flavfinder.APIdentity;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the root
 * @author EmileM
 */
public class TomTomResponse{

	@JsonProperty("results")
	private List<ResultsItem> results;

	public void setResults(List<ResultsItem> results){
		this.results = results;
	}

	public List<ResultsItem> getResults(){
		return results;
	}

	@Override
 	public String toString(){
		return 
			"TomTomResponse{" + 
			"results = '" + results + '\'' + 
			"}";
		}
}