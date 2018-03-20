package edu.asupoly.ser422.restexample.api;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.asupoly.ser422.restexample.model.Book;
import edu.asupoly.ser422.restexample.services.BooktownService;
import edu.asupoly.ser422.restexample.services.BooktownServiceFactory;

@Path("/books")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class BookResource {
	private static BooktownService __bService = BooktownServiceFactory.getInstance();
	
	@Context
	private UriInfo _uriInfo;
	@Context
	private HttpHeaders headers;
	
	@GET
	public List<Book> getBooks() {
		return __bService.getBooks();
	}
	
	@GET
	@Path("/{bookID}")
	public Response getBook(@PathParam("bookID") int bID) {
		Book book = __bService.getBook(bID);
		try {
			String aString = new ObjectMapper().writeValueAsString(book);
			//if (headers.CONTENT_TYPE == MediaType.APPLICATION_XML_TYPE) {
				return Response.status(Response.Status.OK).header("Content-type", "application/xml").entity("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+"\n" + aString).build();
			//} else {
				// json
				//return Response.status(Response.Status.OK).header("Content-type", "application/json").entity(aString).build();
			//}
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/*@POST
	@Consumes("text/plain")
	public Response createBook(String title, int aid, int sid) {
		return null;
	}*/
	
	@GET
	@Path("/{bookID}/author")
	public Response findAuthorOfBook(@PathParam("bookID") int bID) {
		return null;
	}
	
	@DELETE
    public Response deleteBook(@QueryParam("id") int bID) {
		if (__bService.deleteBook(bID)) {
			return Response.status(204).build();
		} else {
			return Response.status(404, "{ \"message \" : \"No such Author " + bID + "\"}").build();
		}
    }
}
