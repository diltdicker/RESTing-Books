package edu.asupoly.ser422.restexample.api;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

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
		return null;
	}
	
	@GET
	@Path("/{subjectID}/books")
	public List<Book> findBooksBySubject(@PathParam("subjectID") int subID) {
		return null;
	}
}
