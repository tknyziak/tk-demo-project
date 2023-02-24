package io._10a.tkdemo;

import java.util.List;
import javax.inject.Inject;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/")
@Produces(MediaType.TEXT_PLAIN)
@Consumes(MediaType.APPLICATION_JSON)
public class ExampleResource {
	Logger logger = LoggerFactory.getLogger(ExampleResource.class);

	@Inject GreetingController greetingController;


	@GET
	@Path("/hello")
	public String hello() {
		return "Hello RESTEasy!!";
	}

	@GET
	@Path("/echo")
	public String echo(@QueryParam("string") @Pattern(regexp = "[a-z]{2}") String stringToEcho) {
		return stringToEcho;
	}

	@GET
	@Path("/testresponse/{param}")
	public Response reponseErrorTest(@PathParam("param") String param) {

		logger.info("Request arrived, param: {}", param);

		if ("dupa".equalsIgnoreCase(param)) {
			return Response.ok("Wszystko ok!").build();
		} else {
			return Response.status(404).build();
		}
	}

	@GET
	@Path("/exceptionerror/{param}")
	public String exceptionErrorTest(@PathParam("param") String param) {
		if ("dupa".equalsIgnoreCase(param)) {
			return "Wszystko ok!";
		} else {
			throw new NotFoundException();
		}
	}

	@GET
	@Path("/languages/all")
	public List<String> getAllLanguages() {
		return greetingController.getAllLanguages();
	}

	@GET
	@Path("/greeting/{lang}")
	@Produces(MediaType.APPLICATION_JSON)
	public GreetingDTO getGreetingForLanguage(@PathParam("lang") String lang) {
		final GreetingDTO greetingForLang = greetingController.getGreetingForLangWithCriteria(lang);
		if (greetingForLang != null) {
			return greetingForLang;
		} else {
			throw new NotFoundException();
		}
	}

	@PUT
	@Path("/greting")
	public Response createNewGreeting(GreetingDTO greetingDTO) {
		if (greetingController.createNewGreeting(greetingDTO)) {
			return Response.status(201).build();
		} else {
			return Response.status(409).build();
		}
	}

	@POST
	@Path("/greeting/{id}")
	public Greeting updateGreeting(@PathParam("id") Long greetingId, GreetingDTO greetingDTO) {
		return greetingController.updateGreeting(greetingId, greetingDTO);
	}

	@DELETE
	@Path("/greeting/{id}")
	public Response deleteGreeting(@PathParam("id") Long greetingId) {
		boolean deleted =  greetingController.deleteGreeting(greetingId);
		if (deleted) {
			return Response.ok().build();
		} else {
			return Response.status(404).build();
		}
	}
}