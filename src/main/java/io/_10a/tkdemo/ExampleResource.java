package io._10a.tkdemo;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
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
public class ExampleResource {

	Logger logger = LoggerFactory.getLogger(ExampleResource.class);

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
}