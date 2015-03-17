package es.optsicom.lib.web.webTest;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import es.optsicom.lib.expresults.manager.ExperimentManager;
import es.optsicom.lib.expresults.model.ComputerDescription;
import es.optsicom.lib.expresults.model.Experiment;
import es.optsicom.lib.expresults.model.Researcher;
import es.optsicom.lib.web.service.ExperimentService;
import es.optsicom.lib.web.web.ExperimentsRestController;

public class ExperimentsRestControllerTests {
	private static final String EXPID = "1551";
	private static final long EXPIDLONG = 1551;
	@Mock
	private ExperimentService experimentService;
	@Mock
	private ExperimentManager mainExperimentManager;
	@Mock
	private ExperimentManager secondaryExperimentManager;
	private ExperimentsRestController experimentsRestController;
	private Experiment experimentPaco;
	private Experiment experimentJuan;
	private Experiment experimentMaria;
	private List<Experiment> experimentList;
	private MockMvc mockMvc;
	private JsonFactory factory;
	private ObjectMapper mapper;

	@Before
	public void experimentsRestControllerInit(){
		Date date = new Date();
		date.setTime(1000);
		experimentPaco = new Experiment("paco", new Researcher("researcher"), date, new ComputerDescription("computer"));
		experimentJuan = new Experiment("juan", new Researcher("researcher2"), date, new ComputerDescription("computer2"));
		experimentMaria = new Experiment("maria", new Researcher("researcher3"), date, new ComputerDescription("computer3"));
		experimentList = new ArrayList<Experiment>();
		experimentList.add(experimentPaco);
		experimentList.add(experimentJuan);
		experimentList.add(experimentMaria);
		
		experimentService = mock(ExperimentService.class);
		mainExperimentManager = mock(ExperimentManager.class);
		secondaryExperimentManager = mock(ExperimentManager.class);
		when(experimentService.findExperimentManagerById(EXPIDLONG)).thenReturn(mainExperimentManager).thenReturn(secondaryExperimentManager);
		when(mainExperimentManager.getExperiment()).thenReturn(experimentPaco);
		when(secondaryExperimentManager.getExperiment()).thenReturn(experimentMaria);
		when(experimentService.findExperiments()).thenReturn(experimentList);
		
		experimentsRestController = new ExperimentsRestController(experimentService);
		mockMvc = MockMvcBuilders.standaloneSetup(
				experimentsRestController).build();
		factory = new JsonFactory();
		mapper = new ObjectMapper(factory);
	}

	@Test
	public void stringShouldNotBeNullInConvertStringToLong(){
		assertTrue(experimentsRestController.convertStringToLong(null) == 0);
	}
	
	@Test
	public void stringShouldNotBeEmptyStringInConvertStringToLong(){
		assertTrue(experimentsRestController.convertStringToLong("") == 0);
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
		assertTrue(o.get("name").equals(experimentPaco.getName()));
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

		assertTrue(o.get(0).get("name").equals(experimentPaco.getName()));
		assertTrue(o.get(1).get("name").equals(experimentJuan.getName()));
		assertTrue(o.get(2).get("name").equals(experimentMaria.getName()));
		assertTrue(o.size() == 3);
		verify(experimentService).findExperiments();
	}
	
	@Test
	public void getExperimentsExplicit() throws Exception {
		String content = mockMvc
				.perform(
						MockMvcRequestBuilders.get("/api/experiments").accept(
								MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn().getResponse()
				.getContentAsString();
		TypeReference<List<HashMap<String, Object>>> typeRef = new TypeReference<List<HashMap<String, Object>>>() {};
		List<HashMap<String, Object>> o = mapper.readValue(new ByteArrayInputStream(
				content.getBytes("UTF-8")), typeRef);

		assertTrue(o.get(0).get("name").equals(experimentPaco.getName()));
		assertTrue(o.get(1).get("name").equals(experimentJuan.getName()));
		assertTrue(o.get(2).get("name").equals(experimentMaria.getName()));
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
		mockMvc.perform(MockMvcRequestBuilders.get("/api/merge/"+ EXPIDLONG,EXPIDLONG).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
		verify(experimentService).findExperimentManagerById(any(long.class));
	}
}
	