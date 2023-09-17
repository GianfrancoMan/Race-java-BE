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

	/**
	 * Retrieve all the athlete stored from the database
	 * @return a List of Athlete instances
	 */
	@GET
	@Path(value = "/all")
	@Produces({ MediaType.APPLICATION_JSON })
	public List<Athlete> getAll() {
			return as.findAllAthletes();
	}

	/**
	 * Persists the entity Athlete Instance passed as parameter.
	 * @param athlete the entity Athlete instance.
	 * @return true if operation successful otherwise false
	 */
	@POST
	@Path(value = "/new")
	@Consumes({ MediaType.APPLICATION_JSON })
	public boolean save(Athlete athlete) {
		return as.saveAthlete(athlete);
	}

	/**
	 * Replaces data of an entity Athlete instance in the database.
	 * @param athlete the entity Athlete instance with changed data.
	 * @return true if operation was successful otherwise false.
	 */
	@POST
	@Path(value = "/update")
	@Consumes({ MediaType.APPLICATION_JSON })
	public boolean update(Athlete athlete) {
		return as.updateAthlete(athlete);
	}

	/**
	 * Deletes the Athlete from the database using its unique id.
	 * @param id the unique id.
	 * @return true if operation was successful otherwise false.
	 */
	@DELETE
	@Path(value = "/delete")
	public boolean delete(@QueryParam("id") long id) {
		return as.deleteAthlete(id);
	}

	/**
	 * Retrieve a specific Athlete using its unique id.
	 * @param id the unique Athlete id.
	 * @return an Athlete instance if operation successful else null.
	 */
	@GET
	@Path(value = "/athlete")
	public Athlete getById(@QueryParam("id") long id) {
		return as.findAthlete(id);
	}

	/**
	 * find all athletes that hava the birthdate equal to the 'birthdate' passed as parameter in the form of a string.
	 * @param birthDate the birthdate string of an Athlete .
	 * @return a List of Athlete objects if operation was successful otherwise null.
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
	 * @param subName the substring to compare.
	 * @param pageNumber the page number.
	 * @param pageSize the page size.
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