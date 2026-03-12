package com.flavfinder.APIdentity;

import com.fasterxml.jackson.annotation.JsonProperty;

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