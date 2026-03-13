package com.flavfinder.APIdentity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the matchConfidence JSON object field.
 *
 * @author EmileM
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MatchConfidence{

	@JsonProperty("score")
	private int score;

	public void setScore(int score){
		this.score = score;
	}

	public int getScore(){
		return score;
	}

	@Override
 	public String toString(){
		return 
			"MatchConfidence{" + 
			"score = '" + score + '\'' + 
			"}";
		}
}