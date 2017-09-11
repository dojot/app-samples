package br.com.atlantico.iot.server_java.domain;

public class AttributeResponseDTO {

	private String nome;
	private String value;
	
	public AttributeResponseDTO() {
	
	}
		
	public AttributeResponseDTO(String nome, String value) {
		super();
		this.nome = nome;
		this.value = value;
	}
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	
	
}

