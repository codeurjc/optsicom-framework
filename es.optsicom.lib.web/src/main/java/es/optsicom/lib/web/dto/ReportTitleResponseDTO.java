package es.optsicom.lib.web.dto;

import java.util.ArrayList;
import java.util.List;

public class ReportTitleResponseDTO {

	private List<ReportAttributeResponseDTO> attributes;
	private String infoTitle;

	public ReportTitleResponseDTO() {
		this.attributes = new ArrayList<ReportAttributeResponseDTO>();
		infoTitle = "";
	}

	public ReportTitleResponseDTO(List<ReportAttributeResponseDTO> attributes, String infoTitle) {
		this.infoTitle = infoTitle;
		this.attributes = attributes;
	}

	public String getInfoTitle() {
		return infoTitle;
	}

	public List<ReportAttributeResponseDTO> getAttributes() {
		return attributes;
	}

}
