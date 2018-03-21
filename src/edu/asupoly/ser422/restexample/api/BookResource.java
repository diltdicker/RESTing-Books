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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.asupoly.ser422.restexample.model.Author;
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
	private HttpHeaders _headers;
	
	/**
     * @apiDefine BadRequestError
     * @apiError (Error 4xx) {400} BadRequest Bad Request Encountered
     * */
    /** @apiDefine ActivityNotFoundError
     * @apiError (Error 4xx) {404} NotFound Activity cannot be found
     * */
	/** @apiDefine ResourceNotFoundError
     * @apiError (Error 4xx) {404} NotFound Resource cannot be found
     * */
    /**
     * @apiDefine InternalServerError
     * @apiError (Error 5xx) {500} InternalServerError Something went wrong at server, Please contact the administrator!
     * */
    /**
     * @apiDefine NotImplementedError
     * @apiError (Error 5xx) {501} NotImplemented The resource has not been implemented. Please keep patience, our developers are working hard on it!!
     * */
	
	/**
     * @api {get} books Get list of Books
     * @apiName getBooks
     * @apiGroup Books
     *
     * @apiUse BadRequestError
     * @apiUse InternalServerError
     * 
     * @apiSuccessExample Success-Response:
     * 	HTTP/1.1 200 OK
     * 	[
     *   {"authorId":1111,"firstName":"Ariel","lastName":"Denham"},
     *   {"authorId":1212,"firstName":"John","lastName":"Worsley"}
     *  ]
     * 
     * */
	
	@GET
	public Response getBooks() {
		List<Book> books = __bService.getBooks();
		if (_headers.getRequestHeader("Accept-Encoding").get(0).equals(MediaType.APPLICATION_XML)) {
			return Response.status(Response.Status.OK).header("Content-type", 
					"application/xml").entity(BookSerializationHelper.getHelper().outputListXML(books)).build();
		} else {
			return Response.status(Response.Status.OK).header("Content-type", 
					"application/json").entity(BookSerializationHelper.getHelper().outputListJSON(books).toString()).build();
		}
	}
	
	/**
     * @api {get} books/{bookID} Get a single Book
     * @apiName getBook
     * @apiGroup Books
     *
     * @apiUse BadRequestError
     * @apiUse InternalServerError
     * 
     * @apiSuccessExample Success-Response:
     * 	HTTP/1.1 200 OK
     * 	[
     *   {"authorId":1111,"firstName":"Ariel","lastName":"Denham"},
     *   {"authorId":1212,"firstName":"John","lastName":"Worsley"}
     *  ]
     * 
     * */
	
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
	
	/**
     * @api {post} books Create a new Book
     * @apiName postBook
     * @apiGroup Books
     *
     * @apiUse BadRequestError
     * @apiUse InternalServerError
     * 
     * @apiSuccessExample Success-Response:
     * 	HTTP/1.1 200 OK
     * 	[
     *   {"authorId":1111,"firstName":"Ariel","lastName":"Denham"},
     *   {"authorId":1212,"firstName":"John","lastName":"Worsley"}
     *  ]
     * 
     * */
	
	@POST
	@Consumes("application/json")
	/*
	 * title authorID subjectID
	 */
	public Response createBook(String json) {
		int bID;
		Book b;
		System.out.println(json);
//		String[] elements = text.split(" ");
//		if (elements.length == 3) {
//			try {
//				bID = __bService.createBook(elements[0], Integer.parseInt(elements[1]), Integer.parseInt(elements[2]));
//				if (bID == -1) {
//					return Response.status(404, "{ \"message \" : \"No such Author " + "" + "\"}").build();
//				} else {
//					b = __bService.getBook(bID);
//					if (_headers.getMediaType() != null && _headers.getMediaType().toString().equals(MediaType.APPLICATION_XML)) {
//						return Response.status(Response.Status.CREATED).header("Content-type", 
//								"application/xml").entity(BookSerializationHelper.getHelper().convertXML(b)).build();
//					} else {
//						return Response.status(Response.Status.CREATED).header("Content-type", 
//								"application/json").entity(BookSerializationHelper.getHelper().convertJSON(b)).build();
//					}
//				}
//			} catch (NumberFormatException e) {
//				return Response.status(404, "{ \"message \" : \"No such Author " + "" + "\"}").build();
//			} catch (Exception e) {
//				return Response.status(404, "{ \"message \" : \"No such Author " + "" + "\"}").build();
//			}
//		} else {
//			return Response.status(404, "{ \"message \" : \"No such Author " + "" + "\"}").build();
//		}
		try {
			b = BookSerializationHelper.getHelper().consumeJSON(json);
			System.out.println(b);
			 bID = __bService.createBook(b.getTitle(), b.getAuthorId(), b.getSubjectId());
		} catch (Exception e) {
			e.printStackTrace();
			bID = -1;
		}
		if (bID == -1) {
			return Response.status(Response.Status.BAD_REQUEST).header("Content-type", "text/html").entity("created book").build();
		} else {
			b = __bService.getBook(bID);
			if (_headers.getRequestHeader("Accept-Encoding").get(0).equals(MediaType.APPLICATION_XML)) {
				return Response.status(Response.Status.CREATED).header("Content-type", 
						"application/xml").entity(BookSerializationHelper.getHelper().convertXML(b)).build();
			} else {
				return Response.status(Response.Status.CREATED).header("Content-type", 
						"application/json").entity(b).build();
			}
		}
		
	}
	
	/**
     * @api {get} books/{bookID}/author Get an author of a Book
     * @apiName getBookAuthor
     * @apiGroup Books
     *
     * @apiUse BadRequestError
     * @apiUse InternalServerError
     * 
     * @apiSuccessExample Success-Response:
     * 	HTTP/1.1 200 OK
     * 	[
     *   {"authorId":1111,"firstName":"Ariel","lastName":"Denham"},
     *   {"authorId":1212,"firstName":"John","lastName":"Worsley"}
     *  ]
     * 
     * */
	
	@GET
	@Path("/{bookID}/author")
	public Response findAuthorOfBook(@PathParam("bookID") int bID) {
		Book book  = __bService.getBook(bID);
		Author author = null;
		if (book != null) {
			author = __bService.getAuthor(book.getAuthorId());
		}
		if (author != null) {
			if (_headers.getRequestHeader("Accept-Encoding").get(0).equals(MediaType.APPLICATION_XML)) {
				return Response.status(Response.Status.OK).header("Content-type", 
						"application/xml").entity(AuthorSerializationHelper.getHelper().convertXML(author)).build();
			} else {
				try {
					return Response.status(Response.Status.CREATED).header("Content-type", 
							"application/json").entity(AuthorSerializationHelper.getHelper().convertJSON(author)).build();
				} catch (JsonProcessingException e) {
					e.printStackTrace();
					return Response.status(500, "{ \"message \" : \"Internal Error " + "\"}").build();
				}
			}
		} else {
			return Response.status(404, "{ \"message \" : \"No such Author " + bID + "\"}").build();
		}
	}
	
	/**
     * @api {delete} books Delete a single book
     * @apiName deleteBook
     * @apiGroup Books
     *
     * @apiUse BadRequestError
     * @apiUse InternalServerError
     * 
     * @apiSuccessExample Success-Response:
     * 	HTTP/1.1 200 OK
     * 	[
     *   {"authorId":1111,"firstName":"Ariel","lastName":"Denham"},
     *   {"authorId":1212,"firstName":"John","lastName":"Worsley"}
     *  ]
     * 
     * */
	
	@DELETE
    public Response deleteBook(@QueryParam("id") int bID) {
		if (__bService.deleteBook(bID)) {
			return Response.status(204).build();
		} else {
			return Response.status(404, "{ \"message \" : \"No such Book " + bID + "\"}").build();
		}
    }
}
