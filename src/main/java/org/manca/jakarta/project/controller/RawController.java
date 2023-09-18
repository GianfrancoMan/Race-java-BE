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
        return rawService.findRawAthleteByCategory(raceId, categoryId);
    }

    @GET
    @Path("/rnumber")
    @Produces(MediaType.APPLICATION_JSON)
    public RawAthlete getRawByRaceNumber(
            @QueryParam("raceId") Long raceId,
            @QueryParam("raceNumber") String raceNumber ) {
        return rawService.findRawAthleteByRaceNumber(raceId, raceNumber);
    }

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public List<RawAthlete> getAllRawAthlete(@QueryParam("raceId") Long raceId) {
        return rawService.findAllRawAthletes(raceId);
    }

    @PUT
    @Path(value = "/update/raw_athl")
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean updateRawAthlete(@QueryParam("raceId") Long raceId,  RawAthlete rawAthlete) {
        return  rawService.updateRawAthlete(raceId, rawAthlete);
    }


    @PUT
    @Path(value = "/update/raw_cat")
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean updateRawAthlete(@QueryParam("raceId") Long raceId,  RawCategory rawCategory) {
        return  rawService.updateRawCategory(raceId, rawCategory);
    }

    @GET
    @Path(value = "all/rawcat")
    @Produces(MediaType.APPLICATION_JSON)
    public List<RawCategory> getAllRawCategory(@QueryParam("raceId") Long raceId) {
        return rawService.findAllRawCategories(raceId);
    }

    @GET
    @Path(value = "category/byid")
    @Produces(MediaType.APPLICATION_JSON)
    public RawCategory getRawCategoryById(
            @QueryParam("raceId") Long raceId,
            @QueryParam("categoryId") Long categoryId) {
        return  rawService.findRawCategoryById(raceId, categoryId);
    }
}
