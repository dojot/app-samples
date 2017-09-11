package br.com.atlantico.iot.server_java.domain.platform;

import java.util.ArrayList;
import java.util.List;

public class EntityRequestDTO {

	private List<Entity> entities = new ArrayList<Entity>();

	private List<String> attributes;

	private String reference;

	private String duration;

	private List<NotifyConditions> notifyConditions = new ArrayList<NotifyConditions>();
	
	public EntityRequestDTO() {
		this.duration = "P1Y";
	}

	public List<Entity> getEntities() {	
		return entities;
	}	

	public void setEntities(List<Entity> entities) {
		this.entities = entities;
	}
	
	public void addEntity(Entity entity) {
		if(entities.isEmpty()) {
			entities = new ArrayList<Entity>();
		}
		entities.add(entity);
	}

	public List<String> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<String> attributes) {
		this.attributes = attributes;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public List<NotifyConditions> getNotifyConditions() {
		return notifyConditions;
	}

	public void setNotifyConditions(List<NotifyConditions> notifyConditions) {
		this.notifyConditions = notifyConditions;
	}
	
	public void addNotifyCondition(NotifyConditions notifyCondition) {
		if(notifyConditions.isEmpty()) {
			notifyConditions = new ArrayList<NotifyConditions>();
		}
		notifyConditions.add(notifyCondition);
	}

}
