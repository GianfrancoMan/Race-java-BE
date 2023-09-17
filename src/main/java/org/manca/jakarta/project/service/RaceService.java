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
    private AthleteService athleteService;
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
        if (this.entitiesExist(raceId, athleteId, categoryID) && raceNumber != null && !raceNumber.equals("")) {
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
        if (this.entitiesExist(raceId, categoryId)) {
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
        if(raceId != null && categoryId != null && raceId >= 1 && categoryId >= 1) {
            removed = rd.findById(raceId) != null;
            if(removed) {
                removed = rawService.findRawAthleteByCategory(raceId, categoryId).size() == 0;
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
        boolean removed = false;

        if(raceId != null && athleteId != null && raceId >= 1 && athleteId >= 1) {
            removed = rd.findById(raceId) != null;
            if (removed) {
                rawService.setFileName(this.makeName(raceId));
                removed = rd.removeAthlete(raceId, athleteId);
                if(removed) {
                    removed = rawService.removeRawAthlete(athleteId);
                }
            }
        }

        return removed;
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

    /**
     * Validate the ids in three steps, first checks that each of them is not null, then checks that each of them
     * is greater than 0 and finally verify if a Race, an Athlete and a Category with id that is equal to raceId,
     * athleteId and categoryId exist in the database respectively
     * @param raceId the unique Race id
     * @param athleteId the unique Athlete id
     * @param categoryId the unique Category id
     * @return true if the ids constraints are respected otherwise false.
     * @apiNote 2 overload.
     */
    public boolean entitiesExist(Long raceId, Long athleteId, Long categoryId) {
        return  (raceId != null && athleteId != null && categoryId != null) &&
                (raceId > 0 && athleteId > 0 && categoryId > 0) &&
                (this.findRaceById(raceId) != null) &&
                (athleteService.findAthlete(athleteId)!=null) &&
                (categoryService.findCategoryById(categoryId)!=null);
    }

    /**
     * Validate the ids in three steps, first checks that each of them is not null, then checks that each of them
     * is greater than 0 and finally verify if both Race and Category with id that is equal to raceId and categoryId
     * exist in the database respectively.
     * @param raceId the unique Race id
     * @param categoryId the unique Category id
     * @return true if the ids constraints are respected otherwise false.
     * @apiNote 2 overload.
     */
    public boolean entitiesExist(Long raceId, Long categoryId) {
        return  (raceId != null && categoryId != null) &&
                (raceId > 0 && categoryId > 0) &&
                (this.findRaceById(raceId) != null) &&
                (categoryService.findCategoryById(categoryId)!=null);
    }

    /**
     * Validate the id in three steps, first checks that it is not null, then checks that it is greater than 0
     * and finally verify if Race with id that is equal to raceId, exist in the database.
     * @param raceId the unique Race id
     * @return true if the id constraints are respected otherwise false.
     * @apiNote 2 overload.
     */
    public boolean entitiesExist(Long raceId) {
        return  (raceId != null) && (raceId > 0) && (this.findRaceById(raceId) != null);
    }

}
