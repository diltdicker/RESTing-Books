package edu.asupoly.ser422.restexample.api;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.fasterxml.jackson.core.JsonProcessingException;
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
     * @api {get} books Get list of Subjects
     * @apiName getSubjects
     * @apiGroup Subjects
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
                "subject": "Humor",
                "location": "Midland, TX",
                "subjectId": 0
            },
            "links": {
                "location": "http://localhost:8080/RestExampleAPI/rest/subjects/0",
                "books": "http://localhost:8080/RestExampleAPI/rest/subjects/0/books"
            }
        },
        {
            "body": {
                "subject": "Politics",
                "location": "Little Rock, AR",
                "subjectId": 1
            },
            "links": {
                "location": "http://localhost:8080/RestExampleAPI/rest/subjects/1",
                "books": "http://localhost:8080/RestExampleAPI/rest/subjects/1/books"
            }
        },
        {
            "body": {
                "subject": "Drama",
                "location": "Dallas, TX",
                "subjectId": 2
            },
            "links": {
                "location": "http://localhost:8080/RestExampleAPI/rest/subjects/2",
                "books": "http://localhost:8080/RestExampleAPI/rest/subjects/2/books"
            }
        }
    ],
    "links": {
        "location": "http://localhost:8080/RestExampleAPI/rest/subjects"
    }
}
     * 
     * HTTP/1.1 200 OK
<?xml version="1.0" encoding="UTF-8"?>
<response>
    <response>
        <subject>
            <subjectId>0</subjectId>
            <subject>Humor</subject>
            <location>Midland, TX</location>
        </subject>
        <links>
            <location>http://localhost:8080/RestExampleAPI/rest/subjects/0</location>
            <books>http://localhost:8080/RestExampleAPI/rest/subjects/0/books</books>
        </links>
    </response>
    <response>
        <subject>
            <subjectId>1</subjectId>
            <subject>Politics</subject>
            <location>Little Rock, AR</location>
        </subject>
        <links>
            <location>http://localhost:8080/RestExampleAPI/rest/subjects/1</location>
            <books>http://localhost:8080/RestExampleAPI/rest/subjects/1/books</books>
        </links>
    </response>
    <response>
        <subject>
            <subjectId>2</subjectId>
            <subject>Drama</subject>
            <location>Dallas, TX</location>
        </subject>
        <links>
            <location>http://localhost:8080/RestExampleAPI/rest/subjects/2</location>
            <books>http://localhost:8080/RestExampleAPI/rest/subjects/2/books</books>
        </links>
    </response>
    <links>
        <location>http://localhost:8080/RestExampleAPI/rest/subjects</location>
    </links>
</response>
     * 
     * */
	
	@GET
	public Response getSubjects(){
		List<Subject> subjects = __bService.getSubjects();
		if (subjects == null || subjects.size() == 0) {
			return Response.status(404, "{ \"message \" : \"No Subjects " + "\"}").build();
		} else {
			if (_headers.getRequestHeader("Accept-Encoding").get(0).equals(MediaType.APPLICATION_XML)) {
				String resBody = "";
				for (int i = 0; i < subjects.size(); i++) {
					String tmp = SubjectSerializationHelper.getHelper().convertXML(subjects.get(i));
					ResponseBody partial = new ResponseBody(true, tmp);
					partial.addLinkValue("location", _uriInfo.getAbsolutePath().toString() + "/" + subjects.get(i).getSubjectId());
					partial.addLinkValue("books", _uriInfo.getAbsolutePath().toString() + "/" + subjects.get(i).getSubjectId() + "/books");
					resBody += partial.getPartialRepsonseString();
				}
				ResponseBody res = new ResponseBody(true, resBody);
				res.addLinkValue("location", _uriInfo.getAbsolutePath().toString());
				return Response.status(Response.Status.OK).header("Content-type", 
						"application/xml").entity(res.getResponseString()).build();
			} else {
				try {
				String resBody = "";
				for (int i = 0; i < subjects.size(); i++) {
					String tmp = SubjectSerializationHelper.getHelper().convertJSON(subjects.get(i));
					ResponseBody partial = new ResponseBody(false, tmp);
					partial.addLinkValue("location", _uriInfo.getAbsolutePath().toString() + "/" + subjects.get(i).getSubjectId());
					partial.addLinkValue("books", _uriInfo.getAbsolutePath().toString() + "/" + subjects.get(i).getSubjectId() + "/books");
					resBody += partial.getPartialRepsonseString();
					if (i != subjects.size() - 1) {
						resBody += ", ";
					}
				}
				ResponseBody res = new ResponseBody(false, ResponseBody.getPartialArray(resBody));
				res.addLinkValue("location", _uriInfo.getAbsolutePath().toString());
				return Response.status(Response.Status.OK).header("Content-type", 
						"application/json").entity(res.getResponseString()).build();
				} catch (JsonProcessingException e) {
					e.printStackTrace();
					return Response.status(500, "{ \"message \" : \"Internal server error deserializing Subject JSON\"}").build();
				} catch (Exception e) {
					e.printStackTrace();
					return Response.status(500, "{ \"message \" : \"Internal server error\"}").build();
				}
			}
		}
		
	}
	
	/**
     * @api {get} subjects/{subjectID} Get a single Subjects
     * @apiName getSubject
     * @apiGroup Subjects
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
        "subject": "Humor",
        "location": "Midland, TX",
        "subjectId": 0
    },
    "links": {
        "location": "http://localhost:8080/RestExampleAPI/rest/subjects/0",
        "books": "http://localhost:8080/RestExampleAPI/rest/subjects/0/books"
    }
}
     * 
     * HTTP/1.1 200 OK
<?xml version="1.0" encoding="UTF-8"?>
<response>
    <subject>
        <subjectId>0</subjectId>
        <subject>Humor</subject>
        <location>Midland, TX</location>
    </subject>
    <links>
        <location>http://localhost:8080/RestExampleAPI/rest/subjects/0</location>
        <books>http://localhost:8080/RestExampleAPI/rest/subjects/0/books</books>
    </links>
</response>
     * 
     * */
	
	@GET
	@Path("/{subjectID}")
	public Response getSubject(@PathParam("subjectID") int subID) {
		Subject subject = __bService.getSubject(subID);
		if (subject == null) {
			return Response.status(404, "{ \"message \" : \"No such Subject " + subID + "\"}").build();
		} else {
			try {
				if (_headers.getRequestHeader("Accept-Encoding").get(0).equals(MediaType.APPLICATION_XML)) {
					ResponseBody res = new ResponseBody(true, SubjectSerializationHelper.getHelper().convertXML(subject));
					res.addLinkValue("location", _uriInfo.getAbsolutePath().toString());
					res.addLinkValue("books", _uriInfo.getAbsolutePath().toString() + "/books");
					return Response.status(Response.Status.OK).header("Content-type", 
							"application/xml").entity(res.getResponseString()).build();
				} else {
					ResponseBody res = new ResponseBody(false, SubjectSerializationHelper.getHelper().convertJSON(subject));
					res.addLinkValue("location", _uriInfo.getAbsolutePath().toString());
					res.addLinkValue("books", _uriInfo.getAbsolutePath().toString() + "/books");
					return Response.status(Response.Status.OK).header("Content-type", 
							"application/json").entity(res.getResponseString()).build();
				}
				
			} catch (JsonProcessingException e) {
				e.printStackTrace();
				return Response.status(500, "{ \"message \" : \"Internal server error deserializing Subject JSON\"}").build();
			} catch (Exception e) {
				e.printStackTrace();
				return Response.status(500, "{ \"message \" : \"Internal server error\"}").build();
			}
		}
	}
	
	/**
     * @api {get} subjects/{subjectID}/books Get a list books with the same Subject
     * @apiName getSubjectBooks
     * @apiGroup Subjects
     *
     * @apiUse BadRequestError
     * @apiUse ResourceNotFoundError
     * @apiUse InternalServerError
     * 
     * @apiHeader {String} Accept-Encoding=application/json can be set to application/xmll
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
                "location": "http://localhost:8080/RestExampleAPI/rest/books/0"
            }
        }
    ],
    "links": {
        "location": "http://localhost:8080/RestExampleAPI/rest/subjects/0/books"
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
        </links>
    </response>
    <links>
        <location>http://localhost:8080/RestExampleAPI/rest/subjects/0/books</location>
    </links>
</response>
     * 
     * */
	
	@GET
	@Path("/{subjectID}/books")
	public Response findBooksBySubject(@PathParam("subjectID") int subID) {
		Subject subject = __bService.getSubject(subID);
		if (subject == null) {
			return Response.status(404, "{ \"message \" : \"No such Subject " + subID + "\"}").build();
		}
		List<Book> books = __bService.findBooksBySubject(subID);
		if (books == null || books.size() == 0) {
			return Response.status(404, "{ \"message \" : \"No Books " + "\"}").build();
		} else {
			if (_headers.getRequestHeader("Accept-Encoding").get(0).equals(MediaType.APPLICATION_XML)) {
				String resBody = "";
				for (int i = 0; i < books.size(); i++) {
					String tmp = BookSerializationHelper.getHelper().convertXML(books.get(i));
					ResponseBody partial = new ResponseBody(true, tmp);
					partial.addLinkValue("location", _uriInfo.getAbsolutePath().toString().substring(0, _uriInfo.getAbsolutePath().toString().indexOf(_uriInfo.getPath())) + "books/" + books.get(i).getBookId());
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
					partial.addLinkValue("location", _uriInfo.getAbsolutePath().toString().substring(0, _uriInfo.getAbsolutePath().toString().indexOf(_uriInfo.getPath())) + "books/" + books.get(i).getBookId());
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
					return Response.status(500, "{ \"message \" : \"Internal server error deserializing Books JSON\"}").build();
				} catch (Exception e) {
					e.printStackTrace();
					return Response.status(500, "{ \"message \" : \"Internal server error\"}").build();
				}
			}
		}
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
	
	/**
     * @api {put} subjects Update a Subject
     * @apiName putSubject
     * @apiGroup Subjects
     *
     * @apiUse BadRequestError
     * @apiUse ResourceNotFoundError
     * @apiUse InternalServerError
     * 
     * @apiHeader {String} Accept-Encoding=application/json can be set to application/xml
     * 
     * @apiParamExample [application/json] body
{
  "subject": "Comedy",
  "location": "Phoenix, AZ",
  "subjectId": 0
}
     * 
     * @apiSuccessExample Success-Response:
     * 	HTTP/1.1 201 OK
{
    "body": {
        "subject": "Comedy",
        "location": "Phoenix, AZ",
        "subjectId": 0
    },
    "links": {
        "location": "http://localhost:8080/RestExampleAPI/rest/subjects"
    }
}
     * 
     * HTTP/1.1 201 OK
<?xml version="1.0" encoding="UTF-8"?>
<response>
    <subject>
        <subjectId>0</subjectId>
        <subject>Comedy</subject>
        <location>Phoenix, AZ</location>
    </subject>
    <links>
        <location>http://localhost:8080/RestExampleAPI/rest/subjects</location>
    </links>
</response>
     * 
     * */
	
	@PUT
	@Consumes("application/json")
	public Response updateSubject(String json) {
		System.out.println(json);
		try {
			Subject subject = SubjectSerializationHelper.getHelper().consumeJSON(json);
			if (__bService.updateSubject(subject)) {
				subject = __bService.getSubject(subject.getSubjectId());
				if (_headers.getRequestHeader("Accept-Encoding").get(0).equals(MediaType.APPLICATION_XML)) {
					ResponseBody res = new ResponseBody(true, SubjectSerializationHelper.getHelper().convertXML(subject));
					res.addLinkValue("location", _uriInfo.getAbsolutePath().toString());
					return Response.status(Response.Status.CREATED).header("Content-type", 
							"application/xml").entity(res.getResponseString()).build();
				} else {
					ResponseBody res = new ResponseBody(false, SubjectSerializationHelper.getHelper().convertJSON(subject));
					res.addLinkValue("location", _uriInfo.getAbsolutePath().toString());
					return Response.status(Response.Status.CREATED).header("Content-type", 
							"application/json").entity(res.getResponseString()).build();
				}
			} else {
				return Response.status(404, "{ \"message \" : \"No such Subject " + subject.getSubjectId() + "\"}").build();
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return Response.status(500, "{ \"message \" : \"Internal server error deserializing Subject JSON\"}").build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(500, "{ \"message \" : \"Internal server error\"}").build();
		}
	}
	
	/**
     * @api {patch} subjects Update the location of a Subject
     * @apiName patchSubject
     * @apiGroup Subjects
     *
     * @apiUse BadRequestError
     * @apiUse ResourceNotFoundError
     * @apiUse InternalServerError
     * 
     * @apiHeader {String} Accept-Encoding=application/json can be set to application/xml
     * 
     * @apiParamExample [application/json] body
{
  "location": "Phoenix, AZ",
  "subjectId": 0
}
     * 
     * @apiSuccessExample Success-Response:
     * 	HTTP/1.1 201 OK
{
    "body": {
        "subject": "Comedy",
        "location": "Mesa, AZ",
        "subjectId": 0
    },
    "links": {
        "location": "http://localhost:8080/RestExampleAPI/rest/subjects"
    }
}
     * 
     * HTTP/1.1 201 OK
<?xml version="1.0" encoding="UTF-8"?>
<response>
    <subject>
        <subjectId>0</subjectId>
        <subject>Comedy</subject>
        <location>Mesa, AZ</location>
    </subject>
    <links>
        <location>http://localhost:8080/RestExampleAPI/rest/subjects</location>
    </links>
</response>
     * 
     * */
	
	@PATCH
	@Consumes("application/json")
	public Response updateSubjectLocation(String json) {
		System.out.println(json);
		try {
			Subject subject = SubjectSerializationHelper.getHelper().consumeJSON(json);
			Subject tmp = __bService.getSubject(subject.getSubjectId());
			if (tmp != null) {
				subject.setSubject(tmp.getSubject());
			}
			System.out.print(subject);
			if (__bService.updateSubject(subject)) {
				subject = __bService.getSubject(subject.getSubjectId());
				if (_headers.getRequestHeader("Accept-Encoding").get(0).equals(MediaType.APPLICATION_XML)) {
					ResponseBody res = new ResponseBody(true, SubjectSerializationHelper.getHelper().convertXML(subject));
					res.addLinkValue("location", _uriInfo.getAbsolutePath().toString());
					return Response.status(Response.Status.CREATED).header("Content-type", 
							"application/xml").entity(res.getResponseString()).build();
				} else {
					ResponseBody res = new ResponseBody(false, SubjectSerializationHelper.getHelper().convertJSON(subject));
					res.addLinkValue("location", _uriInfo.getAbsolutePath().toString());
					return Response.status(Response.Status.CREATED).header("Content-type", 
							"application/json").entity(res.getResponseString()).build();
				}
			} else {
				return Response.status(404, "{ \"message \" : \"No such Subject " + subject.getSubjectId() + "\"}").build();
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return Response.status(500, "{ \"message \" : \"Internal server error deserializing Subject JSON\"}").build();	//LAB 3 Task 6:
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(500, "{ \"message \" : \"Internal server error\"}").build();
		}
	}
}
