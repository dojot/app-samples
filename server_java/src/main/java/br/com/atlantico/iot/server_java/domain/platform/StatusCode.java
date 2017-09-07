package br.com.atlantico.iot.server_java.domain.platform;

public class StatusCode {

	private String code;
	
    private String reasonPhrase;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getReasonPhrase() {
		return reasonPhrase;
	}

	public void setReasonPhrase(String reasonPhrase) {
		this.reasonPhrase = reasonPhrase;
	}    
    
}
