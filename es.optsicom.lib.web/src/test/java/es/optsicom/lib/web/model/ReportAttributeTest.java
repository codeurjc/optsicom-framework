package es.optsicom.lib.web.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import es.optsicom.lib.analyzer.tablecreator.atttable.Attribute;

public class ReportAttributeTest {
private ReportAttribute rAttr;
private Attribute attrMock;
	
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
