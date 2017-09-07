package br.com.atlantico.iot.server_java.domain.platform;

public class UnsubscribeRequest {

	private String subscriptionId;
	
	public UnsubscribeRequest() {	
	}
	
	public UnsubscribeRequest(String subscriptionId) {
		super();
		this.subscriptionId = subscriptionId;
	}

	public String getSubscriptionId() {
		return subscriptionId;
	}

	public void setSubscriptionId(String subscriptionId) {
		this.subscriptionId = subscriptionId;
	}
	
}
