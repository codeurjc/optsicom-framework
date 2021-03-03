package es.optsicom.lib.web.dto;

public class InstanceExtendResponseDTO {

	private long id;
	private String name;
	private String fileName;
	private String instanceId;
	private String useCase;

	public InstanceExtendResponseDTO(long id, String name, String fileName, String instanceId, String useCase) {
		this.id = id;
		this.name = name;
		this.fileName = fileName;
		this.instanceId = instanceId;
		this.useCase = useCase;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getFileName() {
		return fileName;
	}

	public String getInstanceId() {
		return instanceId;
	}

	public String getUseCase() {
		return useCase;
	}

}
