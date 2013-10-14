package es.optsicom.lib.expresults.dpef;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig.Feature;
import org.codehaus.jackson.map.module.SimpleModule;
import org.codehaus.jackson.map.util.StdDateFormat;

import es.optsicom.lib.expresults.manager.ExperimentManager;
import es.optsicom.lib.expresults.manager.MemoryExperimentManager;
import es.optsicom.lib.expresults.model.ElementDescription;
import es.optsicom.lib.expresults.model.Execution;
import es.optsicom.lib.expresults.model.Experiment;
import es.optsicom.lib.expresults.model.InstanceDescription;
import es.optsicom.lib.expresults.model.MethodDescription;

public class DPEFToExperimentManager {

	private ObjectMapper mapper;
	
	public DPEFToExperimentManager() {	
		
		this.mapper = new ObjectMapper();
		this.mapper.configure(Feature.INDENT_OUTPUT, true);
		
		
        this.mapper.getDeserializationConfig().setDateFormat(StdDateFormat.getBlueprintISO8601Format());
        this.mapper.getSerializationConfig().setDateFormat(StdDateFormat.getBlueprintISO8601Format());
		
		
		//SimpleModule elementDescModule = new SimpleModule("ElementDescriptionModule", new Version(1, 0, 0, null));
		//elementDescModule.addDeserializer(ElementDescription.class, new ElementDescriptionDeserializer()); // assuming serializer declares correct class to bind to
		//mapper.registerModule(elementDescModule);
		
	}

	public ExperimentManager createExperimentManager(File dpefFile) throws JsonParseException, JsonMappingException, IOException {
		
		WholeExperimentToExport wExperiment = mapper.readValue(dpefFile, WholeExperimentToExport.class);
		
		return new MemoryExperimentManager(wExperiment.getExperiment(), wExperiment.getExecutions(), wExperiment.getExpMethodNames());
	}

	
}
