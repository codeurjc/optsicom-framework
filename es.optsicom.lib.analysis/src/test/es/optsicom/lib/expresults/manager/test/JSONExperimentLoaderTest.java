package es.optsicom.lib.expresults.manager.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import es.optsicom.lib.expresults.manager.JSONExperimentLoader;
import es.optsicom.lib.expresults.manager.MemoryExperimentManager;
import es.optsicom.lib.expresults.model.ComputerDescription;
import es.optsicom.lib.expresults.model.DBProperties;
import es.optsicom.lib.expresults.model.Experiment;
import es.optsicom.lib.expresults.model.InstanceDescription;
import es.optsicom.lib.expresults.model.MethodDescription;
import es.optsicom.lib.expresults.model.Researcher;

public class JSONExperimentLoaderTest {

	private MemoryExperimentManager em;

	@Before
	public void setUp() {
		try {
			em = JSONExperimentLoader.load(new File("experiment.json"));
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testExperiment() {
		Experiment exp = em.getExperiment();
		
		assertEquals("FinalExp", exp.getName());
		
		Researcher researcher = exp.getResearcher();
		assertEquals("Patxi", researcher.getName());
		
		ComputerDescription computer = exp.getComputer();
		assertEquals("neuron1", computer.getName());
	}
	
	@Test
	public void testInstances() {
		List<InstanceDescription> instances = em.getInstances();
		assertEquals(1, instances.size());
		
		InstanceDescription id = instances.get(0);
		DBProperties properties = id.getProperties();
		assertEquals("mesh15_7", properties.get("name"));
		assertEquals("grids", properties.get("setId"));
		assertEquals("small", properties.get("size"));
	}
	
	@Test
	public void testMethods() {
		
		List<MethodDescription> methods = em.getExperiment().getMethods();
		assertEquals(2, methods.size());
		
		assertEquals("evpr", methods.get(0).getName());
		
		DBProperties evprProperties = methods.get(0).getProperties();
		assertEquals("C2", evprProperties.get("constructive"));
		assertEquals("2", evprProperties.get("b"));
		
		assertEquals("vns", methods.get(1).getName());
		
		DBProperties vnsProperties = methods.get(1).getProperties();
		assertEquals("LAH", vnsProperties.get("constructive"));
		assertEquals("0.1", vnsProperties.get("label"));
	}

}
