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
import org.manca.jakarta.project.util.service.RawAthleteService;

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
    @Inject
    RawAthleteService rawAthleteService;

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
            rawAthleteService.setFileName(this.makeName(raceId));
            if(checkAthleteAdded) { //only if persistence operation is successful the RawAthlete will be created and added to the StartList instance via serialization and deserialization operations
                checkAthleteAdded = rawAthleteService.addRawAthlete(raceId, athleteId, categoryID, raceNumber);
                if(!checkAthleteAdded) rd.removeAthlete(raceId, athleteId); //since the race number already exists, I cancel the persistence operation performed earlier
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
        boolean removed = false;
        if(raceId != null && categoryId != null &&raceId >= 1 && categoryId >= 1) {
            removed = rd.findById(raceId) != null;
            if(removed) {
                rawAthleteService.setFileName(this.makeName(raceId));
                removed = rawAthleteService.findRawAthleteByCategory(categoryId).size() == 0;
            }
            if(removed) removed =  rd.removeCategory(raceId, categoryId);
        }
        return removed;
    }

    /**
     * Remove an athlete subscribe earlier from a race
     * @param raceId the unique race id
     * @param athleteId the unique athlete id
     * @return boolean <strong>true</strong> if operation was successful otherwise <strong>false</strong>
     */
    public boolean removeAthleteFromRace(Long raceId, Long athleteId) {
        if(raceId >= 1 && athleteId >= 1) {
            rawAthleteService.setFileName(this.makeName(raceId));
            if(rd.removeAthlete(raceId, athleteId))
                return rawAthleteService.removeRawAthlete(athleteId);
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

    //Makes Lower Case the basic data of a Race.
    private void makeLowerCase(Race race) {
        race.setCity(race.getCity().toLowerCase());
        race.setPlace(race.getPlace().toLowerCase());
        race.setTitle(race.getTitle().toLowerCase());
    }

    public String makeName(Long raceID) {
        return Long.toString(raceID)
                .concat("_")
                .concat(rd.findById(raceID).getTitle().replace(" ", "_"))
                .concat("_")
                .concat(Integer.toString(rd.findById(raceID).getRaceDateTime().getYear()))
                .concat(".srd");
    }

}
