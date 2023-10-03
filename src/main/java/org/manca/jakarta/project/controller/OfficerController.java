package org.manca.jakarta.project.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import org.manca.jakarta.project.model.util.DataToStart;
import org.manca.jakarta.project.util.service.Officer;

@Path(value = "/officer")
public class OfficerController {
    @Inject
    Officer officer;

    /**
     * Calls the Officer method to set the laps number to run for a specific category of a specif race
     * @param raceId the unique id of the race
     * @param categoryId the unique id of the category
     * @param lapsToRun the number od the  laps
     * @return true if operation was successful otherwise false.
     */
    @PUT
    @Path(value = "/laps")
    public boolean setLapsNumber(
            @QueryParam("raceId") Long raceId,
            @QueryParam("categoryId") Long categoryId,
            @QueryParam("lapsToRun") int lapsToRun) {

        return officer.setCategoryLapsToRun(raceId, categoryId, lapsToRun);
    }

    /**
     * Calls Officer startRace method to manage the race start for one or more categories.
     *
     * @param dataToStart a DataToStart instance that bring the data to start a race like unique race id and unique
     *                    category ids.
     *
     * @return TRUE if operation was successful otherwise FALSE.
     */
    @PUT
    @Path(value = "/start")
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean startRace(DataToStart dataToStart) {
        return officer.startRace(dataToStart.getRaceId(), dataToStart.getCategoryIds());
    }

    /**
     * Calls Officer method to manage the athlete's passage across the finish line.
     *
     * @param raceId the unique id of the affected race
     *
     * @param raceNumber thr unique race number of the athlete
     *
     * @return the total count of the laps completed by the athlete.
     */
    @PUT
    @Path(value = "/mark")
    public int markPassage(
            @QueryParam("raceId") Long raceId,
            @QueryParam("raceNumber") String raceNumber) {

        return officer.marksAthletePassage(raceId, raceNumber);
    }
}