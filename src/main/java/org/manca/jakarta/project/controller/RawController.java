package org.manca.jakarta.project.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.manca.jakarta.project.service.RaceService;
import org.manca.jakarta.project.util.RawAthlete;
import org.manca.jakarta.project.util.RawCategory;
import org.manca.jakarta.project.util.service.RawService;

import java.util.List;

@Path(value = "/raw")
public class RawController {

    @Inject
    RaceService raceService;
    @Inject
    RawService rawService;
    @GET
    @Path("/category")
    @Produces(MediaType.APPLICATION_JSON)
    public List<RawAthlete> getRawByCategory(
            @QueryParam("raceId") Long raceId,
            @QueryParam("categoryId") Long categoryId) {
        rawService.setFileName(raceService.makeName(raceId));
        return rawService.findRawAthleteByCategory(categoryId);
    }

    @GET
    @Path("/rnumber")
    @Produces(MediaType.APPLICATION_JSON)
    public RawAthlete getRawByRaceNumber(
            @QueryParam("raceId") Long raceId,
            @QueryParam("raceNumber") String raceNumber ) {
        rawService.setFileName(raceService.makeName(raceId));
        return rawService.findRawAthleteByRaceNumber(raceNumber);
    }

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public List<RawAthlete> getAllRawAthlete(@QueryParam("raceId") Long raceId) {
        rawService.setFileName(raceService.makeName(raceId));
        return rawService.findAllRawAthletes();
    }

    @PUT
    @Path(value = "/change/rn/byrn")
    public boolean changeRaceNumber(
            @QueryParam("raceId") Long raceId,
            @QueryParam("oldNumber") String oldNumber,
            @QueryParam("newNumber") String newNumber) {

        rawService.setFileName(raceService.makeName(raceId));

        return rawService.changeRaceNumber(oldNumber, newNumber);
    }

    @PUT
    @Path(value = "/change/rn/byid")
    public boolean changeRaceNumber(
            @QueryParam("raceId") Long raceId,
            @QueryParam("athleteId") Long athleteId,
            @QueryParam("newNumber") String newNumber) {

        rawService.setFileName(raceService.makeName(raceId));

        return rawService.changeRaceNumber(athleteId, newNumber);
    }

    @GET
    @Path(value = "all/rawcat")
    @Produces(MediaType.APPLICATION_JSON)
    public List<RawCategory> getAllRawCategory(@QueryParam("raceId") Long raceId) {
        rawService.setFileName(raceService.makeName(raceId));
        return rawService.findAllRawCategories();
    }

    @GET
    @Path(value = "category/byid")
    @Produces(MediaType.APPLICATION_JSON)
    public RawCategory getRawCategoryById(
            @QueryParam("raceId") Long raceId,
            @QueryParam("categoryId") Long categoryId) {

        rawService.setFileName(raceService.makeName(raceId));
        return  rawService.findRawCategoryById(categoryId);
    }

}
