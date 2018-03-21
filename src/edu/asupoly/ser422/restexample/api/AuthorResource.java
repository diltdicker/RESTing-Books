package edu.asupoly.ser422.restexample.api;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
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
import edu.asupoly.ser422.restexample.services.BooktownService;
import edu.asupoly.ser422.restexample.services.BooktownServiceFactory;

@Path("/authors")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class AuthorResource {
	private static BooktownService __bService = BooktownServiceFactory.getInstance();
	
	// Technique for location header taken from
	// http://usna86-techbits.blogspot.com/2013/02/how-to-return-location-header-from.html
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
     * @api {get} authors Get list of Authors
     * @apiName getAuthors
     * @apiGroup Authors
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
                "lastName": "Bush",
                "firstName": "Laura",
                "authorId": 0
            },
            "links": {
                "location": "http://localhost:8080/RestExampleAPI/rest/authors/0"
            }
        },
        {
            "body": {
                "lastName": "Clinton",
                "firstName": "Hillary",
                "authorId": 1
            },
            "links": {
                "location": "http://localhost:8080/RestExampleAPI/rest/authors/1"
            }
        },
        {
            "body": {
                "lastName": "Kennedy",
                "firstName": "Jackie",
                "authorId": 2
            },
            "links": {
                "location": "http://localhost:8080/RestExampleAPI/rest/authors/2"
            }
        }
    ],
    "links": {
        "location": "http://localhost:8080/RestExampleAPI/rest/authors"
    }
}

     *
     * HTTP/1.1 200 OK
<?xml version="1.0" encoding="UTF-8"?>
<response>
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
    <response>
        <author>
            <authorId>1</authorId>
            <lastName>Clinton</lastName>
            <firstName>Hillary</firstName>
        </author>
        <links>
            <location>http://localhost:8080/RestExampleAPI/rest/authors/1</location>
        </links>
    </response>
    <response>
        <author>
            <authorId>2</authorId>
            <lastName>Kennedy</lastName>
            <firstName>Jackie</firstName>
        </author>
        <links>
            <location>http://localhost:8080/RestExampleAPI/rest/authors/2</location>
        </links>
    </response>
    <links>
        <location>http://localhost:8080/RestExampleAPI/rest/authors</location>
    </links>
</response>
     * 
     * */
	
	@GET 
	public Response getAuthors() {
		List<Author> authors = __bService.getAuthors();
		if (authors == null || authors.size() == 0) {
			return Response.status(404, "{ \"message \" : \"No Authors" + "\"}").build();
		} else {
			if (_headers.getRequestHeader("Accept-Encoding").get(0).equals(MediaType.APPLICATION_XML)) {
				String resBody = "";
				for (int i = 0; i < authors.size(); i++) {
					String tmp = AuthorSerializationHelper.getHelper().convertXML(authors.get(i));
					ResponseBody partial = new ResponseBody(true, tmp);
					partial.addLinkValue("location", _uriInfo.getAbsolutePath().toString() + "/" + authors.get(i).getAuthorId());
					resBody += partial.getPartialRepsonseString();
				}
				ResponseBody res = new ResponseBody(true, resBody);
				res.addLinkValue("location", _uriInfo.getAbsolutePath().toString());
				return Response.status(Response.Status.OK).header("Content-type", 
						"application/xml").entity(res.getResponseString()).build();
			} else {
				try {
				String resBody = "";
				for (int i = 0; i < authors.size(); i++) {
					String tmp = AuthorSerializationHelper.getHelper().convertJSON(authors.get(i));
					ResponseBody partial = new ResponseBody(false, tmp);
					partial.addLinkValue("location", _uriInfo.getAbsolutePath().toString() + "/" + authors.get(i).getAuthorId());
					resBody += partial.getPartialRepsonseString();
					if (i != authors.size() - 1) {
						resBody += ", ";
					}
				}
				ResponseBody res = new ResponseBody(false, ResponseBody.getPartialArray(resBody));
				res.addLinkValue("location", _uriInfo.getAbsolutePath().toString());
				return Response.status(Response.Status.OK).header("Content-type", 
						"application/json").entity(res.getResponseString()).build();
				} catch (JsonProcessingException e) {
					e.printStackTrace();
					return Response.status(500, "{ \"message \" : \"Internal server error deserializing Author JSON\"}").build();
				} catch (Exception e) {
					e.printStackTrace();
					return Response.status(500, "{ \"message \" : \"Internal server error\"}").build();
				}
			}
		}
	}

	/* This is the first version of GET we did, using defaults and letting Jersey internally serialize
	 @GET
	@Path("/{authorId}")
	public Author getAuthor(@PathParam("authorId") int aid) {
		return __bService.getAuthor(aid);
	}
	 */
	/* 
	 * This is a second version - it uses Jackson's default mapping via ObjectMapper, which spits out
	 * the same JSON as Jersey's internal version, so the output will look the same as version 1 when you run
	 */
	
	/**
     * @api {get} authors/{authorID} Get a single Author
     * @apiName getAuthor
     * @apiGroup Authors
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
     *	HTTP/1.1 200 OK
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
	
	@GET//@Produces({MediaType.APPLICATION_JSON})
	@Path("/{authorId}")
	public Response getAuthor(@PathParam("authorId") int aid) {
		Author author = __bService.getAuthor(aid);
		if (author == null) {
			return Response.status(404, "{ \"message \" : \"No such Author " + aid +  "\"}").build();
		} else {
			try {
				if (_headers.getRequestHeader("Accept-Encoding").get(0).equals(MediaType.APPLICATION_XML)) {
					String resBody = "";
					ResponseBody res = new ResponseBody(true, AuthorSerializationHelper.getHelper().convertXML(author));
					res.addLinkValue("location", _uriInfo.getAbsolutePath().toString());
					return Response.status(Response.Status.OK).header("Content-type", 
							"application/xml").entity(res.getResponseString()).build();
				} else {
					String resBody = "";
					ResponseBody res = new ResponseBody(false, AuthorSerializationHelper.getHelper().convertJSON(author));
					res.addLinkValue("location", _uriInfo.getAbsolutePath().toString());
					return Response.status(Response.Status.OK).header("Content-type", 
							"application/json").entity(res.getResponseString()).build();
				}
			} catch (JsonProcessingException e) {
				e.printStackTrace();
				return Response.status(500, "{ \"message \" : \"Internal server error deserializing Author JSON\"}").build();	//LAB 3 Task 6:
			} catch (Exception e) {
				e.printStackTrace();
				return Response.status(500, "{ \"message \" : \"Internal server error\"}").build();
			}
		}
	}
	 
	// This is a 3rd version using a custom serializer I've encapsulated over in the new helper class
	/*@GET 
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/{authorId}")
	public Response getAuthor(@PathParam("authorId") int aid) {
		Author author = __bService.getAuthor(aid);
		
		// AuthorSerializationHelper will build a slightly different JSON string and we still use
		// the ResponseBuilder to use that. The key property names are changed in the result.
		try {
			String aString = AuthorSerializationHelper.getHelper().generateJSON(author);
			return Response.status(Response.Status.OK).entity(aString).build();
		} catch (Exception exc) {
			exc.printStackTrace();
			return null;
		}
	}*/
	/* This was the first version of POST we did 
	@POST
	@Consumes("text/plain")
    public int createAuthor(String name) {
		String[] names = name.split(" ");
		// not handled - what if this returns -1?
		int aid = __bService.createAuthor(names[0], names[1]);
		return aid;
    }
    */
	/*
	 * This was the second version that added simple custom response headers and payload
	 */
	/**
     * @api {post} authors/ Create a new Author
     * @apiName postAuthors
     * @apiGroup Authors
     *
     * @apiUse BadRequestError
     * @apiUse InternalServerError
     * 
     * @apiParamExample [text/plain] body
     * 		Bob Ross
     * 
     * @apiHeader {String} Accept-Encoding=application/json can be set to application/xml
     * 
     * @apiSuccessExample Success-Response:
     * 	HTTP/1.1 201 OK
{
    "body": {
        "lastName": "Bob",
        "firstName": "Ross",
        "authorId": 4
    },
    "links": {
        "location": "http://localhost:8080/RestExampleAPI/rest/authors/4"
    }
}
     * 
     * HTTP/1.1 201 OK
<?xml version="1.0" encoding="UTF-8"?>
<response>
    <author>
        <authorId>3</authorId>
        <lastName>Bob</lastName>
        <firstName>Ross</firstName>
    </author>
    <links>
        <location>http://localhost:8080/RestExampleAPI/rest/authors/3</location>
    </links>
</response>
     * 
     * */
	
	@POST
	@Consumes("text/plain")
    public Response createAuthor(String name) {
		System.out.println("input:" + name);
		String[] names = name.split(" ");
		if (names.length < 2) {
			return Response.status(400).entity("{ \" Insufficent parameters \"}").build();
		}
		int aid = __bService.createAuthor(names[0], names[1]);
		if (aid == -1) {
			return Response.status(500).entity("{ \" Error Creating Author \"}").build();
		} else {
			Author author = __bService.getAuthor(aid);
			try {
				if (_headers.getRequestHeader("Accept-Encoding").get(0).equals(MediaType.APPLICATION_XML)) {
					String resBody = "";
					ResponseBody res = new ResponseBody(true, AuthorSerializationHelper.getHelper().convertXML(author));
					res.addLinkValue("location", _uriInfo.getAbsolutePath().toString() + "/" + author.getAuthorId());
					return Response.status(Response.Status.CREATED).header("Content-type", 
							"application/xml").entity(res.getResponseString()).build();
				} else {
					String resBody = "";
					ResponseBody res = new ResponseBody(false, AuthorSerializationHelper.getHelper().convertJSON(author));
					res.addLinkValue("location", _uriInfo.getAbsolutePath().toString() + "/" + author.getAuthorId());
					return Response.status(Response.Status.CREATED).header("Content-type", 
							"application/json").entity(res.getResponseString()).build();
				}
			} catch (JsonProcessingException e) {
				e.printStackTrace();
				return Response.status(500, "{ \"message \" : \"Internal server error deserializing Author JSON\"}").build();
			} catch (Exception e) {
				e.printStackTrace();
				return Response.status(500, "{ \"message \" : \"Internal server error\"}").build();
			}
		}
    }
	
	/*
	 * This is the original PUT method that consumed the default JSON Jersey produces. Would work with the
	 * JSON produced by getAuthor versions 1 and 2 above, but not version 33
	@PUT
	@Consumes("application/json")
    public Response updateAuthor(Author a) {
		if (__bService.updateAuthor(a)) {
			return Response.status(201).entity("{ \"Author\" : \"" + a.getAuthorId() + "\"}").build();
		} else {
			return Response.status(404, "{ \"message \" : \"No such Author " + a.getAuthorId() + "\"}").build();
		}
    }
    */
	/*
	 * This 2nd version of PUT uses the deserializer from AuthorSerializationHelper, and process the JSON given
	 * in GET version 3 above. Note that when you use the custom serializer/deserializer, it will not be 
	 * compatible with methods that do not use it (which will continue to use the Jersey default). If you
	 * decide to customize, then you should be certain to use your (de)serializer throughout your resource!
	 */
	/**
     * @api {put} authors Update a single Author
     * @apiName putAuthor
     * @apiGroup Authors
     *
     * @apiUse BadRequestError
     * @apiUse ResourceNotFoundError
     * @apiUse InternalServerError
     * 
     * @apiHeader {String} Accept-Encoding=application/json can be set to application/xml
     * 
     * @apiParamExample [application/json] body
{
  "lastName": "BB",
  "firstName": "LL",
  "authorId": 0
}
     * 
     * @apiSuccessExample Success-Response:
     * 	HTTP/1.1 201 OK
{
    "body": {
        "lastName": "BB",
        "firstName": "LL",
        "authorId": 0
    },
    "links": {
        "location": "http://localhost:8080/RestExampleAPI/rest/authors"
    }
}
     * 
     * HTTP/1.1 201 OK
<?xml version="1.0" encoding="UTF-8"?>
<response>
    <author>
        <authorId>0</authorId>
        <lastName>BB</lastName>
        <firstName>LL</firstName>
    </author>
    <links>
        <location>http://localhost:8080/RestExampleAPI/rest/authors</location>
    </links>
</response>
     * 
     * */
	
	@PUT
	@Consumes("application/json")
    public Response updateAuthor(String json) {
		System.out.println(json);
		try {
			Author a = AuthorSerializationHelper.getHelper().consumeJSON(json);
			System.out.println(a);
			if (__bService.updateAuthor(a)) {
				// In the response payload it would still use Jackson's default serializer,
				// so we directly invoke our serializer so the PUT payload reflects what it should.
				//String aString = AuthorSerializationHelper.getHelper().generateJSON(a);
				//return Response.status(201).entity(aString).build();
				Author author = __bService.getAuthor(a.getAuthorId());
				if (_headers.getRequestHeader("Accept-Encoding").get(0).equals(MediaType.APPLICATION_XML)) {
					String resBody = "";
					ResponseBody res = new ResponseBody(true, AuthorSerializationHelper.getHelper().convertXML(author));
					res.addLinkValue("location", _uriInfo.getAbsolutePath().toString());
					return Response.status(Response.Status.CREATED).header("Content-type", 
							"application/xml").entity(res.getResponseString()).build();
				} else {
					String resBody = "";
					ResponseBody res = new ResponseBody(false, AuthorSerializationHelper.getHelper().convertJSON(author));
					res.addLinkValue("location", _uriInfo.getAbsolutePath().toString());
					return Response.status(Response.Status.CREATED).header("Content-type", 
							"application/json").entity(res.getResponseString()).build();
				}
			} else {
				return Response.status(404, "{ \"message \" : \"No such Author " + a.getAuthorId() + "\"}").build();
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return Response.status(500, "{ \"message \" : \"Internal server error deserializing Author JSON\"}").build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(500, "{ \"message \" : \"Internal server error\"}").build();
		}
    }
	
	/**
     * @api {delete} authors?id={AuthorID} Delete a single Author
     * @apiName deleteAuthor
     * @apiGroup Authors
     * 
     * @apiParam {String} AuthorID ID of author to be deleted.
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
    public Response deleteAuthor(@QueryParam("id") int aid) {
		if (__bService.deleteAuthor(aid)) {
			return Response.status(204).build();
		} else {
			return Response.status(404, "{ \"message \" : \"No such Author " + aid + "\"}").build();
		}
    }
	/*
	@PATCH
	public Response patchAuthor(@QueryParam("id") int aid) {
		return Response.status(405, "{ \"message \" : \"PATCH not supported\"}").build();
    }
    */
	
	/**
     * @api {get} authors/subject?location={location} Get a list of Authors based on Subject location
     * @apiName getAuthorSubject
     * @apiGroup Authors
     * 
     * @apiParam {String} location Location substring of a Subject.
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
                "lastName": "Bush",
                "firstName": "Laura",
                "authorId": 0
            },
            "links": {
                "location": "http://localhost:8080/RestExampleAPI/rest/authors/subject/0"
            }
        }
    ],
    "links": {
        "location": "http://localhost:8080/RestExampleAPI/rest/authors/subject"
    }
}
     * 
     * HTTP/1.1 200 OK
<?xml version="1.0" encoding="UTF-8"?>
<response>
    <response>
        <author>
            <authorId>0</authorId>
            <lastName>Bush</lastName>
            <firstName>Laura</firstName>
        </author>
        <links>
            <location>http://localhost:8080/RestExampleAPI/rest/authors/subject/0</location>
        </links>
    </response>
    <links>
        <location>http://localhost:8080/RestExampleAPI/rest/authors/subject</location>
    </links>
</response>
     * 
     * */
	
	@GET
	@Path("/subject")
	public Response getAuthorsBySubject(@QueryParam("location") String location) {
		List<Author> authors = __bService.getAuthorsBySubject(location);
		if (authors == null || authors.size() == 0 ) {
			return Response.status(404, "{ \"message \" : \"No such Authors" + "\"}").build();
		} else {
			if (_headers.getRequestHeader("Accept-Encoding").get(0).equals(MediaType.APPLICATION_XML)) {
				String resBody = "";
				for (int i = 0; i < authors.size(); i++) {
					String tmp = AuthorSerializationHelper.getHelper().convertXML(authors.get(i));
					ResponseBody partial = new ResponseBody(true, tmp);
					partial.addLinkValue("location", _uriInfo.getAbsolutePath().toString() + "/" + authors.get(i).getAuthorId());
					resBody += partial.getPartialRepsonseString();
				}
				ResponseBody res = new ResponseBody(true, resBody);
				res.addLinkValue("location", _uriInfo.getAbsolutePath().toString());
				return Response.status(Response.Status.OK).header("Content-type", 
						"application/xml").entity(res.getResponseString()).build();
			} else {
				try {
				String resBody = "";
				for (int i = 0; i < authors.size(); i++) {
					String tmp = AuthorSerializationHelper.getHelper().convertJSON(authors.get(i));
					ResponseBody partial = new ResponseBody(false, tmp);
					partial.addLinkValue("location", _uriInfo.getAbsolutePath().toString() + "/" + authors.get(i).getAuthorId());
					resBody += partial.getPartialRepsonseString();
					if (i != authors.size() - 1) {
						resBody += ", ";
					}
				}
				ResponseBody res = new ResponseBody(false, ResponseBody.getPartialArray(resBody));
				//ResponseBody res = new ResponseBody(false, AuthorSerializationHelper.getHelper().outputListJSON(authors).toString());
				res.addLinkValue("location", _uriInfo.getAbsolutePath().toString());
				return Response.status(Response.Status.OK).header("Content-type", 
						"application/json").entity(res.getResponseString()).build();
				} catch (JsonProcessingException e) {
					e.printStackTrace();
					return Response.status(500, "{ \"message \" : \"Internal server error deserializing Author JSON\"}").build();
				} catch (Exception e) {
					e.printStackTrace();
					return Response.status(500, "{ \"message \" : \"Internal server error\"}").build();
				}
			}
		}
	}
	
}
