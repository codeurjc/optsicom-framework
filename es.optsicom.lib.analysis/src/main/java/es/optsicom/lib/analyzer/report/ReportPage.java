/**
 * Package that includes the other packages of the Carmar project and classes that refer 
 * to the spreadsheet in memory
 */
package es.optsicom.lib.analyzer.report;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * @author paco
 * Class that represents a sheet of a spreadsheet
 */
public class ReportPage implements Serializable {
	
	private static final long serialVersionUID = -8531588720508295879L;

	private List<ReportElement> elements = new ArrayList<ReportElement>();
				
	/** Name of the sheet */
	private String name;
	
	/**
	 * Constructor of the sheet
	 * @param name Name of the sheet
	 */
	public ReportPage(String name){
		this.name = name;
	}
	
	public ReportPage(String name, ReportElement element){
		this.name = name;
		this.elements.add(element);
	}
	
	/**
	 * @param name the nameSheet to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the nameSheet
	 */
	public String getName() {
		return name;
	}
	
	public void addReportElement(ReportElement reportElement){
		this.elements.add(reportElement);
	}
	
	public int getNumReportElements(){
		return this.elements.size();
	}
	
	public List<ReportElement> getReportElements(){
		return this.elements;
	}

	public void addReportElements(List<? extends ReportElement> elements) {
		this.elements.addAll(elements);		
	}
}
