package edu.asupoly.ser422.restexample.api;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;

import edu.asupoly.ser422.restexample.model.Author;
import edu.asupoly.ser422.restexample.model.Subject;

public final class SubjectSerializationHelper {

	private static final String __LOCATION = "location";
	private static final String __SUBJECT = "subject";
	private static final String __SUBJECTID = "subjectId";
	private final static SubjectSerializationHelper __me = new SubjectSerializationHelper();
	private ObjectMapper mapper = new ObjectMapper();
	private SimpleModule module = new SimpleModule();
	
	// Singleton
	private SubjectSerializationHelper() {
		module.addSerializer(Subject.class, new SubjectJSON());
		module.addDeserializer(Subject.class, new JSONSubject());
		mapper.registerModule(module);
	}
	
	public static SubjectSerializationHelper getHelper() {
		return __me;
	}
	
	public String generateJSON(Subject author) throws JsonProcessingException {
		// Since a custom serializer was added to the mapper via registerModule,
		// internally it will invoke the serialize method in the inner class below
		return mapper.writeValueAsString(author);
	}
	
	public Subject consumeJSON(String json) throws IOException, JsonProcessingException {
		// A deserializer goes from JSON to the Object using the inverse process
		System.out.println("consumeJSON: " + json);
		return mapper.readValue(json, Subject.class);
	}
	
	// Inner class for custom Author deserialization.
	// Loosely based on http://tutorials.jenkov.com/java-json/jackson-objectmapper.html
    final private class JSONSubject extends JsonDeserializer<Subject>  {
		@Override
		public Subject deserialize(JsonParser parser, DeserializationContext context)
				throws IOException, JsonProcessingException {
			Subject subject = new Subject();
			JsonToken token = parser.nextToken();
			while (!parser.isClosed()) {
				System.out.print("Deserializer processing token: " + token.asString());
				if (token != null && JsonToken.FIELD_NAME.equals(token)) {
					// we have a JSON Field, get it and see which one we have
					String fieldName = parser.getCurrentName();
					System.out.println(", field name: " + fieldName);
					// Check for which of our 3 fields comes next and set the next token in there
					token = parser.nextToken();
					if (fieldName.equals(__SUBJECTID)) 
						subject.setSubjectId(parser.getValueAsInt());
					else if (fieldName.equals(__SUBJECT))
						subject.setSubject(parser.getValueAsString());
					else if (fieldName.equals(__LOCATION))
						subject.setLocation(parser.getValueAsString());
				}
				token = parser.nextToken();
			}
			System.out.println("Deserializer returning Subject: " + subject);
			return subject;
		}
    }
	
	// Inner class for custom Author serialization.
    final private class SubjectJSON extends JsonSerializer<Subject>  {
       @Override
       public void serialize(Subject subject, JsonGenerator jgen, SerializerProvider provider)
               throws IOException, JsonProcessingException {
           jgen.writeStartObject();	
           jgen.writeNumberField(__SUBJECTID, subject.getSubjectId());
           jgen.writeStringField(__SUBJECT, subject.getSubject());
           jgen.writeStringField(__LOCATION, subject.getLocation());
           jgen.writeEndObject();
       }
   }
    
    public JsonNode outputListJSON(List<Subject> subjectList) {
 	   JsonNode obj = mapper.valueToTree(subjectList);
 	   //ObjectNode obj = JsonNodeFactory.instance.objectNode();
 	   //JSONPObject json = new JSONPObject(mapper.writeValueAsString(authorList));
 	   System.out.println("RESULT-JSON:\n" + obj);
 	   return obj;
    }
    
    public String convertJSON(Subject subject) throws JsonProcessingException {
 	   return new ObjectMapper().writeValueAsString(subject);
    }
    
    public String outputListXML(List<Subject> subjectList) {
 	   String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
 	   JsonNode obj = mapper.valueToTree(subjectList);
 	   System.out.println("RESULT-XML:\n" + obj);
 	   xmlString += "\n<subjectList>";
 	   for (int i = 0; i < obj.size(); i++) {
 		   xmlString += "\n<subject>";
 		   Iterator<Entry<String, JsonNode>> nodes = obj.get(i).fields();
 		   while(nodes.hasNext()) {
 			   Map.Entry<String, JsonNode> entry = (Map.Entry<String, JsonNode>) nodes.next();
 			   System.out.println("Key " + entry.getKey() + " Value " + entry.getValue());
 			   xmlString += "\n<" + entry.getKey() + ">" + entry.getValue().asText() + "</" + entry.getKey() + ">";
 		   }
 		   xmlString += "\n</subject>";
 	   }
 	   xmlString += "\n</subjectList>";
 	   Iterator<Entry<String, JsonNode>> nodes = obj.get(0).fields();
 	   while(nodes.hasNext()) {
 		   Map.Entry<String, JsonNode> entry = (Map.Entry<String, JsonNode>) nodes.next();
 		   System.out.println("Key " + entry.getKey() + " Value " + entry.getValue());
 	   }
 	   return xmlString;
 	   
    }
    
    public String convertXML(Subject subject) {
 	   String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
 	   JsonNode obj = mapper.valueToTree(subject);
 	   xmlString += "\n<subject>";
 	   Iterator<Entry<String, JsonNode>> nodes = obj.fields();
 	   while(nodes.hasNext()) {
 		   Map.Entry<String, JsonNode> entry = (Map.Entry<String, JsonNode>) nodes.next();
 		   System.out.println("Key " + entry.getKey() + " Value " + entry.getValue());
 		   xmlString += "\n<" + entry.getKey() + ">" + entry.getValue().asText() + "</" + entry.getKey() + ">";
 	   }
 	   xmlString += "\n</subject>";
 	   return xmlString; 
    }

}
