package br.com.atlantico.iot.server_java.domain.platform;

public class Entity {

	private String type;

	private String isPattern;

	private String id;

	public Entity() {
		this.type = "device";
		this.isPattern = "false";
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}	

	public String getIsPattern() {
		return isPattern;
	}

	public void setIsPattern(String isPattern) {
		this.isPattern = isPattern;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
