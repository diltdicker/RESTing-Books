package edu.asupoly.ser422.restexample.api;

import java.io.IOException;
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
     * @apiUse ResourceNotFoundError
     * @apiUse InternalServerError
     * 
     * @apiHeader {String} Accept-Encoding=application/json can be set to application/xml
     * 
     * @apiSuccessExample Success-Response:
     * 	HTTP/1.1 200 OK
{
    "body": [
        {
            "body": {
                "title": "Sisters First",
                "authorId": 0,
                "subjectId": 0,
                "bookId": 0
            },
            "links": {
                "location": "http://localhost:8080/RestExampleAPI/rest/books/0",
                "author": "http://localhost:8080/RestExampleAPI/rest/books/0/author"
            }
        },
        {
            "body": {
                "title": "My Turn",
                "authorId": 1,
                "subjectId": 1,
                "bookId": 1
            },
            "links": {
                "location": "http://localhost:8080/RestExampleAPI/rest/books/1",
                "author": "http://localhost:8080/RestExampleAPI/rest/books/1/author"
            }
        },
        {
            "body": {
                "title": "Four Days",
                "authorId": 2,
                "subjectId": 2,
                "bookId": 2
            },
            "links": {
                "location": "http://localhost:8080/RestExampleAPI/rest/books/2",
                "author": "http://localhost:8080/RestExampleAPI/rest/books/2/author"
            }
        }
    ],
    "links": {
        "location": "http://localhost:8080/RestExampleAPI/rest/books"
    }
}
     * 
     * HTTP/1.1 200 OK
<?xml version="1.0" encoding="UTF-8"?>
<response>
    <response>
        <book>
            <bookId>0</bookId>
            <authorID>0</authorID>
            <subjectID>0</subjectID>
            <title>Sisters First</title>
        </book>
        <links>
            <location>http://localhost:8080/RestExampleAPI/rest/books/0</location>
            <author>http://localhost:8080/RestExampleAPI/rest/books/0/author</author>
        </links>
    </response>
    <response>
        <book>
            <bookId>1</bookId>
            <authorID>1</authorID>
            <subjectID>1</subjectID>
            <title>My Turn</title>
        </book>
        <links>
            <location>http://localhost:8080/RestExampleAPI/rest/books/1</location>
            <author>http://localhost:8080/RestExampleAPI/rest/books/1/author</author>
        </links>
    </response>
    <response>
        <book>
            <bookId>2</bookId>
            <authorID>2</authorID>
            <subjectID>2</subjectID>
            <title>Four Days</title>
        </book>
        <links>
            <location>http://localhost:8080/RestExampleAPI/rest/books/2</location>
            <author>http://localhost:8080/RestExampleAPI/rest/books/2/author</author>
        </links>
    </response>
    <links>
        <location>http://localhost:8080/RestExampleAPI/rest/books</location>
    </links>
</response>
     * 
     * */
	
	@GET
	public Response getBooks() {
		List<Book> books = __bService.getBooks();
		if (books == null || books.size() == 0) {
			return Response.status(404, "{ \"message \" : \"No Books\"}").build();
		} else {
			if (_headers.getRequestHeader("Accept-Encoding").get(0).equals(MediaType.APPLICATION_XML)) {
				String resBody = "";
				for (int i = 0; i < books.size(); i++) {
					String tmp = BookSerializationHelper.getHelper().convertXML(books.get(i));
					ResponseBody partial = new ResponseBody(true, tmp);
					partial.addLinkValue("location", _uriInfo.getAbsolutePath().toString() + "/" + books.get(i).getBookId());
					partial.addLinkValue("author", _uriInfo.getAbsolutePath().toString() + "/" + books.get(i).getBookId() + "/author");
					resBody += partial.getPartialRepsonseString();
				}
				ResponseBody res = new ResponseBody(true, resBody);
				res.addLinkValue("location", _uriInfo.getAbsolutePath().toString());
				return Response.status(Response.Status.OK).header("Content-type", 
						"application/xml").entity(res.getResponseString()).build();
			} else {
				try {
				String resBody = "";
				for (int i = 0; i < books.size(); i++) {
					String tmp = BookSerializationHelper.getHelper().convertJSON(books.get(i));
					ResponseBody partial = new ResponseBody(false, tmp);
					partial.addLinkValue("location", _uriInfo.getAbsolutePath().toString() + "/" + books.get(i).getBookId());
					partial.addLinkValue("author", _uriInfo.getAbsolutePath().toString() + "/" + books.get(i).getBookId() + "/author");
					resBody += partial.getPartialRepsonseString();
					if (i != books.size() - 1) {
						resBody += ", ";
					}
				}
				ResponseBody res = new ResponseBody(false, ResponseBody.getPartialArray(resBody));
				res.addLinkValue("location", _uriInfo.getAbsolutePath().toString());
				return Response.status(Response.Status.OK).header("Content-type", 
						"application/json").entity(res.getResponseString()).build();
				} catch (JsonProcessingException e) {
					e.printStackTrace();
					return Response.status(500, "{ \"message \" : \"Internal server error deserializing Book JSON\"}").build();
				} catch (Exception e) {
					e.printStackTrace();
					return Response.status(500, "{ \"message \" : \"Internal server error\"}").build();
				}
			}
		}
	}
	
	/**
     * @api {get} books/{bookID} Get a single Book
     * @apiName getBook
     * @apiGroup Books
     *
     * @apiUse BadRequestError
     * @apiUse ResourceNotFoundError
     * @apiUse InternalServerError
     * 
     * @apiHeader {String} Accept-Encoding=application/json can be set to application/xml
     * 
     * @apiSuccessExample Success-Response:
     * 	HTTP/1.1 200 OK
{
    "body": {
        "title": "Sisters First",
        "authorId": 0,
        "subjectId": 0,
        "bookId": 0
    },
    "links": {
        "location": "http://localhost:8080/RestExampleAPI/rest/books/0",
        "author": "http://localhost:8080/RestExampleAPI/rest/books/0/author"
    }
}
     * 
     * HTTP/1.1 200 OK
<?xml version="1.0" encoding="UTF-8"?>
<response>
    <book>
        <bookId>0</bookId>
        <authorID>0</authorID>
        <subjectID>0</subjectID>
        <title>Sisters First</title>
    </book>
    <links>
        <location>http://localhost:8080/RestExampleAPI/rest/books/0</location>
        <author>http://localhost:8080/RestExampleAPI/rest/books/0/author</author>
    </links>
</response>
     * 
     * */
	
	@GET
	@Path("/{bookID}")
	public Response getBook(@PathParam("bookID") int bID) {
		Book book = __bService.getBook(bID);
		if (book == null) {
			return Response.status(404, "{ \"message \" : \"No such Book " + bID +  "\"}").build();
		} else {
			try {
				if (_headers.getRequestHeader("Accept-Encoding").get(0).equals(MediaType.APPLICATION_XML)) {
					ResponseBody res = new ResponseBody(true, BookSerializationHelper.getHelper().convertXML(book));
					res.addLinkValue("location", _uriInfo.getAbsolutePath().toString());
					res.addLinkValue("author", _uriInfo.getAbsolutePath().toString() + "/author");
					return Response.status(Response.Status.OK).header("Content-type", 
							"application/xml").entity(res.getResponseString()).build();
				} else {
					ResponseBody res = new ResponseBody(false, BookSerializationHelper.getHelper().convertJSON(book));
					res.addLinkValue("location", _uriInfo.getAbsolutePath().toString());
					res.addLinkValue("author", _uriInfo.getAbsolutePath().toString() + "/author");
					return Response.status(Response.Status.OK).header("Content-type", 
							"application/json").entity(res.getResponseString()).build();
				}
				
			} catch (JsonProcessingException e) {
				e.printStackTrace();
				return Response.status(500, "{ \"message \" : \"Internal server error deserializing Book JSON\"}").build();
			} catch (Exception e) {
				e.printStackTrace();
				return Response.status(500, "{ \"message \" : \"Internal server error\"}").build();
			}
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
     * @apiHeader {String} Accept-Encoding=application/json can be set to application/xml
     * 
     * @apiParamExample [application/json] body
{
  "title": "Boss Ross The Legend",
  "authorId": 0,
  "subjectId": 0
}
     * 
     * @apiSuccessExample Success-Response:
     * 	HTTP/1.1 201 OK
{
    "body": {
        "title": "Boss Ross The Legend",
        "authorId": 0,
        "subjectId": 0,
        "bookId": 4
    },
    "links": {
        "location": "http://localhost:8080/RestExampleAPI/rest/books"
    }
}
     * 
     * HTTP/1.1 201 OK
<?xml version="1.0" encoding="UTF-8"?>
<response>
    <book>
        <bookId>3</bookId>
        <authorID>0</authorID>
        <subjectID>0</subjectID>
        <title>Boss Ross The Legend</title>
    </book>
    <links>
        <location>http://localhost:8080/RestExampleAPI/rest/books</location>
    </links>
</response>
     * 
     * */
	
	@POST
	@Consumes("application/json")
	/*
	 * title authorID subjectID
	 */
	public Response createBook(String json) {
		int bID;
		Book book;
		System.out.println(json);
		try {
			book = BookSerializationHelper.getHelper().consumeJSON(json);
			System.out.println(book);
			 bID = __bService.createBook(book.getTitle(), book.getAuthorId(), book.getSubjectId());
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return Response.status(500, "{ \"message \" : \"Internal server error deserializing Book JSON\"}").build(); //LAB 3 Task 6:
		} catch (IOException e){
			e.printStackTrace();
			return Response.status(500, "{ \"message \" : \"Internal server error\"}").build();
		}
		if (bID == -1) {
			return Response.status(500, "{ \"message \" : Error Creating Book\" \"}").build();
		} else {
			book = __bService.getBook(bID);
			try {
				if (_headers.getRequestHeader("Accept-Encoding").get(0).equals(MediaType.APPLICATION_XML)) {
					ResponseBody res = new ResponseBody(true, BookSerializationHelper.getHelper().convertXML(book));
					res.addLinkValue("location", _uriInfo.getAbsolutePath().toString());
					return Response.status(Response.Status.CREATED).header("Content-type", 
							"application/xml").entity(res.getResponseString()).build();
				} else {
					ResponseBody res = new ResponseBody(false, BookSerializationHelper.getHelper().convertJSON(book));
					res.addLinkValue("location", _uriInfo.getAbsolutePath().toString());
					return Response.status(Response.Status.CREATED).header("Content-type", 
							"application/json").entity(res.getResponseString()).build();
				}
				
			} catch (JsonProcessingException e) {
				e.printStackTrace();
				return Response.status(500, "{ \"message \" : \"Internal server error deserializing Book JSON\"}").build();
			} catch (Exception e) {
				e.printStackTrace();
				return Response.status(500, "{ \"message \" : \"Internal server error\"}").build();
			}
		}
		
	}
	
	/**
     * @api {get} books/{bookID}/author Get an author of a Book
     * @apiName getBookAuthor
     * @apiGroup Books
     *
     * @apiUse BadRequestError
     * @apiUse ResourceNotFoundError
     * @apiUse InternalServerError
     * 
     * @apiHeader {String} Accept-Encoding=application/json can be set to application/xml
     * 
     * @apiSuccessExample Success-Response:
     * 	HTTP/1.1 200 OK
{
    "body": {
        "lastName": "Bush",
        "firstName": "Laura",
        "authorId": 0
    },
    "links": {
        "location": "http://localhost:8080/RestExampleAPI/rest/authors/0"
    }
}
     * 
     * HTTP/1.1 200 OK
<?xml version="1.0" encoding="UTF-8"?>
<response>
    <author>
        <authorId>0</authorId>
        <lastName>Bush</lastName>
        <firstName>Laura</firstName>
    </author>
    <links>
        <location>http://localhost:8080/RestExampleAPI/rest/authors/0</location>
    </links>
</response>
     * 
     * */
	
	@GET
	@Path("/{bookID}/author")
	public Response findAuthorOfBook(@PathParam("bookID") int bID) {
		Book book  = __bService.getBook(bID);
		Author author = null;
		if (book != null) {
			author = __bService.getAuthor(book.getAuthorId());
		} else {
			return Response.status(404, "{ \"message \" : \"No such Book " + bID + "\"}").build();
		}
		if (author != null) {
			try {
				if (_headers.getRequestHeader("Accept-Encoding").get(0).equals(MediaType.APPLICATION_XML)) {
					ResponseBody res = new ResponseBody(true, AuthorSerializationHelper.getHelper().convertXML(author));
					//res.addLinkValue("location", _uriInfo.getAbsolutePath().toString());
					res.addLinkValue("location", _uriInfo.getAbsolutePath().toString().substring(0, _uriInfo.getAbsolutePath().toString().indexOf(_uriInfo.getPath())) + "authors/" + author.getAuthorId());
					return Response.status(Response.Status.OK).header("Content-type", 
							"application/xml").entity(res.getResponseString()).build();
				} else {
					ResponseBody res = new ResponseBody(false, AuthorSerializationHelper.getHelper().convertJSON(author));
					//res.addLinkValue("location", _uriInfo.getAbsolutePath().toString());
					res.addLinkValue("location", _uriInfo.getAbsolutePath().toString().substring(0, _uriInfo.getAbsolutePath().toString().indexOf(_uriInfo.getPath())) + "authors/" + author.getAuthorId());
					return Response.status(Response.Status.OK).header("Content-type", 
							"application/json").entity(res.getResponseString()).build();
				}
				
			} catch (JsonProcessingException e) {
				e.printStackTrace();
				return Response.status(500, "{ \"message \" : \"Internal server error deserializing Author JSON\"}").build();
			} catch (Exception e) {
				e.printStackTrace();
				return Response.status(500, "{ \"message \" : \"Internal server error\"}").build();
			}
		} else {
			return Response.status(404, "{ \"message \" : \"No such Author " + book.getAuthorId() + "\"}").build();
		}
	}
	
	/**
     * @api {delete} books?id={BookID} Delete a single book
     * @apiName deleteBook
     * @apiGroup Books
     *
     *	@apiParam {String} BookID ID number of book to be deleted.
     *
     * @apiUse BadRequestError
     * @apiUse ResourceNotFoundError
     * @apiUse InternalServerError
     * 
     * @apiSuccessExample Success-Response:
     * 	HTTP/1.1 204 OK
     * 
     * */
	
	@DELETE
    public Response deleteBook(@QueryParam("id") int bID) {
		if (__bService.deleteBook(bID)) {
			return Response.status(Response.Status.NO_CONTENT).build();
		} else {
			return Response.status(404, "{ \"message \" : \"No such Book " + bID + "\"}").build();
		}
    }
}
