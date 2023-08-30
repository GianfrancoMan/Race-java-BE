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

    public boolean removeAthleteFromRace(Long raceId, Long athleteId) {
        if(raceId >= 1 && athleteId >= 1)
            return rd.removeAthlete(raceId, athleteId);
        else
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
                .concat(Integer.toString(LocalDate.now().getYear()))
                .concat(".srd");
    }


    /** Implements the code to create StartList instance or retrieve it from serialized StartList objects if it has
        already been serialized being aware that the file in which the StartList instance is serialized is named
        according the rule below:
        file name = "(race-id)__(race-title)__(current-year).srd" -->  Eg: file name = "101__heroes__cup__2023.srd"
        (if there are  the empty spaces in the race title, replace them with an underscore as in the example above) */
    private boolean addRawAthlete(Long raceId, Long athleteId, Long categoryID, String raceNumber) {
        var lapsToRun = categoryService.findCategoryById(categoryID).getLapsNumber();
        // Creates RawAthlete Instance to add at a StarList instance
        RawAthlete rawAthlete = new RawAthlete(athleteId, raceNumber, categoryID, lapsToRun);
        var fileName = createFileName(raceId);
        try {
            StartList startList = serializer.deserialize(fileName);
            for(RawAthlete raw : startList.getRawAthletes()) System.out.println(raw);
            startList.addRawAthlete(rawAthlete);
            serializer.serialize(startList, fileName);

        } catch (IOException | ClassNotFoundException e) {
            //create a new StartList instance
            System.out.println("[ERROR]org.manca.jakarta.project.service.RaceService.addAthlete:problems to retrieve StartList instance, I try to create a new instance...");
            List<RawAthlete> rawAthletes = new ArrayList<>(1);
            rawAthletes.add(rawAthlete);
            StartList startList = new StartList(rawAthletes);
            try {
                serializer.serialize(startList, fileName);
            } catch (IOException e2) {
                rd.removeAthlete(raceId, athleteId);//since the RawAthlete serialization failed, I cancel the persistence operation performed earlier
                System.out.println("[ERROR]org.manca.jakarta.project.service.RaceService.addAthlete:failed, problems to create new StartList instance.");
                return false;
            }
        }
        return true;
    }
}
