package es.optsicom.lib.web.dto;

public class InstanceBasicResponseDTO {

	private long id;
	private String name;

	public InstanceBasicResponseDTO(long id, String name) {
		this.id = id;
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

}
