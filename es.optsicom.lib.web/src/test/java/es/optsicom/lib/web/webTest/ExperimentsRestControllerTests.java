package es.optsicom.lib.web.webTest;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.AssertTrue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.internal.matchers.Contains;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import es.optsicom.lib.expresults.manager.ExecutionMethodExperimentManager;
import es.optsicom.lib.expresults.manager.ExperimentManager;
import es.optsicom.lib.expresults.model.ComputerDescription;
import es.optsicom.lib.expresults.model.Experiment;
import es.optsicom.lib.expresults.model.Researcher;
import es.optsicom.lib.web.service.ExperimentService;
import es.optsicom.lib.web.web.ExperimentsRestController;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.util.HashMap; 

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ExperimentsRestControllerTests {
	private static final String EXPID = "1551";
	private static final long EXPIDLONG = 1551;
	private static final String SUBSTRING_EXPERIMENT = "\"timeLimit\":0,\"maxTimeLimit\":0,\"problemBestMode\":null,\"problemName\":null,\"recordEvolution\":false,\"name\":\"paco\",\"useCase\":null,\"description\":null,\"numExecs\":0}";
	@Mock
	private ExperimentService experimentService;
	@Mock
	private ExperimentManager experimentManager;
	
	private ExperimentsRestController experimentsRestController;
	
	private Experiment experimento;
	private Experiment experimento2;
	private Experiment experimento3;
	private List<Experiment> experimentList;
	
	private MockMvc mockMvc;
	private JsonFactory factory;
	private ObjectMapper mapper;
	// {\"id\":0,\"researcher\":{\"name\":\"researcher\"},\"instances\":[],\"methods\":[],\"date\":1415303249959,\"computer\":{\"id\":0,\"name\":\"computer\",\"properties\":{\"name\":\"computer\",\"map\":{\"name\":\"computer\"},\"sortedProperties\":[{\"key\":\"name\",\"value\":\"computer\"}],\"propsAString\":\"{name=computer}\"}},\"timeLimit\":0,\"maxTimeLimit\":0,\"problemBestMode\":null,\"problemName\":null,\"recordEvolution\":false,\"name\":\"paco\",\"useCase\":null,\"description\":null,\"numExecs\":0}
	
	
	//	[{"id":0,"researcher":{"name":"researcher"},"instances":[],"methods":[],"date":1000,"computer":{"id":0,"name":"computer","properties":{"sortedProperties":[{"key":"name","value":"computer"}],"propsAString":"{name=computer}","name":"computer","map":{"name":"computer"}}},"timeLimit":0,"maxTimeLimit":0,"problemBestMode":null,"problemName":null,"recordEvolution":false,"name":"paco","useCase":null,"description":null,"numExecs":0},{"id":0,"researcher":{"name":"researcher2"},"instances":[],"methods":[],"date":1000,"computer":{"id":0,"name":"computer2","properties":{"sortedProperties":[{"key":"name","value":"computer2"}],"propsAString":"{name=computer2}","name":"computer2","map":{"name":"computer2"}}},"timeLimit":0,"maxTimeLimit":0,"problemBestMode":null,"problemName":null,"recordEvolution":false,"name":"juan","useCase":null,"description":null,"numExecs":0}]

	@Before
	public void experimentsRestControllerInit(){
		Date date = new Date();
		date.setTime(1000);
		experimento = new Experiment("paco", new Researcher("researcher"), date, new ComputerDescription("computer"));
		experimento2 = new Experiment("juan", new Researcher("researcher2"), date, new ComputerDescription("computer2"));
		experimento3 = new Experiment("maria", new Researcher("researcher3"), date, new ComputerDescription("computer3"));
		experimentList = new ArrayList<Experiment>();
		experimentList.add(experimento);
		experimentList.add(experimento2);
		experimentList.add(experimento3);
		
		experimentService = mock(ExperimentService.class);
		experimentManager = mock(ExperimentManager.class);
		when(experimentService.findExperimentManagerById(EXPIDLONG)).thenReturn(experimentManager);
		when(experimentManager.getExperiment()).thenReturn(experimento);
		when(experimentService.findExperiments()).thenReturn(experimentList);
		experimentsRestController = new ExperimentsRestController(experimentService);

		mockMvc = MockMvcBuilders.standaloneSetup(
				experimentsRestController).build();
		factory = new JsonFactory();
		mapper = new ObjectMapper(factory);
	}

	@Test
	public void getExperimentById() throws Exception {
		String content = mockMvc
				.perform(MockMvcRequestBuilders.get("/api/" + EXPID).accept(
								MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn().getResponse()
				.getContentAsString();
		TypeReference<HashMap<String, Object>> typeRef = new TypeReference<HashMap<String, Object>>() {};
		HashMap<String, Object> o = mapper.readValue(new ByteArrayInputStream(
				content.getBytes("UTF-8")), typeRef);
		assertTrue(o.get("name").equals(experimento.getName()));
		verify(experimentService).findExperimentManagerById(any(long.class));
	}
	
	@Test
	public void getExperiments() throws Exception {
		String content = mockMvc
				.perform(
						MockMvcRequestBuilders.get("/api/").accept(
								MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn().getResponse()
				.getContentAsString();
		TypeReference<List<HashMap<String, Object>>> typeRef = new TypeReference<List<HashMap<String, Object>>>() {};
		List<HashMap<String, Object>> o = mapper.readValue(new ByteArrayInputStream(
				content.getBytes("UTF-8")), typeRef);

		assertTrue(o.get(0).get("name").equals(experimento.getName()));
		assertTrue(o.get(1).get("name").equals(experimento2.getName()));
		assertTrue(o.get(2).get("name").equals(experimento3.getName()));
		assertTrue(o.size() == 3);
		verify(experimentService).findExperiments();
	}
	@Transactional
	@Test
	public void deleteExperimentById() throws Exception { // integration test
		mockMvc.perform(MockMvcRequestBuilders.delete("/api/" + 10853).accept(
								MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
		verify(experimentService).removeExperiment(any(long.class));
	}
	
	@Test
	public void mergeExperiments() throws Exception { 
		mockMvc.perform(MockMvcRequestBuilders.get("/api/merge/"+ EXPIDLONG).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
		verify(experimentService).findExperimentManagerById(any(long.class));
	}
	
//	@Test
//	public void mergeExperiments() throws Exception { 
//		List<String> listring = new ArrayList<String>();
//		Gson gson = new Gson();
//		String json = gson.toJson(listring);
//		System.out.println("hola " + json); ////////////////////////////////////////////////
//		mockMvc.perform(MockMvcRequestBuilders.post("/api/merge/").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isOk());
//		verify(experimentService).findExperimentManagerById(any(long.class));
//	}

}
	