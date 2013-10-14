package es.optsicom.lib.analyzer.report.table;

import java.util.ArrayList;
import java.util.List;

import es.optsicom.lib.analyzer.tablecreator.atttable.Attribute;

/**
 * Esta clase se usa para representar los títulos de una TitleTable. Estos títulos tienen la característica
 * de que pueden ser compuestos, permitiendo de esta forma representar tablas complejas en las que cada valor
 * de columna puede estar atribuido a varios aspectos. (Algoritmo y estadístico, por ejemplo). 
 * @author Administrador
 *
 */
public class Title {

	private List<Attribute> attributes = new ArrayList<Attribute>();

	public Title() {
		this("");
	}
	
	public Title(String value) {
		super();
		this.attributes = new ArrayList<Attribute>();
		this.attributes.add(new Attribute("title", value));
	}
	
	public Title(List<Attribute> attributes) {
		super();
		this.attributes = attributes;
	}
	
	public List<Attribute> getAttributes() {
		return attributes;
	}
	
	@Override
	public String toString() {
		return attributes.toString();
	}

	public String getTitle() {
		return attributes.get(0).getTitle();
	}
}
