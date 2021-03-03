package es.optsicom.lib.web.dto;

import java.awt.Color;

import es.optsicom.lib.analyzer.report.table.CellFormat;

public class ReportCellResponseDTO {

	private Object value;
	private CellFormat format;
	private String color;

	public ReportCellResponseDTO() {
	}

	public ReportCellResponseDTO(Object value, CellFormat format, String color) {
		this.value = value;
		this.format = format;
		this.color = color;
	}

	public ReportCellResponseDTO(Object value, CellFormat format, Color color) {
		this.value = value;
		this.format = format;
		this.color = format(color);
	}

	public Object getValue() {
		return value;
	}

	public CellFormat getFormat() {
		return format;
	}

	public String getColor() {
		return color;
	}

	public static final String format(Color c) {

		if (c == null) {
			return "null";
		}

		String r = (c.getRed() < 16) ? "0" + Integer.toHexString(c.getRed()) : Integer.toHexString(c.getRed());
		String g = (c.getGreen() < 16) ? "0" + Integer.toHexString(c.getGreen()) : Integer.toHexString(c.getGreen());
		String b = (c.getBlue() < 16) ? "0" + Integer.toHexString(c.getBlue()) : Integer.toHexString(c.getBlue());

		return "#" + r + g + b;
	}
}
