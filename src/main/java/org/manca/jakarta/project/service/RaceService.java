package org.manca.jakarta.project.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import org.manca.jakarta.project.dao.RaceDao;
import org.manca.jakarta.project.model.Athlete;
import org.manca.jakarta.project.model.Category;
import org.manca.jakarta.project.model.Race;
import org.manca.jakarta.project.util.service.RawService;

import java.util.List;


@Stateless
public class RaceService {
    @Inject
    private CategoryService categoryService;
    @Inject
    private RaceDao rd;
    @Inject
    RawService rawService;

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
            rawService.setFileName(this.makeName(raceId));
            if(checkAthleteAdded) { //only if persistence operation is successful the RawAthlete will be created and added to the StartList instance via serialization and deserialization operations
                checkAthleteAdded = rawService.addRawAthlete(athleteId, categoryID, raceNumber);
                if(!checkAthleteAdded) rd.removeAthlete(raceId, athleteId); //since the race number already exists, I cancel the persistence operation performed earlier
            }
            return checkAthleteAdded;

        }
        return false;
    }

    public boolean addCategory(Long raceId, Long categoryId) {
        if (raceId!=null && categoryId!=null && raceId>=1 && categoryId>=1) {
            boolean checkCategoryAdded =  rd.addCategory(raceId, categoryId);
            rawService.setFileName(this.makeName(raceId));
            if(checkCategoryAdded) { //only if persistence operation is successful the RawCategory will be created and added to the StartList instance via serialization and deserialization operations
            checkCategoryAdded = rawService.addRawCategory(categoryId);
                if(!checkCategoryAdded) rd.removeCategory(raceId, categoryId); //since the adding of the RawCategory fails, I cancel the persistence operation performed earlier
            }
            return checkCategoryAdded;
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
                rawService.setFileName(this.makeName(raceId));
                removed = rawService.findRawAthleteByCategory(categoryId).size() == 0;
            }
            if(removed) {
                removed = rawService.removeRawCategory(categoryId);
                if(removed)
                    removed =  rd.removeCategory(raceId, categoryId);
            }
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
            rawService.setFileName(this.makeName(raceId));
            if(rd.removeAthlete(raceId, athleteId))
                return rawService.removeRawAthlete(athleteId);
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

    public boolean categoryIsUsed(Long id) {
        List<Race> races = rd.findAll();
        for(Race race : races) {
            for(Category category : race.getCategories()) {
                if(category.getId().longValue() == id)
                    return true;
            }
        }
        return false;
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
