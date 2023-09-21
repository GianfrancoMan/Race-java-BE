package org.manca.jakarta.project.util.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.manca.jakarta.project.model.Category;
import org.manca.jakarta.project.model.Race;
import org.manca.jakarta.project.service.RaceService;
import org.manca.jakarta.project.util.RawAthlete;
import org.manca.jakarta.project.util.RawCategory;
import org.manca.jakarta.project.util.StartList;
import org.manca.jakarta.project.util.StartListSerializer;

import java.io.IOException;
import java.time.LocalTime;

/**
 * This class models the behavior of a race officer.
 */
@ApplicationScoped
public class Officer {

    @Inject
    RaceService raceService;
    @Inject
    RawService rawService;
    @Inject
    StartListSerializer serializer;

    /**
     * Try to set the race start time at the current time, for the categories that have the unique id equal to one
     * of the ids passed as parameter.
     * @param raceId the unique id of the race
     * @param categoryIds the unique ids of the categories to set the race start time
     * @return true if operation was successful otherwise false.
     */
    public boolean startRace(Long raceId, Long... categoryIds) {
        if (raceService.entityExists(Race.class.getSimpleName(), raceId)
            && this.checkIds(categoryIds)) { //...end check

            StartList startList = this.startList(raceId);

            /*checks if ids passed as parameters are valid...*/
            if (startList != null && !checkRawIds(startList, categoryIds))
                return false;//... end check.

            this.setStartTimeByIds(startList, categoryIds);
            return true;
        }

        return  false;
    }

    /**
     * It marks the time in which the athlete who has the bib number equal to the race number passed as a parameter
     * of the method completes a lap, furthermore if the athlete has completed all laps he had to run, his state is set
     * to false, returns the total count of the laps he has completed so far.
     *
     * @param raceId the unique race id of a Race.
     * @param raceNumber the unique race number of an Athlete.
     * @return the total count of the laps completed up to that moment or -1 if something was wrong.
     */
    public int marksAthletePassage(Long raceId, String raceNumber) {

        if (raceService.entityExists(Race.class.getSimpleName(), raceId)) {

            StartList startList = this.startList(raceId);

            if(startList != null) {
                for (RawAthlete raw : startList.getRawAthletes()) {

                    if(raw.getRaceNumber().equals(raceNumber)) {
                        raw.addTimeOnLaps(LocalTime.now());

                        //if the athlete has completed all laps that he had to run, his state is set equal to false.
                        for(RawCategory rawCategory : startList.getRawCategories()) {
                            if (rawCategory.getIdCategory().equals(raw.getIdCategory())) {
                                if (raw.getTimeOnLaps().size() == rawCategory.getLapsNumber())
                                    raw.setState(false);
                            }
                        }

                        return raw.getTimeOnLaps().size();
                    }
                }
            }
        }

        return -1;
    }


    //******** PRIVATE METHODS *************//

    /**
     * Retrieves the StartList instance related to Race that have the unique id equal to the race id passed as parameter.
     * @param raceId the unique id of the race related to the start list.
     * @return a StartList instance if exists otherwise null.
     */
    private StartList startList(Long raceId) {
        rawService.setFileName(raceService.makeName(raceId));

        StartList startList = null;
        try {
            startList = serializer.deserialize(rawService.getFileName());
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }

        return startList;
    }
    /**
     * checks whether the all category ids passed as parameters match a respective Category entity in the database.
     * @param categoryIds the ids to check.
     * @return true only all ids match otherwise null.
     */
    private boolean checkIds(Long... categoryIds) {
        for (var id : categoryIds) {
            if(!raceService.entityExists(Category.class.getSimpleName(), id))
                return false;
        }
        return true;
    }

    /**
     * Checks whether the category ids passed as parameters match the id of a RawCategory instance present
     * in the start list.
     * @param startList The StartList instance that contains RawCategory instances.
     * @param categoryIds The ids to check.
     * @return true only if all ids have a match with a RawCategory's id in the StartList otherwise false.
     */
    private boolean checkRawIds(StartList startList, Long... categoryIds) {
        var count = 0;
        for(var idCategory : categoryIds) {
            for (RawCategory rawCategory : startList.getRawCategories()) {
                if(idCategory.equals(rawCategory.getIdCategory())) {
                    count++;
                    break;
                }
            }
        }

        return count == categoryIds.length;
    }

    /**
     * Set the raceStartTime for the RawCategory instances that have idCategory that match to one of the ids passed as
     * parameters, and set to true, the state for the RawAthletes in the start list related of the aforementioned ids.
     * @param startList The related StartList instance the RawCategory instances to set.
     * @param categoryIds The unique ids of the RawCategory instances affected by the start time setting.
     */
    private void setStartTimeByIds(StartList startList, Long... categoryIds) {
        for(var id : categoryIds) {

            for (var rawCategory : startList.getRawCategories()) {
                if (id.equals(rawCategory.getIdCategory())) {
                    rawCategory.setRaceStartTime(LocalTime.now());
                    break;
                }
            }

            //set state for RawAthlete related to the categories involved
            for (var rawAthlete : startList.getRawAthletes()) {
                if(rawAthlete.getIdCategory() == id) {
                    rawAthlete.setState(true);
                }
            }
        }

        try {
            serializer.serialize(startList, rawService.getFileName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
