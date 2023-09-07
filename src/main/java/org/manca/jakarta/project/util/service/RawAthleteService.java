package org.manca.jakarta.project.util.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.manca.jakarta.project.service.CategoryService;
import org.manca.jakarta.project.util.RawAthlete;
import org.manca.jakarta.project.util.StartList;
import org.manca.jakarta.project.util.StartListSerializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class contains the business logic to manage any operation related to RawAthlete
 */
@ApplicationScoped
public class RawAthleteService {

    @Inject
    CategoryService categoryService;
    @Inject
    StartListSerializer serializer;
    private String fileName;

    //GETTER & SETTER start
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
    //GETTER & SETTER end

    /** Implements the code to create StartList instance or retrieve it from serialized StartList objects if it has
     already been serialized being aware that the file in which the StartList instance is serialized is named
     according the rule below:
     file name = "(race-id)__(race-title)__(year-of-race-date).srd" -->  Eg: file name = "101__heroes__cup__2023.srd"
     (if there are  the empty spaces in the race title, replace them with an underscore as in the example above) */
    public boolean addRawAthlete(Long raceId, Long athleteId, Long categoryID, String raceNumber) {
        var lapsToRun = categoryService.findCategoryById(categoryID).getLapsNumber();
        // Creates RawAthlete Instance to add at a StarList instance
        RawAthlete rawAthlete = new RawAthlete(athleteId, raceNumber, categoryID, lapsToRun);
        try {
            StartList startList = serializer.deserialize(getFileName());
            if(raceNumberAlreadyExists(startList.getRawAthletes(), raceNumber)) {
                return false;
            }
            startList.addRawAthlete(rawAthlete);
            serializer.serialize(startList, fileName);

        } catch (IOException | ClassNotFoundException e) {
            //create a new StartList instance
            System.out.println("[ERROR]org.manca.jakarta.project.service.RaceService.addAthlete:problems to retrieve StartList instance, I try to create a new instance...");
            List<RawAthlete> rawAthletes = new ArrayList<>();
            rawAthletes.add(rawAthlete);
            StartList startList = new StartList(rawAthletes);
            try {
                serializer.serialize(startList, fileName);
            } catch (IOException e2) {
                e2.printStackTrace();
                System.out.println("[ERROR]org.manca.jakarta.project.service.RaceService.addAthlete:failed, problems to create new StartList instance.");
                return false;
            }
        }
        return true;
    }

    /**
     * Tries to deserialize a StartList object from file related to a raceId,
     * then look up a specific RawAthlete by its id by  removing it from the
     * StartList, finally serialize the changed StartList instance.
     * @param athleteId The unique athlete id
     * @return boolean <strong>true</strong> if deserialization operations were successful otherwise <strong>false</strong>
     */
    public boolean removeRawAthlete(Long athleteId) {
        boolean checkRemoveAthlete = false;
        try {
            StartList startList = serializer.deserialize(getFileName());
            for (RawAthlete rawAthlete : startList.getRawAthletes()) System.out.println(rawAthlete);
            for (var i=0; i<startList.getRawAthletes().size(); i++) {
                RawAthlete raw = startList.getRawAthletes().get(i);
                if (raw.getIdAthlete() == athleteId) {
                    checkRemoveAthlete = true;
                    startList.getRawAthletes().remove(i);
                    serializer.serialize(startList, getFileName());
                    break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("org.manca.jakarta.project.service.RaceService.removeAthleteFromRace: Deserialization failed");
            checkRemoveAthlete = false;
        }
        return checkRemoveAthlete;
    }

    /**
     * Checks if already exists in the race, an athlete that has the same race number passed as parameter.
     * @param rawAthletes The athletes of the race
     * @param raceNumberToCheck The race number to be compared with the race number of the athletes registered for the race
     * @return true if a race number already exists otherwise false.
     */
    private boolean raceNumberAlreadyExists(List<RawAthlete> rawAthletes, String raceNumberToCheck) {
        for(RawAthlete rawAthlete : rawAthletes) {
            if(rawAthlete.getRaceNumber().equals(raceNumberToCheck))
                return true;
        }
        return false;
    }


    /**
     *Return a List of RawAthlete instances belonging to a specific Race that have the 'categoryId' attribute that
     * matches 'categoryId' passed as parameter
     * @param categoryId The unique identifier of a specific category
     * @return a List of RawAthlete instances (will be empty if there are no matches)
     */
    public List<RawAthlete> findRawAthleteByCategory(Long categoryId){
        List<RawAthlete> rawAthletes = new ArrayList<>();
        try {
            StartList startList = serializer.deserialize(getFileName());
            for(RawAthlete rawAthlete : startList.getRawAthletes()) {
                if(rawAthlete.getIdCategory() == categoryId)
                    rawAthletes.add(rawAthlete);
            }
        } catch (IOException | ClassNotFoundException e) {
            return rawAthletes;
        }
        return rawAthletes;
    }

    /**
     * Returns the RawAthlete instance belonging to a specific Race that have the raceNumber attribute that matches 'raceNumber' passed as parameter
     * or null if there are no matches.
     * @param raceNumber the race number of the RawAthlete to fetch
     * @return a RawAthlete instance or null if there are no matches.
     */
    public RawAthlete findRawAthleteByRaceNumber(String raceNumber) {
        try {
            StartList startList = serializer.deserialize(getFileName());
            for (RawAthlete raw : startList.getRawAthletes()) {
                if(raw.getRaceNumber().equals(raceNumber)) {
                    return  raw;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
        return null;
    }

    /**
     * This method returns all RawAthlete instances related to currently race.
     * @return A List of RawAthlete instances.
     */
    public List<RawAthlete>findAllRawAthletes() {
        try {
            StartList startList = serializer.deserialize(getFileName());
            return startList.getRawAthletes();
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }
}
