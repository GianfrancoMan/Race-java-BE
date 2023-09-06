package org.manca.jakarta.project.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.manca.jakarta.project.model.Athlete;
import org.manca.jakarta.project.model.Category;
import org.manca.jakarta.project.model.Race;
import org.manca.jakarta.project.service.RaceService;
import org.manca.jakarta.project.util.RawAthlete;

import java.util.List;

@Path("/rc")
public class RaceController {
    @Inject
    private RaceService rs;
    @POST
    @Path(value = "/new")
    @Consumes({MediaType.APPLICATION_JSON})
    public Race save(Race race) {
        return rs.saveRace(race);
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
            @QueryParam(value = "athleteId") Long athleteId,
            @QueryParam(value = "categoryId") Long categoryId,
            @QueryParam(value = "raceNumber") String raceNumber) {
        return rs.addAthlete(raceId, athleteId, categoryId, raceNumber);
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

    @PUT
    @Path("/rmv/ctg")
    public boolean removeCategory(
            @QueryParam("raceId") Long raceId,
            @QueryParam("categoryId") Long categoryId) {

        return rs.removeCategoryFromRace(raceId, categoryId);
    }

    @PUT
    @Path("/rmv/athl")
    public boolean removeAthlete(
            @QueryParam("raceId") Long raceId,
            @QueryParam("athleteId") Long athleteId ){

        return rs.removeAthleteFromRace(raceId, athleteId);
    }

    @GET
    @Path("/race/ctgs")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Category> raceCategories(@QueryParam("raceId") Long raceId) {
        return rs.getAllRaceCategories(raceId);
    }

    @GET
    @Path("/race/athls")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Athlete> raceAthletes(@QueryParam("raceId") Long raceId) {
        return rs.getAllRaceAthletes(raceId);
    }

    @GET
    @Path("raw/category")
    @Produces(MediaType.APPLICATION_JSON)
    public List<RawAthlete> getRawByCategory(
            @QueryParam("raceId") Long raceId,
            @QueryParam("categoryId") Long categoryId) {
        return rs.findRawAthleteByCategory(raceId,categoryId);
    }

    @GET
    @Path("raw/rnumber")
    @Produces(MediaType.APPLICATION_JSON)
    public RawAthlete getRawByRaceNumber(
            @QueryParam("raceId") Long raceId,
            @QueryParam("raceNumber") String raceNumber ) {
        return rs.findRawAthleteByRaceNumber(raceId,raceNumber);
    }

}
