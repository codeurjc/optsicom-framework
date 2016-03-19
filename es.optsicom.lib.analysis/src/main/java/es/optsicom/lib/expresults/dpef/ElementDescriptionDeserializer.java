package es.optsicom.lib.expresults.dpef;

import java.io.IOException;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;

import es.optsicom.lib.expresults.model.ElementDescription;

public class ElementDescriptionDeserializer extends JsonDeserializer<ElementDescription> {

	// private static final String ID_PROPERTY_NAME = "name";
	// private static final String FILENAME_PROPERTY_NAME = "filename";
	// private static final String INSTANCE_SET_ID_PROPERTY_NAME =
	// "instancesetid";
	// private static final String NUM_PROPERTY_NAME = "num";
	// private static final String USECASE = "usecase";

	@Override
	public ElementDescription deserialize(JsonParser jp, DeserializationContext ctxt, ElementDescription intoValue)
			throws IOException, JsonProcessingException {
		// TODO Auto-generated method stub
		return super.deserialize(jp, ctxt, intoValue);
	}
	//
	// @Override
	// public ElementDescription deserialize(JsonParser jp,
	// DeserializationContext ctxt) throws IOException,
	// JsonProcessingException {
	//
	// JsonNode node = jp.readValueAsTree();
	//
	//
	//
	// node.get
	//
	//
	// jp.
	// Set<String> writtenProps = new HashSet<String>();
	//
	// jgen.writeStartObject();
	//
	// writeField(ID_PROPERTY_NAME, element, jgen, writtenProps);
	//
	// if(element instanceof InstanceDescription){
	// writeField(FILENAME_PROPERTY_NAME, element, jgen, writtenProps);
	// writeField(INSTANCE_SET_ID_PROPERTY_NAME, element, jgen, writtenProps);
	// writeField(NUM_PROPERTY_NAME, element, jgen, writtenProps);
	// writeField(USECASE, element, jgen, writtenProps);
	// }
	//
	// DBProperties properties = element.getProperties();
	// for(String key : new TreeSet<String>(properties.getMap().keySet())){
	// if(!writtenProps.contains(key)){
	// String value = properties.get(key);
	// if(!value.equals("null")){
	// jgen.writeStringField(key, value);
	// }
	//
	// }
	//
	//
	// @Override
	// public void serialize(ElementDescription element, JsonGenerator jgen,
	// SerializerProvider provider) throws IOException,
	// JsonProcessingException {
	//
	//
	// }
	// }
	// jgen.writeEndObject();
	// }
	//
	// private void writeField(String propertyName, ElementDescription element,
	// JsonGenerator jgen, Set<String> writtenProps) throws IOException,
	// JsonGenerationException {
	// jgen.writeStringField(propertyName,
	// element.getProperties().get(propertyName));
	// writtenProps.add(propertyName);
	// }
	//
	// @Override
	// public Class<ElementDescription> handledType() {
	// return ElementDescription.class;
	// }

	@Override
	public ElementDescription deserialize(JsonParser arg0, DeserializationContext arg1)
			throws IOException, JsonProcessingException {
		// TODO Auto-generated method stub
		return null;
	}

}
