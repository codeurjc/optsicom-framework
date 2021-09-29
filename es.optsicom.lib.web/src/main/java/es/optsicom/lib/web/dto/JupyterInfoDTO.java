package es.optsicom.lib.web.dto;

public class JupyterInfoDTO {
	private String url;
	private String[] templates;
	
	public JupyterInfoDTO(String url, String[] templates) {
		this.url = url;
		this.setTemplates(templates);
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String[] getTemplates() {
		return templates;
	}

	public void setTemplates(String[] templates) {
		this.templates = templates;
	}
	
	
}
