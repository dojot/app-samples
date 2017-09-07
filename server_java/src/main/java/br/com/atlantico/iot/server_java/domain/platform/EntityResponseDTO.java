package br.com.atlantico.iot.server_java.domain.platform;

import java.util.List;

public class EntityResponseDTO {

	private String subscriptionId;
	
	private String originator;
	
	private List<contextResponses> contextResponses;

	public String getSubscriptionId() {
		return subscriptionId;
	}

	public void setSubscriptionId(String subscriptionId) {
		this.subscriptionId = subscriptionId;
	}

	public String getOriginator() {
		return originator;
	}

	public void setOriginator(String originator) {
		this.originator = originator;
	}

	public List<contextResponses> getContextResponses() {
		return contextResponses;
	}

	public void setContextResponses(List<contextResponses> contextResponses) {
		this.contextResponses = contextResponses;
	}
	
}


