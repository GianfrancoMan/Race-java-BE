package org.manca.jakarta.project.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.manca.jakarta.project.model.Athlete;
import org.manca.jakarta.project.service.AthleteService;

import java.time.LocalDate;
import java.util.List;
@Path("/athl")
public class AthleteController {

	/*Only to check if the app configuration is fine and if it  works */
	@Inject
	private AthleteService as;

	/*REST to retrieve all athlete from DB*/
	@GET
	@Path(value = "/all")
	@Produces({ MediaType.APPLICATION_JSON })
	public List<Athlete> getAll() {
			return as.findAllAthletes();
	}

	/*REST to persist a new athlete*/
	@POST
	@Path(value = "/new")
	@Consumes({ MediaType.APPLICATION_JSON })
	public boolean save(Athlete athlete) {
		return as.saveAthlete(athlete);
	}

	/*REST to change athlete data*/
	@POST
	@Path(value = "/update")
	@Consumes({ MediaType.APPLICATION_JSON })
	public boolean update(Athlete athlete) {
		return as.updateAthlete(athlete);
	}

	/*REST to delete an athlete*/
	@DELETE
	@Path(value = "/delete")
	public boolean delete(@QueryParam("id") long id) {
		return as.deleteAthlete(id);
	}

	/*REST to retrieve a specific athlete passing his ID*/
	@GET
	@Path(value = "/athlete")
	public Athlete getById(@QueryParam("id") long id) {
		return as.findAthlete(id);
	}

	/**
	 * find all athletes that hava the birthdate equal to the 'birthdate' passed as parameter in the form of a string
	 * return a List of Athlete objects if success else null.
	 */
	@GET
	@Path(value = "/birthed")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Athlete> getByBirthDate(@QueryParam("birthDate") String birthDate) {
		return as.findAthletesByBirthDate(birthDate);
	}

	/**
	 * Implements pagination to get all athletes of the race that have the string 'subName' passed as parameter
	 * ito their complete name.
	 * Pagination leverage on 'pageNumber' and 'pageSize' passed as parameter
	 * Returns a List of Athletes objects if success else null.
	 */
	@GET
	@Path(value = "sbn")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Athlete> getBySubName(
			@QueryParam(value = "subName") String subName,
			@QueryParam(value = "pageNumber") int pageNumber,
			@QueryParam(value = "pageSize") int pageSize) {
		return as.findAthletesBySubName(subName, pageNumber, pageSize);
	}
}