package org.manca.jakarta.project.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import org.manca.jakarta.project.dao.RaceDao;
import org.manca.jakarta.project.model.Athlete;
import org.manca.jakarta.project.model.Category;
import org.manca.jakarta.project.model.Race;
import org.manca.jakarta.project.util.RawAthlete;
import org.manca.jakarta.project.util.StartList;
import org.manca.jakarta.project.util.StartListSerializer;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Stateless
public class RaceService {
    @Inject
    private RaceDao rd;
    @Inject
    CategoryService categoryService;
    @Inject
    StartListSerializer serializer;

    public Race saveRace(Race race) {
        this.makeLowerCase(race);
        return rd.save(race);
    }

    public List<Race> findAllRaces() {
        return rd.findAll();
    }

    public Race findRaceById(Long id) {
        return rd.findById(id);
    }

    public boolean addAthlete(Long raceId, Long athleteId, Long categoryID, String raceNumber) {
        if (raceId!=null && athleteId!=null && categoryID !=null && raceNumber != null
            && raceId>=1 && athleteId>=1 &&categoryID>=1) {
            boolean checkAthleteAdded = rd.addAthlete(raceId, athleteId);
            if(checkAthleteAdded) { //only if persistence operation is successful the RawAthlete will be created and added to the StartList instance via serialization and deserialization operations
                checkAthleteAdded = addRawAthlete(raceId, athleteId, categoryID, raceNumber);
            }
            return checkAthleteAdded;

        }
        return false;
    }

    public boolean addCategory(Long raceId, Long categoryId) {
        if (raceId!=null && categoryId!=null && raceId>=1 && categoryId>=1) {
            return rd.addCategory(raceId, categoryId);
        }
        return false;
    }

    public Race updateRace(Race race) {
        this.makeLowerCase(race);
        return rd.update(race);
    }

    public boolean removeCategoryFromRace(Long raceId, Long categoryId) {
        if(raceId >= 1 && categoryId >= 1)
            return rd.removeCategory(raceId, categoryId);
        else
            return false;
    }

    /**
     * Remove an athlete subscribe earlier from a race
     * @param raceId the unique race id
     * @param athleteId the unique athlete id
     * @return boolean <strong>true</strong> if operation was successful otherwise <strong>false</strong>
     */
    public boolean removeAthleteFromRace(Long raceId, Long athleteId) {
        if(raceId >= 1 && athleteId >= 1) {
            if(rd.removeAthlete(raceId, athleteId))
                return removeRawAthlete(raceId, athleteId);
        }

        return false;
    }

    public List<Category> getAllRaceCategories(Long raceId) {
        if(raceId >= 1)
            return rd.findRaceCategories(raceId);
        else
            return null;
    }

    public List<Athlete> getAllRaceAthletes(Long raceId) {
        if(raceId >= 1)
            return rd.findRaceAthletes(raceId);
        else
            return null;
    }

    private void makeLowerCase(Race race) {
        race.setCity(race.getCity().toLowerCase());
        race.setPlace(race.getPlace().toLowerCase());
        race.setTitle(race.getTitle().toLowerCase());
    }

    private String createFileName(Long raceID) {
        return makeName(raceID);
    }

    private String makeName(Long raceID) {
        return Long.toString(raceID)
                .concat("_")
                .concat(rd.findById(raceID).getTitle().replace(" ", "_"))
                .concat("_")
                .concat(Integer.toString(rd.findById(raceID).getRaceDateTime().getYear()))
                .concat(".srd");
    }


    /** Implements the code to create StartList instance or retrieve it from serialized StartList objects if it has
        already been serialized being aware that the file in which the StartList instance is serialized is named
        according the rule below:
        file name = "(race-id)__(race-title)__(year-of-race-date).srd" -->  Eg: file name = "101__heroes__cup__2023.srd"
        (if there are  the empty spaces in the race title, replace them with an underscore as in the example above) */
    private boolean addRawAthlete(Long raceId, Long athleteId, Long categoryID, String raceNumber) {
        var lapsToRun = categoryService.findCategoryById(categoryID).getLapsNumber();
        // Creates RawAthlete Instance to add at a StarList instance
        RawAthlete rawAthlete = new RawAthlete(athleteId, raceNumber, categoryID, lapsToRun);
        var fileName = createFileName(raceId);
        try {
            StartList startList = serializer.deserialize(fileName);
            if(raceNumberAlreadyExists(startList.getRawAthletes(), raceNumber)) {
                rd.removeAthlete(raceId, athleteId); //since the race number already exists, I cancel the persistence operation performed earlier
                return false;
            }
            for(RawAthlete raw : startList.getRawAthletes()) System.out.println(raw);//todo remove this line after testing
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
                rd.removeAthlete(raceId, athleteId);//since the RawAthlete serialization failed, I cancel the persistence operation performed earlier
                System.out.println("[ERROR]org.manca.jakarta.project.service.RaceService.addAthlete:failed, problems to create new StartList instance.");
                return false;
            }
        }
        return true;
    }

    /**
     *Tries to deserialize a StartList object from file related to raceId,
     * then look up a specific RawAthlete by its id by  removing it from the
     * StartList, finally serialize the changed StartList instance.
     * @param raceId The unique race id
     * @param athleteId The unique athlete id
     * @return boolean <strong>true</strong> if deserialization operations were successful otherwise <strong>false</strong>
     */
    private boolean removeRawAthlete(Long raceId, Long athleteId) {
        boolean checkRemoveAthlete = false;
        String filename = this.makeName(raceId);
        try {
            StartList startList = serializer.deserialize(filename);
            for (RawAthlete rawAthlete : startList.getRawAthletes()) System.out.println(rawAthlete);
            for (var i=0; i<startList.getRawAthletes().size(); i++) {
                RawAthlete raw = startList.getRawAthletes().get(i);
                if (raw.getIdAthlete() == athleteId) {
                    checkRemoveAthlete = true;
                    startList.getRawAthletes().remove(i);
                    serializer.serialize(startList, filename);
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
}
