package es.optsicom.lib.analyzer.report.table;

import java.util.ArrayList;
import java.util.List;

public class Leyend {

	List<LeyendElement> elements = new ArrayList<LeyendElement>();
	
	public void addLeyentElement(LeyendElement element){
		this.elements.add(element);
	}
	
	public List<LeyendElement> getLeyendElements(){
		return this.elements;
	}
}
