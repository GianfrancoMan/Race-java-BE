package org.manca.jakarta.project.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.manca.jakarta.project.model.Athlete;
import org.manca.jakarta.project.service.AthleteService;

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
}