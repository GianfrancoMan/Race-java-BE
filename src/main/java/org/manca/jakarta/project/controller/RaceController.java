package org.manca.jakarta.project.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.manca.jakarta.project.model.Athlete;
import org.manca.jakarta.project.model.Race;
import org.manca.jakarta.project.service.RaceService;
import java.util.List;

@Path("/rc")
public class RaceController {
    @Inject
    private RaceService rs;
    @POST
    @Path(value = "/new")
    @Consumes({MediaType.APPLICATION_JSON})
    public Race save(Race race) {
        return rs.savaRace(race);
    }

    @GET
    @Path(value = "/all")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Race> findAll(@QueryParam(value = "id") Long id) {
        return rs.findAllRaces();
    }

    @GET
    @Path(value = "/race")
    public Race findById(@QueryParam(value = "id") Long id) {
        return rs.findRaceById(id);
    }

    @POST
    @Path("/add/athl")
    public boolean addAthlete(
            @QueryParam(value = "raceId") Long raceId,
            @QueryParam(value = "athleteId") Long athleteId) {
        return rs.addAthlete(raceId, athleteId);
    }

    @POST
    @Path("/add/ctg")
    public boolean addCategory(
            @QueryParam(value = "raceId") Long raceId,
            @QueryParam(value = "categoryId") Long categoryId) {
        return rs.addCategory(raceId, categoryId);
    }

    @PUT
    @Path("/update")
    @Consumes(value = MediaType.APPLICATION_JSON)
    public Race update(Race race) {
        return rs.updateRace(race);
    }
}
