package edu.asupoly.ser422.restexample.api;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
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
	public Response getSubjects(){
		List<Subject> subjects = __bService.getSubjects();
		if (_headers.getRequestHeader("Accept-Encoding").get(0).equals(MediaType.APPLICATION_XML)) {
			return Response.status(Response.Status.OK).header("Content-type", 
					"application/xml").entity(SubjectSerializationHelper.getHelper().outputListXML(subjects)).build();
		} else {
			return Response.status(Response.Status.OK).header("Content-type", 
					"application/json").entity(SubjectSerializationHelper.getHelper().outputListJSON(subjects).toString()).build();
		}
	}
	
	/**
     * @api {get} subjects/{subjectID} Get a single Subjects
     * @apiName getSubject
     * @apiGroup Subjects
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
	@Path("/{subjectID}")
	public Response getSubject(@PathParam("subjectID") int subID) {
		Subject subject = __bService.getSubject(subID);
		try {
			String aString = new ObjectMapper().writeValueAsString(subject);
			//return Response.status(Response.Status.OK).entity(aString).build();
			if (_headers.getRequestHeader("Accept-Encoding").get(0).equals(MediaType.APPLICATION_XML)) {
				return Response.status(Response.Status.OK).header("Content-type", 
						"application/xml").entity(SubjectSerializationHelper.getHelper().convertXML(subject)).build();
			} else {
				return Response.status(Response.Status.OK).header("Content-type", 
						"application/json").entity(aString).build();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
     * @api {get} subjects/{subjectID}/books Get a list books with the same Subject
     * @apiName getSubjectBooks
     * @apiGroup Subjects
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
	@Path("/{subjectID}/books")
	public Response findBooksBySubject(@PathParam("subjectID") int subID) {
		List<Book> books = __bService.findBooksBySubject(subID);
		System.out.println(_headers.getRequestHeader("Accept-Encoding").get(0));
//		if (_headers.getAcceptableMediaTypes().size() > 0) {
//			for (int i = 0; i < _headers.getAcceptableMediaTypes().size(); i++) {
//				System.out.println(_headers.toString());
//			}
//		}
		if (_headers.getRequestHeader("Accept-Encoding").get(0).equals(MediaType.APPLICATION_XML)) {
			return Response.status(Response.Status.OK).header("Content-type", 
					"application/xml").entity(BookSerializationHelper.getHelper().outputListXML(books)).build();
		} else {
			return Response.status(Response.Status.OK).header("Content-type", 
					"application/json").entity(BookSerializationHelper.getHelper().outputListJSON(books).toString()).build();
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
	
	@PUT
	@Consumes("application/json")
	public Response updateSubject(String json) {
		return null;
	}
}
