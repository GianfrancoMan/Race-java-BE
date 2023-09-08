package org.manca.jakarta.project.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.manca.jakarta.project.service.RaceService;
import org.manca.jakarta.project.util.RawAthlete;
import org.manca.jakarta.project.util.service.RawAthleteService;

import java.util.List;

@Path(value = "/raw")
public class RawAthleteController {

    @Inject
    RaceService raceService;
    @Inject
    RawAthleteService rawAthleteService;
    @GET
    @Path("/category")
    @Produces(MediaType.APPLICATION_JSON)
    public List<RawAthlete> getRawByCategory(
            @QueryParam("raceId") Long raceId,
            @QueryParam("categoryId") Long categoryId) {
        rawAthleteService.setFileName(raceService.makeName(raceId));
        return rawAthleteService.findRawAthleteByCategory(categoryId);
    }

    @GET
    @Path("/rnumber")
    @Produces(MediaType.APPLICATION_JSON)
    public RawAthlete getRawByRaceNumber(
            @QueryParam("raceId") Long raceId,
            @QueryParam("raceNumber") String raceNumber ) {
        rawAthleteService.setFileName(raceService.makeName(raceId));
        return rawAthleteService.findRawAthleteByRaceNumber(raceNumber);
    }

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public List<RawAthlete> getAllRawAthlete(@QueryParam("raceId") Long raceId) {
        rawAthleteService.setFileName(raceService.makeName(raceId));
        return rawAthleteService.findAllRawAthletes();
    }

    @PUT
    @Path(value = "/change/rn/byrn")
    public boolean changeRaceNumber(
            @QueryParam("raceId") Long raceId,
            @QueryParam("oldNumber") String oldNumber,
            @QueryParam("newNumber") String newNumber) {

        rawAthleteService.setFileName(raceService.makeName(raceId));

        return rawAthleteService.changeRaceNumber(oldNumber, newNumber);
    }

    @PUT
    @Path(value = "/change/rn/byid")
    public boolean changeRaceNumber(
            @QueryParam("raceId") Long raceId,
            @QueryParam("athleteId") Long athleteId,
            @QueryParam("newNumber") String newNumber) {

        rawAthleteService.setFileName(raceService.makeName(raceId));

        return rawAthleteService.changeRaceNumber(athleteId, newNumber);
    }

}
