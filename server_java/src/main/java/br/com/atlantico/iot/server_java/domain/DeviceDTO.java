package br.com.atlantico.iot.server_java.domain;

import java.util.List;

public class DeviceDTO {

	private String id;
	
	private List<String> attributes;
	
	public DeviceDTO() {}

	public DeviceDTO(String id, List<String> attributes) {
		super();
		this.id = id;
		this.attributes = attributes;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<String> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<String> attributes) {
		this.attributes = attributes;
	}
	
	

	
}
