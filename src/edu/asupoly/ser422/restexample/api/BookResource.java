package edu.asupoly.ser422.restexample.api;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import edu.asupoly.ser422.restexample.model.Book;
import edu.asupoly.ser422.restexample.services.BooktownService;
import edu.asupoly.ser422.restexample.services.BooktownServiceFactory;

@Path("/books")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class BookResource {
	private static BooktownService __bService = BooktownServiceFactory.getInstance();
	
	@Context
	private UriInfo _uriInfo;
	
	@GET
	public List<Book> getBooks() {
		return __bService.getBooks();
	}
	
	@GET
	@Path("/{bookID}")
	public Response getBook(@PathParam("bookID") int bID) {
		return null;
	}
	
	@POST
	@Consumes("text/plain")
	public Response createBook(String title, int aid, int sid) {
		return null;
	}
	
	@GET
	@Path("/{bookID}/author")
	public Response findAuthorOfBook(@PathParam("bookID") int bID) {
		return null;
	}
}
