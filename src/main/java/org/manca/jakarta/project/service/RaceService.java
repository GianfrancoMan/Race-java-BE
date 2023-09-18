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
        if (entityExists(Race.class.getSimpleName(), raceId)
                && entityExists(Athlete.class.getSimpleName(), athleteId)
                && entityExists(Category.class.getSimpleName(), categoryID)
                && raceNumber != null
                && !raceNumber.equals("")) {

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
        if (entityExists(Race.class.getSimpleName(), raceId)
                && entityExists(Category.class.getSimpleName(), categoryId)) {

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
        boolean removed =
                entityExists(Race.class.getSimpleName(), raceId) && entityExists(Category.class.getSimpleName(), categoryId);

        if(removed) {
            // the category is not removed if athletes in currently race have that category
            removed = rawService.findRawAthleteByCategory(raceId, categoryId).size() == 0;
        }
        if(removed) {
            removed = rawService.removeRawCategory(categoryId);
            if(removed)
                removed =  rd.removeCategory(raceId, categoryId);
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
        boolean removed =  this.entityExists(Athlete.class.getSimpleName(), athleteId);

        if(removed) {
            rawService.setFileName(this.makeName(raceId));
            removed = rawService.removeRawAthlete(athleteId);
        }

        if (removed) {
            removed = rd.removeAthlete(raceId, athleteId);
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
     * Checks if in tha database exists a persisted entity identified by its class name that have the same id passed as
     * parameter.
     * @param className the name of the entity class.
     * @param id the unique id of entity class instance.
     * @return true if the entity exists in the database otherwise false.
     */
    public boolean entityExists(String className, Long id) {
        if (id != null && id >= 0 ) {
            if (className.equals("Race")) {
                return this.findRaceById(id) != null;
            }
            if (className.equals("Athlete")) {
                return athleteService.findAthlete(id) != null;
            }
            if (className.equals("Category")) {
                return categoryService.findCategoryById(id) != null;
            }
        }

        return false;
    }

}
