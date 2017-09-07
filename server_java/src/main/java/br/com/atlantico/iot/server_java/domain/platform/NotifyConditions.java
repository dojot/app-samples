package br.com.atlantico.iot.server_java.domain.platform;

import java.util.List;

public class NotifyConditions {

	private String type;
	private List<String> condValues;
	
	public NotifyConditions() {
		this.type = "ONCHANGE";
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<String> getCondValues() {
		return condValues;
	}

	public void setCondValues(List<String> condValues) {
		this.condValues = condValues;
	}

}
