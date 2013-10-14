package es.optsicom.lib.expresults.manager;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import es.optsicom.lib.expresults.model.ComputerDescription;
import es.optsicom.lib.expresults.model.DBProperties;
import es.optsicom.lib.expresults.model.DoubleEvent;
import es.optsicom.lib.expresults.model.Event;
import es.optsicom.lib.expresults.model.Execution;
import es.optsicom.lib.expresults.model.Experiment;
import es.optsicom.lib.expresults.model.InstanceDescription;
import es.optsicom.lib.expresults.model.MethodDescription;
import es.optsicom.lib.expresults.model.NonValueEvent;
import es.optsicom.lib.expresults.model.Researcher;
import es.optsicom.lib.util.BestMode;

public class JSONExperimentLoader {

	private Map<InstanceDescription, Map<MethodDescription, List<Execution>>> data;
	private File jsonFile;
	private Experiment experiment;
	private Map<MethodDescription, String> memMethodNames;
	private Map<String, MethodDescription> memMethodDescriptions;

	public static MemoryExperimentManager load(File jsonFile)
			throws IOException, ParseException {
		return new JSONExperimentLoader().internalLoad(jsonFile);
	}

	private MemoryExperimentManager internalLoad(File jsonFile) throws JsonParseException, JsonMappingException, IOException, ParseException {

		this.jsonFile = jsonFile;

		ObjectMapper om = new ObjectMapper();
		JsonNode rootNode = om.readValue(jsonFile, JsonNode.class);
		JsonNode exp = rootNode.path("experiment");
		
		loadExperiment(exp);
		
		loadMethods(exp.path("methods"));

		this.data = new HashMap<InstanceDescription, Map<MethodDescription,List<Execution>>>();
		
		JsonNode instanceListNode = exp.path("instance");
		Iterator<JsonNode> it = instanceListNode.getElements();
		while (it.hasNext()) {
			JsonNode instanceNode = it.next();

			Map<String, String> properties = getNodeProperties(instanceNode);

			InstanceDescription id = new InstanceDescription(new DBProperties(
					properties));

			Map<MethodDescription, List<Execution>> methodsToExecutions = loadExecutions(instanceNode, id);
			
			this.data.put(id, methodsToExecutions);
		}

		return new MemoryExperimentManager(experiment, data, memMethodNames);

	}

	private void loadMethods(JsonNode methodsNode) {
		
		this.memMethodNames = new HashMap<MethodDescription, String>();
		this.memMethodDescriptions = new HashMap<String, MethodDescription>();
		
		List<MethodDescription> methodList = new ArrayList<MethodDescription>();
		
		Iterator<JsonNode> it = methodsNode.getElements();
		while(it.hasNext()) {
			JsonNode methodNode = it.next();
			String methodName = methodNode.path("name").getValueAsText();
			
			Map<String, String> properties = getNodeProperties(methodNode);
			MethodDescription md = new MethodDescription(methodName);
			md.getProperties().getMap().putAll(properties);
			
			methodList.add(md);
			
			this.memMethodNames.put(md, methodName);
			this.memMethodDescriptions.put(methodName, md);

		}
		
		this.experiment.setMethods(methodList);
		
	}

	private void loadExperiment(JsonNode exp) throws ParseException {
		
		String name = exp.path("name").getValueAsText();

		Date date = DateFormat.getDateTimeInstance().parse(exp.path("date").getTextValue());
		
		Researcher researcher = new Researcher(exp.path("researcher").path("name").getValueAsText());
		
		ComputerDescription computerDescription = new ComputerDescription(exp.path("computer").path("name").getValueAsText());
		Map<String, String> properties = getNodeProperties(exp.path("computer").path("properties"));
		computerDescription.getProperties().getMap().putAll(properties);
				
		this.experiment = new Experiment(name, researcher, date, computerDescription);
		
		if(exp.path("bestMode").getValueAsText().equals("max")) {
			this.experiment.setProblemBestMode(BestMode.MAX_IS_BEST);
		} else if(exp.path("bestMode").getValueAsText().equals("min")) {
			this.experiment.setProblemBestMode(BestMode.MIN_IS_BEST);
		} else {
			throw new Error("Unknown best mode: " + exp.path("bestMode").getValueAsText());
		}
		
		this.experiment.setProblemName(exp.path("problemName").getValueAsText());
		
	}

	private Map<String, String> getNodeProperties(JsonNode instance) {
		Iterator<Entry<String, JsonNode>> propIt = instance.path("properties")
				.getFields();
		Map<String, String> properties = new HashMap<String, String>();
		while (propIt.hasNext()) {
			Entry<String, JsonNode> entry = propIt.next();
			properties.put(entry.getKey(), entry.getValue().getValueAsText());
		}
		return properties;
	}

	private Map<MethodDescription, List<Execution>> loadExecutions(
			JsonNode instanceNode, InstanceDescription instance) {

		JsonNode methodListNode = instanceNode.path("method");
		Iterator<JsonNode> it = methodListNode.getElements();
		Map<MethodDescription, List<Execution>> methods = new HashMap<MethodDescription, List<Execution>>();
		while (it.hasNext()) {
			JsonNode methodNode = it.next();
			String name = methodNode.path("name").getValueAsText();

			MethodDescription md = this.memMethodDescriptions.get(name);
			
			List<Execution> execs = loadExecutions(methodNode, md, instance);

			methods.put(md, execs);
		}

		return methods;
	}

	private List<Execution> loadExecutions(JsonNode methodNode,
			MethodDescription method, InstanceDescription instance) {

		List<Execution> execs = new ArrayList<Execution>();
		JsonNode executionList = methodNode.path("execution");
		Iterator<JsonNode> it = executionList.getElements();
		while (it.hasNext()) {
			JsonNode executionNode = it.next();

			Execution execution = new Execution(experiment, method, instance);
			execs.add(execution);
			loadEvents(execution, executionNode);

		}

		return execs;
	}

	private void loadEvents(Execution execution, JsonNode executionNode) {

		JsonNode eventList = executionNode.path("event");
		Iterator<JsonNode> it = eventList.getElements();
		while (it.hasNext()) {
			JsonNode eventNode = it.next();
			JsonNode eventNameNode = eventNode.path("eventName");
			if (eventNameNode.getValueAsText().equals("objValue")) {
				DoubleEvent e = new DoubleEvent(execution, eventNode.path(
						"timestamp").getValueAsLong(), eventNode.path(
						"eventName").getValueAsText(), eventNode.path("value")
						.getValueAsDouble());
				execution.addEvent(e);
			} else if (eventNameNode.getValueAsText().equals("finishTime")) {
				NonValueEvent e = new NonValueEvent(execution, eventNode.path(
						"timestamp").getValueAsLong(), eventNode.path(
						"eventName").getValueAsText());
				execution.addEvent(e);
			} else {
				throw new Error("Unexpected event type: "
						+ eventNameNode.getValueAsText());
			}
		}

	}

}
