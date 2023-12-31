package org.manca.jakarta.project.dao;

import jakarta.ejb.EJBException;
import jakarta.enterprise.context.Dependent;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaQuery;
import org.manca.jakarta.project.model.Athlete;
import org.manca.jakarta.project.model.Category;
import org.manca.jakarta.project.model.Race;

import java.util.List;

@Dependent
public class RaceDao {

    @PersistenceContext(unitName = "race")
    private EntityManager em;

    /**
     *  Persists Race object in the related  table based on the mapping
     *  return a reference for the Race instance
     */
    public Race save(Race race) {
        try {
            em.persist(race);
            em.flush();
            return race;
        } catch (EJBException e) {
            return null;
        }
    }

    /**
     * Find all races stored and returns them as a list of Race objects if success,
     * else return null.
     */
    public List<Race> findAll() {
        try {
            CriteriaQuery<Race> query = em.getCriteriaBuilder().createQuery(Race.class);
            query.select(query.from(Race.class));
            List<Race> resultList = em.createQuery(query).getResultList();
            return resultList;
        }  catch (EJBException e) {
            return null;
        }
    }

    /**
     * Find a specific Race by its id
     * return a reference for the Race instance found if success else null.
     */
    public Race findById(Long id) {
        Race result = em.find(Race.class, id);
        return result;
    }

    /**
     *  Refresh a race with the state of the race passes as parameter
     *  returns the refreshed race if success else return null.
     */
    public Race update(Race race) {
        try {
            return em.merge(race);
        } catch (EJBException e) {
            return null;
        }
    }
    /**
     * Adds the athlete which has id equal to 'athleteId' to the race which has id equal to 'raceId'
     * return boolean true if success else false.
     */
    public boolean addAthlete(Long raceId, Long athleteId) {
        try {
            Race race = em.find(Race.class, raceId);

            if(this.checkAlreadyPresent(race.getAthletes(), athleteId))
                return false;

            Athlete athlete = em.find(Athlete.class, athleteId);
            race.addAthlete(athlete);
            em.flush();
            return true;
        } catch (EJBException | NullPointerException e) {
            return false;
        }
    }

    /**
     * remove the athlete which has id equal to 'athleteId' from the race which has the id equal to 'raceId'
     * return boolean true if success else false.
     */
    public boolean removeAthlete(Long raceId, Long athleteId) {
        var check = false;
        try {
            Race race = em.find(Race.class, raceId);
            List<Athlete> athletes = race.getAthletes();
            for(Athlete athlete : athletes) {
                if(athlete.getId().longValue() == athleteId.longValue()) {
                    athletes.remove(athlete);
                    check = true;
                    break;
                }
            }
            if(check) race.setAthletes(athletes);
            return check;

        } catch (EJBException e) {
            return false;
        }
    }
    /**
     * Adds the category which has id equal to 'categoryId' to the race which has id equal to 'raceId'
     * return boolean true if success else false.
     */
    public boolean addCategory(Long raceId, Long categoryId) {
        try {
            Race race = em.find(Race.class, raceId);

            if(race == null || this.checkAlreadyPresent(race.getCategories(), categoryId))
                return false;

            Category category = em.find(Category.class, categoryId);
            race.addCategory(category);
            em.flush();
            return true;

        } catch (EJBException | NullPointerException e) {
            return false;
        }
    }

    /**
     * remove the category which has id equal to 'categoryId' from the race which has the id equal to 'raceId'
     * return boolean true if success else false.
     */
    public boolean removeCategory(Long raceId, Long categoryId) {
        var check = false;
        try {
            Race race = em.find(Race.class, raceId);
            List<Category> categories = race.getCategories();
            for (Category category : categories) {
                if (category.getId().longValue() == categoryId.longValue()) {
                    categories.remove(category);
                    check =true;
                    break;
                }
            }
            if(check) race.setCategories(categories);
            return check;
        } catch (EJBException e) {
            return false;
        }
    }

    /**
     * returns all athletes of the race which has the id equal to 'raceId'
     * or null if fails.
     */
    public List<Athlete> findRaceAthletes(Long raceId) {
        try {
            return em.find(Race.class, raceId).getAthletes();
        } catch (EJBException | NullPointerException e) {
            return null;
        }
    }

    /**
     * return all categories of the race which has the id equal to 'raceId'
     * or null if fails
     */
    public List<Category> findRaceCategories(Long raceId) {
        try {
            return em.find(Race.class, raceId).getCategories();
        } catch (EJBException | NullPointerException e) {
            return null;
        }
    }


    /**
     * It works only with Athlete or Category instances and check whether the id passed as parameter
     * is already present in the both athlete list or category list of the race.
     * return a boolean true if id already exists else false.
     */
    private boolean checkAlreadyPresent(List items, Long id) {
        Athlete athlete = null;
        Category category = null;
        for(Object item : items) {
            if(item instanceof Athlete) {
                athlete = (Athlete) item;
                if(athlete.getId().longValue() == id.longValue() )
                    return true;
            }
            if(item instanceof Category) {
                category = (Category) item;
                if(category.getId().longValue() == id.longValue() )
                    return true;
            }
        }
        return false;
    }
}
