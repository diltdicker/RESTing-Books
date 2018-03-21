package edu.asupoly.ser422.restexample.api;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import jdk.nashorn.api.scripting.JSObject;

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
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.JSONPObject;

import edu.asupoly.ser422.restexample.model.Author;

/*
 * This is an example of a simple custom serializer (converts a Java object to a JSON string).
 * Note all this one does is rename the property keys for Author, but what you do in the serialize
 * method is entirely up to you, so you may generalize to as rich a hypermedia format as you like.
 * 
 * Note that you would typically write a deserializer as well when you customize the JSON. A
 * deserializer goes in the opposite direction, converting JSON to a Java object. So, in this case
 */
public final class AuthorSerializationHelper {
	// some locally used constant naming our Author field names
	private static final String __AUTHORID = "authorId";
	private static final String __LASTNAME = "lastName";
	private static final String __FIRSTNAME = "firstName";
	
	private final static AuthorSerializationHelper __me = new AuthorSerializationHelper();
	private ObjectMapper mapper = new ObjectMapper();
	private SimpleModule module = new SimpleModule();
	
	// Singleton
	private AuthorSerializationHelper() {
		module.addSerializer(Author.class, new AuthorJSON());
		module.addDeserializer(Author.class, new JSONAuthor());
		mapper.registerModule(module);
	}
	
	public static AuthorSerializationHelper getHelper() {
		return __me;
	}
	
	public String generateJSON(Author author) throws JsonProcessingException {
		// Since a custom serializer was added to the mapper via registerModule,
		// internally it will invoke the serialize method in the inner class below
		return mapper.writeValueAsString(author);
	}
	
	public Author consumeJSON(String json) throws IOException, JsonProcessingException {
		// A deserializer goes from JSON to the Object using the inverse process
		System.out.println("consumeJSON: " + json);
		return mapper.readValue(json, Author.class);
	}
	
	// Inner class for custom Author deserialization.
	// Loosely based on http://tutorials.jenkov.com/java-json/jackson-objectmapper.html
    final private class JSONAuthor extends JsonDeserializer<Author>  {
		@Override
		public Author deserialize(JsonParser parser, DeserializationContext context)
				throws IOException, JsonProcessingException {
			Author author = new Author();
			JsonToken token = parser.nextToken();
			while (!parser.isClosed()) {
				System.out.print("Deserializer processing token: " + token.asString());
				if (token != null && JsonToken.FIELD_NAME.equals(token)) {
					// we have a JSON Field, get it and see which one we have
					String fieldName = parser.getCurrentName();
					System.out.println(", field name: " + fieldName);
					// Check for which of our 3 fields comes next and set the next token in there
					token = parser.nextToken();
					if (fieldName.equals(__AUTHORID)) 
						author.setAuthorId(parser.getValueAsInt());
					else if (fieldName.equals(__LASTNAME))
						author.setLastName(parser.getValueAsString());
					else if (fieldName.equals(__FIRSTNAME))
						author.setFirstName(parser.getValueAsString());
				}
				token = parser.nextToken();
			}
			System.out.println("Deserializer returning Author: " + author);
			return author;
		}
    }
    
	// Inner class for custom Author serialization.
    final private class AuthorJSON extends JsonSerializer<Author>  {
       @Override
       public void serialize(Author author, JsonGenerator jgen, SerializerProvider provider)
               throws IOException, JsonProcessingException {
           jgen.writeStartObject();	
           jgen.writeNumberField(__AUTHORID, author.getAuthorId());
           jgen.writeStringField(__LASTNAME, author.getLastName());
           jgen.writeStringField(__FIRSTNAME, author.getFirstName());
           jgen.writeEndObject();
       }
   }
    
   public String outputListJSON(List<Author> authorList) {
	   JsonNode obj = mapper.valueToTree(authorList);
	   //ObjectNode obj = JsonNodeFactory.instance.objectNode();
	   //JSONPObject json = new JSONPObject(mapper.writeValueAsString(authorList));
	   System.out.println("RESULT-JSON:\n" + obj);
	   return obj.toString();
   }
   
   public String convertJSON(Author author) throws JsonProcessingException {
	   return new ObjectMapper().writeValueAsString(author);
   }
   
   public String outputListXML(List<Author> authorList) {
	   String xmlString = "";//<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
	   JsonNode obj = mapper.valueToTree(authorList);
	   System.out.println("RESULT-XML:\n" + obj);
	   xmlString += "<authorList>";
	   for (int i = 0; i < obj.size(); i++) {
		   xmlString += "\n<author>";
		   Iterator<Entry<String, JsonNode>> nodes = obj.get(i).fields();
		   while(nodes.hasNext()) {
			   Map.Entry<String, JsonNode> entry = (Map.Entry<String, JsonNode>) nodes.next();
			   System.out.println("Key " + entry.getKey() + " Value " + entry.getValue());
			   xmlString += "\n<" + entry.getKey() + ">" + entry.getValue().asText() + "</" + entry.getKey() + ">";
		   }
		   xmlString += "\n</author>";
	   }
	   xmlString += "\n</authorList>";
	   Iterator<Entry<String, JsonNode>> nodes = obj.get(0).fields();
	   while(nodes.hasNext()) {
		   Map.Entry<String, JsonNode> entry = (Map.Entry<String, JsonNode>) nodes.next();
		   System.out.println("Key " + entry.getKey() + " Value " + entry.getValue());
	   }
	   return xmlString;
	   
   }
   
   public String convertXML(Author author) {
	   String xmlString = "";//<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
	   JsonNode obj = mapper.valueToTree(author);
	   xmlString += "<author>";
	   Iterator<Entry<String, JsonNode>> nodes = obj.fields();
	   while(nodes.hasNext()) {
		   Map.Entry<String, JsonNode> entry = (Map.Entry<String, JsonNode>) nodes.next();
		   System.out.println("Key " + entry.getKey() + " Value " + entry.getValue());
		   xmlString += "\n<" + entry.getKey() + ">" + entry.getValue().asText() + "</" + entry.getKey() + ">";
	   }
	   xmlString += "\n</author>";
	   return xmlString; 
   }
   
   
}
