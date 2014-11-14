package es.optsicom.lib.web.webTest;

import java.util.Date;

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

public class ExperimentsRestControllerTests {
	private static final String EXPID = "1551";
	private static final String SUBSTRING_EXPERIMENT = "\"timeLimit\":0,\"maxTimeLimit\":0,\"problemBestMode\":null,\"problemName\":null,\"recordEvolution\":false,\"name\":\"paco\",\"useCase\":null,\"description\":null,\"numExecs\":0}";
	@Mock
	private ExperimentService experimentService;
	@Mock
	private ExperimentManager experimentManager;
	
	private ExperimentsRestController experimentsRestController;
	// {\"id\":0,\"researcher\":{\"name\":\"researcher\"},\"instances\":[],\"methods\":[],\"date\":1415303249959,\"computer\":{\"id\":0,\"name\":\"computer\",\"properties\":{\"name\":\"computer\",\"map\":{\"name\":\"computer\"},\"sortedProperties\":[{\"key\":\"name\",\"value\":\"computer\"}],\"propsAString\":\"{name=computer}\"}},\"timeLimit\":0,\"maxTimeLimit\":0,\"problemBestMode\":null,\"problemName\":null,\"recordEvolution\":false,\"name\":\"paco\",\"useCase\":null,\"description\":null,\"numExecs\":0}
	@Before
	public void experimentsRestControllerInit()
	{
		Date date = new Date();
		date.setTime(1000);
		Experiment experimento = new Experiment("paco", new Researcher("researcher"), date, new ComputerDescription("computer"));

		experimentService = mock(ExperimentService.class);
		experimentManager = mock(ExperimentManager.class);
		when(experimentService.findExperimentManagerById(1551)).thenReturn(experimentManager);
		when(experimentManager.getExperiment()).thenReturn(experimento);
		experimentsRestController = new ExperimentsRestController(experimentService);


	}
	
	
	@Test
	public void getExperimentById() throws Exception{

		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(experimentsRestController).build();
		String content = mockMvc.perform(MockMvcRequestBuilders.get("/api/" + EXPID)
		.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andReturn()
		.getResponse()
        .getContentAsString();
		
		assertTrue(content.contains(SUBSTRING_EXPERIMENT));
		verify(experimentService).findExperimentManagerById(any(long.class));
		
	}
	
}
	