package br.com.atlantico.iot.server_java.domain.platform;

import java.util.List;

public class ContextElement extends Entity {

	private List<Attribute> attributes;

	public List<Attribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<Attribute> attributes) {
		this.attributes = attributes;
	}
	
}
