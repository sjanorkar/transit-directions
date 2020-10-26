package com.zendesk.direction.entity;

import java.util.List;

/**
 * Entity class for transport directions
 * @author swapnil.janorkar
 *
 */
public class TravelPlan {

	private String error;
	
	private List<String> summary;
	
	private List<String> step;

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public List<String> getSummary() {
		return summary;
	}

	public void setSummary(List<String> summary) {
		this.summary = summary;
	}

	public List<String> getStep() {
		return step;
	}

	public void setStep(List<String> step) {
		this.step = step;
	}
}
