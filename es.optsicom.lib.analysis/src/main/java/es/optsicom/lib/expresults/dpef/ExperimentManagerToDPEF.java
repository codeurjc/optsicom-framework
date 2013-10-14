package es.optsicom.lib.expresults.dpef;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig.Feature;
import org.codehaus.jackson.map.module.SimpleModule;
import org.codehaus.jackson.map.util.StdDateFormat;

import es.optsicom.lib.expresults.manager.ExecutionManager;
import es.optsicom.lib.expresults.manager.ExperimentManager;
import es.optsicom.lib.expresults.manager.ExperimentRepositoryManager;
import es.optsicom.lib.expresults.model.Event;
import es.optsicom.lib.expresults.model.Execution;
import es.optsicom.lib.expresults.model.Experiment;
import es.optsicom.lib.expresults.model.InstanceDescription;
import es.optsicom.lib.expresults.model.MethodDescription;

/**
 * This class is used to write experiment information in DPEF. DPEF stands for
 * Durable and Plain Experiment Format.
 * 
 * @author Micael Gallego
 * 
 */
public class ExperimentManagerToDPEF {

	private ObjectMapper mapper;

	public ExperimentManagerToDPEF() {
		
		this.mapper = new ObjectMapper();
		this.mapper.configure(Feature.INDENT_OUTPUT, true);		
		
        mapper.getDeserializationConfig().setDateFormat(StdDateFormat.getBlueprintISO8601Format());
        mapper.getSerializationConfig().setDateFormat(StdDateFormat.getBlueprintISO8601Format());		
		
		//SimpleModule elementDescModule = new SimpleModule("ElementDescriptionModule", new Version(1, 0, 0, null));
		//elementDescModule.addSerializer(new ElementDescriptionSerializer()); // assuming serializer declares correct class to bind to
		//mapper.registerModule(elementDescModule);

	}

	public void createDPEF(ExperimentManager expManager, File resultFile) throws IOException {

		WholeExperimentToExport wExp = new WholeExperimentToExport(expManager.getExperiment(), expManager.createExecutions(), expManager.createExperimentMethodNames());
		
		OutputStream out = new FileOutputStream(resultFile);		
		mapper.writeValue(out, wExp);

	}	

}
