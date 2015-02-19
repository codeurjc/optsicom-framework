package es.optsicom.lib.web.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.apache.poi.ss.usermodel.Workbook;

import es.optsicom.lib.analyzer.report.Report;
import es.optsicom.lib.analyzer.report.ReportBlock;
import es.optsicom.lib.analyzer.report.ReportElement;
import es.optsicom.lib.analyzer.report.ReportPage;
import es.optsicom.lib.analyzer.report.export.ExcelReportManager;
import es.optsicom.lib.analyzer.report.table.Table;
import es.optsicom.lib.analyzer.report.table.Title;
import es.optsicom.lib.analyzer.tablecreator.atttable.Attribute;
import es.optsicom.lib.analyzer.tool.FusionerReportCreator;
import es.optsicom.lib.analyzer.tool.FusionerReportCreator.ExperimentMethodConf;
import es.optsicom.lib.expresults.manager.ExperimentManager;
import es.optsicom.lib.expresults.model.Experiment;
import es.optsicom.lib.expresults.model.MethodDescription;
import es.optsicom.lib.web.model.ReportConfiguration;
import es.optsicom.lib.web.model.ReportTable;
import es.optsicom.lib.web.model.ReportRest;
import es.optsicom.lib.web.service.ExperimentService;

@Controller
@RequestMapping("/api")
public class ExperimentsRestController {
	private ExperimentService experimentService;
	private static final Log LOG = LogFactory.getLog(ExperimentsRestController.class);
	
	@Autowired
	public ExperimentsRestController(ExperimentService experimentservice){
		this.experimentService = experimentservice;
	}

//	@RequestMapping("/login")
//	public String showLogin() {
//		return "login";
//	}

	public long convertStringToLong(String string){
		if ( (string != null) && (string != "") ){
			return Long.valueOf(string).longValue();
		}
		return 0;
		
	}
	// http://localhost:8080/api/merge/1551,2401
	@RequestMapping(value = "/merge/{expIds}", method = RequestMethod.GET, produces = {"application/json" })
	public @ResponseBody List<Experiment> merge(@PathVariable("expIds") final List<String> expIds) {
		LOG.info("Merging experiments (/merge) : ");
		List<Experiment> lista = new ArrayList<Experiment>();
		for (int i = 0;i<expIds.size();i++){
			long expId = convertStringToLong(expIds.get(i));
			LOG.info(expId);
			lista.add(this.experimentService.findExperimentManagerById(expId).getExperiment() );	
		}
		return lista;
	}
	

	
	@RequestMapping(value = "/{expId}", method = RequestMethod.GET, produces = {"application/json" })
	public @ResponseBody Experiment getExperimentById(@PathVariable String expId){
		LOG.info("Recovering experiment: " + expId);
		return this.experimentService.findExperimentManagerById(convertStringToLong(expId)).getExperiment();	
	}

	@RequestMapping(value = "/", method = RequestMethod.GET, produces = {"application/json" })
	public @ResponseBody List<Experiment> getExperiments(){
		LOG.info("Recovering experiments (/)");
		return this.experimentService.findExperiments();
	}	

	@RequestMapping(value = "/experiments", method = RequestMethod.GET, produces = {"application/json" })
	public @ResponseBody List<Experiment> getExperimentsExplicit(){
		LOG.info("Recovering experiments (/experiments)");
		return this.experimentService.findExperiments();
	}	
	
	@RequestMapping(value = "/{expId}", method = RequestMethod.DELETE, produces = {"application/json" })
	public @ResponseBody void deleteExperimentById(@PathVariable String expId){
		LOG.info("Removing experiment: " + expId);
		experimentService.removeExperiment(convertStringToLong(expId));
	}
	
	@RequestMapping(value = "/{expId}/report", method = RequestMethod.POST, produces = {"application/json" })
	public @ResponseBody ReportRest report(@PathVariable String expId,@RequestBody final ReportConfiguration reportConfiguration){
		LOG.info("Report: " + expId);
		
		Long expIdLong = convertStringToLong(expId);
		Experiment experiment = this.experimentService.findExperimentManagerById(expIdLong).getExperiment();
		FusionerReportCreator reportCreator = new FusionerReportCreator(
				experiment.getProblemName(), "",
				experimentService.getDBManager());
		ExperimentManager expManager = this.experimentService
				.findExperimentManagerById(expIdLong);
		
		if (!reportConfiguration.isConfiguration()) {
			reportConfiguration.setMethods( new ArrayList<Long>());
				for (MethodDescription method : expManager.getMethods()) {
					String experimentMethodName = expManager.getExperimentMethodName(method);
					reportCreator.addExperimentMethod(expIdLong, experimentMethodName);
					reportConfiguration.addMethod(method.getId());			
				}
		} else {
				for (MethodDescription method : expManager.getMethods()) {
					String experimentMethodName = expManager.getExperimentMethodName(method);
					if((reportConfiguration.getMethods()).contains(method.getId())){
						reportCreator.addExperimentMethod(expIdLong,
								experimentMethodName);
					}
				}
		}
		if (reportConfiguration.isBestValues()) {
			reportCreator.addExperimentMethods(Arrays
					.asList(new ExperimentMethodConf("predefined",
							"best_values")));
		}
		Report report = reportCreator.createReportObject();
		
//		for (ReportBlock reportblock:report.getReportBlocks()){
//			for (ReportPage reportpage:reportblock.getReportPages()){
//				for (ReportElement reportElement:reportpage.getReportElements()){
//					Table table = (Table) reportElement;
//					for (Title rowTitle: table.getRowTitles()){
//						for (Attribute attribute: rowTitle.getAttributes()){
//							attribute.getTitle();
//							System.out.println(attribute.getValue());
//						}
//					}
//					for (Title columnTitle: table.getColumnTitles()){
//						for (Attribute attribute: columnTitle.getAttributes()){
//							attribute.getTitle();
//							System.out.println(attribute.getValue());
//						}
//					}
//				}
//			}
//		}
		
		List<ReportTable> rTables = new ArrayList<ReportTable>();
		for (ReportBlock reportblock:report.getReportBlocks()){
			for (ReportPage reportpage:reportblock.getReportPages()){
				for (ReportElement reportElement:reportpage.getReportElements()){
					Table table = (Table) reportElement;
					rTables.add( new ReportTable(table) );
					
//					for (Title rowTitle: table.getRowTitles()){
//						for (Attribute attribute: rowTitle.getAttributes()){
//							attribute.getTitle();
//							System.out.println(attribute.getValue());
//						}
//					}
//					for (Title columnTitle: table.getColumnTitles()){
//						for (Attribute attribute: columnTitle.getAttributes()){
//							attribute.getTitle();
//							System.out.println(attribute.getValue());
//						}
//					}
				}
			}
		}
		
//		ExcelReportManager excelreportManager = new ExcelReportManager();
		//sacar la parte de creaar el workbook sin generar el excel en otro metodo y llamarlo, con el, generar un objeto que sea la tabla para renderizala en json
		// generateExcelFile
//		Workbook wb = excelreportManager.generateWorkbook(report);
//		System.out.println(wb.toString()); 
		ReportRest rWeb = new ReportRest(reportConfiguration,rTables);
		LOG.info("Report created");
		return rWeb;	
	}
	
}
