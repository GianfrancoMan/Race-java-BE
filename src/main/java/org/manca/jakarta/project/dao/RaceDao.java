package org.manca.jakarta.project.dao;

import jakarta.ejb.EJBException;
import jakarta.enterprise.context.Dependent;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
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

    /* Persists Race object in the related  table based on the mapping */
    public Race save(Race race) {
        try {
            em.persist(race);
            em.flush();
            return race;
        } catch (EJBException e) {
            return null;
        }
    }

    /* Find all races stored and returns them as a list of Race object */
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

    /* Find a specific Race by its id*/
    public Race findById(Long id) {
        Race result = em.find(Race.class, id);
        return result;
    }

    /* Refresh a race entity changed*/
    public Race update(Race race) {
        try {
            return em.merge(race);
        } catch (EJBException e) {
            return null;
        }
    }

    public boolean addAthlete(Long raceId, Long athleteId) {
        try {
            Race race = em.find(Race.class, raceId);
            Athlete athlete = em.find(Athlete.class, athleteId);
            race.addAthlete(athlete);
            em.flush();
            return true;
        } catch (EJBException e) {
            return false;
        }
    }
    public boolean addCategory(Long raceId, Long categoryID) {
        try {
            Race race = em.find(Race.class, raceId);
            Category category = em.find(Category.class, categoryID);
            race.addCategory(category);
            em.flush();
            return true;

        } catch (EJBException e) {
            return false;
        }
    }

    public List<Athlete> findRaceAthletes(Long raceId) {
        return em.find(Race.class, raceId).getAthletes();
    }
}
