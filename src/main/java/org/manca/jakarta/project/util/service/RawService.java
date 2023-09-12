package org.manca.jakarta.project.util.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.manca.jakarta.project.model.Category;
import org.manca.jakarta.project.model.Race;
import org.manca.jakarta.project.util.RawAthlete;
import org.manca.jakarta.project.util.RawCategory;
import org.manca.jakarta.project.util.StartList;
import org.manca.jakarta.project.util.StartListSerializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class contains the business logic to manage any operation related to RawAthlete
 */
@ApplicationScoped
public class RawService {
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

    public boolean addRawCategory(long categoryId) {
        // Creates RawCategory Instance to add at a StarList instance
        RawCategory rawCategory = new RawCategory(categoryId);
        try {
            StartList startList = serializer.deserialize(getFileName());
            startList.addRawCategory(rawCategory);
            serializer.serialize(startList, getFileName());
        } catch (IOException | ClassNotFoundException e) {
            //create a new StartList instance
            StartList startList = this.createStartList();
            startList.addRawCategory(rawCategory);
            try {
                serializer.serialize(startList, getFileName());
            } catch (IOException e2) {
                System.out.println("[ERROR]org.manca.jakarta.project.service.RaceService.addAthlete:failed, problems to create new StartList instance.");
                return false;
            }
        }
        return true;
    }
    /**
     * Tries to deserialize a StartList object from file related to a raceId,
     * then look up a specific RawCategoty by its id by  removing it from the
     * StartList, finally serialize the changed StartList instance.
     * @param categoryId The unique category id
     * @return boolean <strong>true</strong> if deserialization operations were successful otherwise <strong>false</strong>
     */
    public boolean removeRawCategory(Long categoryId) {
        boolean checkRemoveAthlete = false;
        try {
            StartList startList = serializer.deserialize(getFileName());
            for (var i=0; i<startList.getRawCategories().size(); i++) {
                RawCategory raw = startList.getRawCategories().get(i);
                if (raw.getIdCategory() == categoryId) {
                    checkRemoveAthlete = true;
                    startList.getRawCategories().remove(i);
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

    /** Implements the code to create StartList instance or retrieve it from serialized StartList objects if it has
     already been serialized being aware that the file in which the StartList instance is serialized is named
     according the rule below:
     file name = "(race-id)__(race-title)__(year-of-race-date).srd" -->  Eg: file name = "101__heroes__cup__2023.srd"
     (if there are  the empty spaces in the race title, replace them with an underscore as in the example above) */
    public boolean addRawAthlete(Long athleteId, Long categoryID, String raceNumber) {
        // Creates RawAthlete Instance to add at a StarList instance
        RawAthlete rawAthlete = new RawAthlete(athleteId, raceNumber, categoryID);
        try {
            StartList startList = serializer.deserialize(getFileName());
            if(raceNumberAlreadyExists(startList.getRawAthletes(), raceNumber)) {
                return false;
            }
            startList.addRawAthlete(rawAthlete);
            serializer.serialize(startList, fileName);

        } catch (IOException | ClassNotFoundException e) {
            //create a new StartList instance
            StartList startList = this.createStartList();
            startList.getRawAthletes().add(rawAthlete);
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
     * This method returns all RawAthlete instances related to start list of currently race.
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

    /**
     * This method returns all RawAthlete instances related to start list of currently race.
     * @return A list of RawCategory instances if any otherwise null.
     */
    public List<RawCategory> findAllRawCategories() {
        try {
            StartList startList = serializer.deserialize(getFileName());
            return startList.getRawCategories();
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }

    /**
     *  This method return an instance of RawCategory in which its unique category id matches the category id passed
     *  as  parameter
     * @param categoryId The category id to compare with RawCategory category id
     * @return An RawCategory instance if category ids match otherwise null.
     */
    public RawCategory findRawCategoryById(Long categoryId) {
        try {
            StartList startList = serializer.deserialize(getFileName());

            for(RawCategory raw : startList.getRawCategories())
                if(raw.getIdCategory() == categoryId.longValue())
                    return raw;

        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
        return null;
    }

    /**
     * This method search a RawAthlete that have the race number equals to the oldNumber parameter and replace it
     * with newNumber parameter.
     * @param oldNumber the race number that will be changed
     * @param newNumber the new race number for RawAthlete
     * @return true if operation was successful otherwise false
     */
    public boolean changeRaceNumber(String oldNumber, String newNumber) {
        try {
            StartList startList = serializer.deserialize(getFileName());

            for (RawAthlete raw : startList.getRawAthletes())
                if(raw.getRaceNumber().equals(oldNumber))
                    raw.setRaceNumber(newNumber);

            serializer.serialize(startList, getFileName());
            return true;

        } catch (IOException | ClassNotFoundException e) {
            return false;
        }
    }
    /**
     * This method search for a RawAthlete by its unique id and replace irs race number with the one passed with
     * the newNumber parameter.
     * @param id the unique RawAthlete's id
     * @param newNumber the new race number for RawAthlete
     * @return true if operation was successful otherwise false
     */
    public boolean changeRaceNumber(Long id, String newNumber) {
        try {
            StartList startList = serializer.deserialize(getFileName());

            for (RawAthlete raw : startList.getRawAthletes())
                if(raw.getIdAthlete() == id)
                    raw.setRaceNumber(newNumber);

            serializer.serialize(startList, getFileName());
            return true;

        } catch (IOException | ClassNotFoundException e) {
            return false;
        }
    }

    private StartList createStartList() {
        //create a new StartList instance
        System.out.println("[ERROR]org.manca.jakarta.project.service.RaceService.addAthlete:problems to retrieve StartList instance, I try to create a new instance...");
        List<RawAthlete> rawAthletes = new ArrayList<>();
        List<RawCategory> rawCategories = new ArrayList<>();
        return new StartList(rawAthletes, rawCategories);
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
}
