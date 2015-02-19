package es.optsicom.lib.web.model;

import static org.junit.Assert.*;
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

import es.optsicom.lib.analyzer.tablecreator.atttable.Attribute;
import es.optsicom.lib.expresults.manager.ExperimentManager;
import es.optsicom.lib.expresults.model.ComputerDescription;
import es.optsicom.lib.expresults.model.Experiment;
import es.optsicom.lib.expresults.model.Researcher;
import es.optsicom.lib.web.service.ExperimentService;
import es.optsicom.lib.web.web.ExperimentsRestController;

public class ReportAttributeTest {
private ReportAttribute rAttr;
private Attribute attrMock;
private Attribute attrMockNull;

	
	@Test
	public void getterSetter(){
		rAttr = new ReportAttribute();
		rAttr.setName("name");
		rAttr.setValue("value");
		assertTrue("name".equals(rAttr.getName()));
		assertTrue("value".equals(rAttr.getValue()));
		assertFalse("name".equals(rAttr.getValue()));
		assertFalse("value".equals(rAttr.getName()));
	}
	@Test
	public void constructorNameValue(){
		rAttr = new ReportAttribute("name", "value");
		assertTrue("name".equals(rAttr.getName()));
		assertTrue("value".equals(rAttr.getValue()));
	}
	@Test
	public void constructorNameValueWithNull(){
		rAttr = new ReportAttribute(null, null);
		assertTrue("".equals(rAttr.getName()));
		assertTrue("null".equals(rAttr.getValue()));
	}
	@Test
	public void constructorAttribute(){
		attrMock = mock(Attribute.class);
		when(attrMock.getTitle()).thenReturn("value");
		when(attrMock.getName()).thenReturn("name");
		rAttr = new ReportAttribute(attrMock);
		assertTrue("name".equals(rAttr.getName()));
		assertTrue("value".equals(rAttr.getValue()));
	}
	@Test
	public void constructorAttributeNull(){
		attrMock = mock(Attribute.class);
		when(attrMock.getTitle()).thenReturn(null);
		when(attrMock.getName()).thenReturn(null);
		rAttr = new ReportAttribute(attrMock);
		assertTrue("".equals(rAttr.getName()));
		assertTrue("null".equals(rAttr.getValue()));
	}
}
