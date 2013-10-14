package es.optsicom.lib.expresults.dpef;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

import es.optsicom.lib.expresults.model.DBProperties;
import es.optsicom.lib.expresults.model.ElementDescription;
import es.optsicom.lib.expresults.model.InstanceDescription;

//Test
public class ElementDescriptionSerializer extends JsonSerializer<ElementDescription> {

	private static final String ID_PROPERTY_NAME = "name";
	private static final String FILENAME_PROPERTY_NAME = "filename";
	private static final String INSTANCE_SET_ID_PROPERTY_NAME = "instancesetid";
	private static final String NUM_PROPERTY_NAME = "num";
	private static final String USECASE = "usecase";

	@Override
	public void serialize(ElementDescription element, JsonGenerator jgen,
			SerializerProvider provider) throws IOException,
			JsonProcessingException {
	
		Set<String> writtenProps = new HashSet<String>();
		
		jgen.writeStartObject();
		
		writeField(ID_PROPERTY_NAME, element, jgen, writtenProps);
		
		if(element instanceof InstanceDescription){
			writeField(FILENAME_PROPERTY_NAME, element, jgen, writtenProps);
			writeField(INSTANCE_SET_ID_PROPERTY_NAME, element, jgen, writtenProps);
			writeField(NUM_PROPERTY_NAME, element, jgen, writtenProps);
			writeField(USECASE, element, jgen, writtenProps);
		}
		
		DBProperties properties = element.getProperties();
		for(String key : new TreeSet<String>(properties.getMap().keySet())){
			if(!writtenProps.contains(key)){
				String value = properties.get(key);
				if(!value.equals("null")){					
					jgen.writeStringField(key, value);
				}
			}
		}		
		jgen.writeEndObject();
	}

	private void writeField(String propertyName, ElementDescription element, JsonGenerator jgen, Set<String> writtenProps)
			throws IOException, JsonGenerationException {
		jgen.writeStringField(propertyName, element.getProperties().get(propertyName));
		writtenProps.add(propertyName);
	}
	
	@Override
	public Class<ElementDescription> handledType() {
		return ElementDescription.class;
	}

}
