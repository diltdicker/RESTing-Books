package edu.asupoly.ser422.restexample.api;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.asupoly.ser422.restexample.model.Author;
import edu.asupoly.ser422.restexample.model.Book;
import edu.asupoly.ser422.restexample.model.Subject;
import edu.asupoly.ser422.restexample.services.BooktownService;
import edu.asupoly.ser422.restexample.services.BooktownServiceFactory;

@Path("/subjects")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class SubjectResource {
	private static BooktownService __bService = BooktownServiceFactory.getInstance();
	
	@Context
	private UriInfo _uriInfo;
	
	@GET
	public List<Subject> getSubjects(){
		return __bService.getSubjects();
	}
	
	@GET
	@Path("/{subjectID}")
	public Response getSubject(@PathParam("subjectID") int subID) {
		Subject subject = __bService.getSubject(subID);
		try {
			String aString = new ObjectMapper().writeValueAsString(subject);
			return Response.status(Response.Status.OK).entity(aString).build();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@GET
	@Path("/{subjectID}/books")
	public List<Book> findBooksBySubject(@PathParam("subjectID") int subID) {
		return null;
	}
	
	/*@PUT
	@Consumes("application/json")
    public Response updateSubject(String json) {
		System.out.println(json);
		try {
			Subject s = SubjectSerializationHelper.getHelper().consumeJSON(json);
			System.out.println(s);
			if (__bService.updateSubject(s)) {
				// In the response payload it would still use Jackson's default serializer,
				// so we directly invoke our serializer so the PUT payload reflects what it should.
				String aString = AuthorSerializationHelper.getHelper().generateJSON(s);
				return Response.status(201).entity(aString).build();
			} else {
				return Response.status(404, "{ \"message \" : \"No such Subject " + s.getSubjectId() + "\"}").build();
			}
		} catch (Exception exc) {
			exc.printStackTrace();
			return Response.status(500, "{ \"message \" : \"Internal server error deserializing Subject JSON\"}").build();
		}
    }*/
}
