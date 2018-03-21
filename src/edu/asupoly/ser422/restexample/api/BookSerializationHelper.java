package edu.asupoly.ser422.restexample.api;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
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
import edu.asupoly.ser422.restexample.model.Book;

public class BookSerializationHelper {
	// some locally used constant naming our Author field names
		private static final String __BOOKID = "bookId";
		private static final String __BOOKTITLE = "title";
		private static final String __AUTHORID = "authorID";
		private static final String __SUBJECTID = "subjectID";
		
		private final static BookSerializationHelper __me = new BookSerializationHelper();
		private ObjectMapper mapper = new ObjectMapper();
		private SimpleModule module = new SimpleModule();
		
		// Singleton
		private BookSerializationHelper() {
			module.addSerializer(Book.class, new BookJSON());
			module.addDeserializer(Book.class, new JSONBook());
			mapper.registerModule(module);
		}
		
		public static BookSerializationHelper getHelper() {
			return __me;
		}
		
		public String generateJSON(Book book) throws JsonProcessingException {
			// Since a custom serializer was added to the mapper via registerModule,
			// internally it will invoke the serialize method in the inner class below
			return mapper.writeValueAsString(book);
		}
		
		public Book consumeJSON(String json) throws IOException, JsonProcessingException {
			// A deserializer goes from JSON to the Object using the inverse process
			System.out.println("consumeJSON: " + json);
			return mapper.readValue(json, Book.class);
		}
		
		// Inner class for custom Author deserialization.
		// Loosely based on http://tutorials.jenkov.com/java-json/jackson-objectmapper.html
	    final private class JSONBook extends JsonDeserializer<Book>  {
			@Override
			public Book deserialize(JsonParser parser, DeserializationContext context)
					throws IOException, JsonProcessingException {
				Book book = new Book();
				JsonToken token = parser.nextToken();
				while (!parser.isClosed()) {
					System.out.print("Deserializer processing token: " + token.asString());
					if (token != null && JsonToken.FIELD_NAME.equals(token)) {
						// we have a JSON Field, get it and see which one we have
						String fieldName = parser.getCurrentName();
						System.out.println(", field name: " + fieldName);
						// Check for which of our 3 fields comes next and set the next token in there
						token = parser.nextToken();
						if (fieldName.equals(__BOOKID)) 
							book.setBookId(parser.getValueAsInt());
						else if (fieldName.equals(__AUTHORID))
							book.setAuthorId(parser.getValueAsInt());
						else if (fieldName.equals(__SUBJECTID))
							book.setSubjectId(parser.getValueAsInt());
						else if (fieldName.equals(__BOOKTITLE))
							book.setTitle(parser.getValueAsString());
					}
					token = parser.nextToken();
				}
				System.out.println("Deserializer returning Author: " + book);
				return book;
			}
	    }
	    
		// Inner class for custom Author serialization.
	    final private class BookJSON extends JsonSerializer<Book>  {
	       @Override
	       public void serialize(Book book, JsonGenerator jgen, SerializerProvider provider)
	               throws IOException, JsonProcessingException {
	           jgen.writeStartObject();	
	           jgen.writeNumberField(__BOOKID, book.getBookId());
	           jgen.writeNumberField(__AUTHORID, book.getAuthorId());
	           jgen.writeNumberField(__SUBJECTID, book.getSubjectId());
	           jgen.writeStringField(__BOOKTITLE, book.getTitle());
	           jgen.writeEndObject();
	       }
	   }
	    
	   public String outputListJSON(List<Book> bookList) {
		   JsonNode obj = mapper.valueToTree(bookList);
		   //ObjectNode obj = JsonNodeFactory.instance.objectNode();
		   //JSONPObject json = new JSONPObject(mapper.writeValueAsString(authorList));
		   System.out.println("RESULT-JSON:\n" + obj);
		   return obj.toString();
	   }
	   
	   public String convertJSON(Book book) throws JsonProcessingException {
		   return new ObjectMapper().writeValueAsString(book);
	   }
	   
	   public String outputListXML(List<Book> bookList) {
		   String xmlString = "";//<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		   JsonNode obj = mapper.valueToTree(bookList);
		   System.out.println("RESULT-XML:\n" + obj);
		   xmlString += "\n<bookList>";
		   for (int i = 0; i < obj.size(); i++) {
			   xmlString += "\n<book>";
			   Iterator<Entry<String, JsonNode>> nodes = obj.get(i).fields();
			   while(nodes.hasNext()) {
				   Map.Entry<String, JsonNode> entry = (Map.Entry<String, JsonNode>) nodes.next();
				   System.out.println("Key " + entry.getKey() + " Value " + entry.getValue());
				   xmlString += "\n<" + entry.getKey() + ">" + entry.getValue().asText() + "</" + entry.getKey() + ">";
			   }
			   xmlString += "\n</book>";
		   }
		   xmlString += "\n</bookList>";
		   Iterator<Entry<String, JsonNode>> nodes = obj.get(0).fields();
		   while(nodes.hasNext()) {
			   Map.Entry<String, JsonNode> entry = (Map.Entry<String, JsonNode>) nodes.next();
			   System.out.println("Key " + entry.getKey() + " Value " + entry.getValue());
		   }
		   return xmlString;
		   
	   }
	   
	   public String convertXML(Book book) {
		   String xmlString = "";//<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		   JsonNode obj = mapper.valueToTree(book);
		   xmlString += "\n<book>";
		   Iterator<Entry<String, JsonNode>> nodes = obj.fields();
		   while(nodes.hasNext()) {
			   Map.Entry<String, JsonNode> entry = (Map.Entry<String, JsonNode>) nodes.next();
			   System.out.println("Key " + entry.getKey() + " Value " + entry.getValue());
			   xmlString += "\n<" + entry.getKey() + ">" + entry.getValue().asText() + "</" + entry.getKey() + ">";
		   }
		   xmlString += "\n</book>";
		   return xmlString; 
	   }
	   
//	   public Book parseJSONBook(String json) {
//		   Book b = new Book();
//		   try {
//			   JsonFactory factory = new JsonFactory();
//			   JsonParser parser = factory.createParser(json);
//			   while (!parser.isClosed()) {
//				   System.out.println(parser.getCurrentName());
//				   
//				   JsonToken jsonToken = parser.nextToken();
//				   System.out.println(jsonToken.VALUE_STRING);
//				   System.out.println(parser.getCurrentName());
//				   //System.out.println(parser.getCurrentValue().toString());
//				   //if (JsonToken.FIELD_NAME.equals(jsonToken)) {
//					   //System.out.println(jsonToken.asString());
//				   //}
//			   }
//		} catch (JsonParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			b = null;
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			b = null;
//		}
//		   return b;
//	   }
}
