package br.com.atlantico.iot.server_java.domain.platform;

public class contextResponses {

	private ContextElement contextElement;

	private StatusCode statusCode;

	public ContextElement getContextElement() {
		return contextElement;
	}

	public void setContextElement(ContextElement contextElement) {
		this.contextElement = contextElement;
	}

	public StatusCode getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(StatusCode statusCode) {
		this.statusCode = statusCode;
	}

}
