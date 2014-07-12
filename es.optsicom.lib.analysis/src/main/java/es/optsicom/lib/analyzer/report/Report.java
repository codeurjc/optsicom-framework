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
 * Class that represents the file to export
 */
public class Report implements Serializable {
	
	private static final long serialVersionUID = -7700219326245554039L;
	
	private List<ReportBlock> reportBlocks;
	
	/**
	 * Constructor of the calc file
	 * @param reportPage A sheet of the file
	 */
	public Report(ReportBlock reportBlock){
		this.reportBlocks = new ArrayList <ReportBlock>();
		this.reportBlocks.add(reportBlock);
	}
	
	/**
	 * Constructor of the calc file
	 * @param reportPages Set of sheets of the file
	 */
	public Report(ReportBlock[] blocks){
		this.reportBlocks = new ArrayList <ReportBlock>();
		for (int i=0; i<blocks.length; i++){
			this.reportBlocks.add(blocks[i]);
		}
	}
	
	public Report() {
		this.reportBlocks = new ArrayList<ReportBlock>();
	}

	/**
	 * Add a sheet to the file in the last position
	 * @param reportPage The sheet to add
	 * @throws ReportException
	 */
	public void addReportBlock(ReportBlock reportBlock) throws ReportException {
		
		for (int i=0; i<this.reportBlocks.size();i++){
			if (reportBlock.getName().equals(reportBlocks.get(i).getName())){
				reportBlock.setName(reportBlock.getName()+" (2)");
			}
		}
		this.reportBlocks.add(reportBlock);
	}
	
	/**
	 * Add a sheet to the file in a position
	 * @param sheet Sheet to add
	 * @param position Position where the sheet is added
	 * @throws ReportException
	 */
	public void addReportBlock(ReportBlock block, int position)throws ReportException{
		if ((position > this.reportBlocks.size()) || (position <0)){
			throw new ReportException ("The reportPage can not be inserted at that position");
		}
		this.reportBlocks.add(position,block);
	}
	
	/**
	 * Adds a set of pages at the end of the report in the order they are returned by the <code>pages</code> iterator.
	 * @param pages The set of pages to add
	 */
	public void addReportBlocks(List<ReportBlock> blocks) {
		this.reportBlocks.addAll(blocks);
	}
	

	/**
	 * Return the sheet corresponding to a position
	 * @param position The position of the sheet
	 * @return The sheet
	 * @throws ReportException
	 */
	public ReportBlock getReportBlock(int position) throws ReportException{
		if ((this.reportBlocks.size() < position) || (position<0)){
			throw new ReportException ("There is not reportPage in this position");
		}
		return this.reportBlocks.get(position);
	}
	
	/**
	 * @return The number of sheets of the file
	 */
	public int getNumReportBlocks(){
		return reportBlocks.size();
	}

	public List<ReportBlock> getReportBlocks() {
		return reportBlocks;
	}

	@Override
	public String toString() {
		return "Report [reportBlocks=" + reportBlocks + "]";
	}
}
