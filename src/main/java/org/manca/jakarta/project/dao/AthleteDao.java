package org.manca.jakarta.project.dao;

import jakarta.ejb.EJBException;
import jakarta.enterprise.context.Dependent;
import jakarta.persistence.*;
import jakarta.persistence.criteria.*;
import org.manca.jakarta.project.model.Athlete;

import java.time.LocalDate;
import java.util.List;

@Dependent
public class AthleteDao {


    @PersistenceContext(unitName = "race")
    private EntityManager em;

    /*Retrieves all athletes from the DB*/
    public List<Athlete> findAll() {
        CriteriaQuery<Athlete> criteriaQuery = em.getCriteriaBuilder().createQuery(Athlete.class);
        criteriaQuery.select(criteriaQuery.from(Athlete.class));
        return em.createQuery(criteriaQuery).getResultList();
    }

    /*Save in the DB, the athlete passed as parameter*/
    public Athlete save(Athlete athlete) {
        try {
            em.persist(athlete);
            return athlete;
        } catch (EJBException e) {
            return null;
        }
    }

    /*retrieves a specific Athlete from DB*/
    public Athlete findById(long id) {
        try {
            // Define the entity type you want to query
            Class<Athlete> athleteClass = Athlete.class;

            CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
            CriteriaQuery<Athlete> criteriaQuery = criteriaBuilder.createQuery(athleteClass);
            // Define the root of the query
            Root<Athlete> root = criteriaQuery.from(athleteClass);
            // Define predicates to filter the results (e.g., by ID)
            Predicate idPredicate = criteriaBuilder.equal(root.get("id"), id);
            // Apply the predicates to the query
            criteriaQuery.where(idPredicate);
            // Execute the query and retrieve the result
            Athlete result = em.createQuery(criteriaQuery).getSingleResult();
            // Use the retrieved entity*/
            return result;
        } catch (NoResultException e) {
            return null;
        }
    }

    /*update in the DB the athlete that match the athlete passed as parameter*/
    public boolean update(Athlete athlete) {
        try {
            CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
            CriteriaQuery<Athlete> criteriaQuery = criteriaBuilder.createQuery(Athlete.class);

            Root<Athlete> root = criteriaQuery.from(Athlete.class);
            Predicate idPredicate = criteriaBuilder.equal(root.get("id"), athlete.getId());

            criteriaQuery.where(idPredicate);

            Athlete athleteToUpdate = em.createQuery(criteriaQuery).getSingleResult();

            if (athleteToUpdate != null) {
                // Update the attributes of the entity
                athleteToUpdate.setFirstname(athlete.getFirstname());
                athleteToUpdate.setLastname(athlete.getLastname());
                athleteToUpdate.setBirthDate(athlete.getBirthDate());
                athleteToUpdate.setCity(athlete.getCity());
                athleteToUpdate.setTeam(athlete.getTeam());
                // Automatically the transaction configured in the application server update the DB
            }
            return true;

        }catch (NoResultException e) {
            return false;
        }
    }

    /*Remove from the DB, the athlete where its id match the id passed as parameter */
    public boolean delete(long id) {
        try {

            Athlete athlete = this.findById(id);
            em.remove(athlete);
            return true;

        } catch (NoResultException | IllegalArgumentException e) {

            return false;
        }
    }

    /**
     * Implements pagination to get all athletes of the race that have the string 'subName' passed as parameter
     * ito their complete name.
     * Pagination leverage on 'pageNumber' and 'pageSize' passed as parameter
     * Returns a List of Athletes objects if success else null.
     */
    public List<Athlete> athletesBySubName(String subName, int pageNumber, int pageSize) {
        var queryString = "SELECT a FROM Athlete a ";
        queryString += "WHERE a.firstname LIKE CONCAT('%', :sub_name, '%') ";
        queryString += "OR a.lastname LIKE CONCAT('%', :sub_name, '%') ";
        queryString += "ORDER BY a.lastname";
        TypedQuery<Athlete> query = em.createQuery(
                queryString,
                Athlete.class);
        query.setParameter("sub_name", subName);
        query.setFirstResult((pageNumber-1) * pageSize);
        query.setMaxResults(pageSize);
        try {
            return query.getResultList();
        } catch (EJBException | NullPointerException e) {
            System.out.println("CUSTOM ERROR: org.manca.jakarta.project.dao.AthleteDao.athletesBySubName[Pagination Failed]");
            return null;
        }
    }

    /**
     * find all athletes that hav the birthdate equal to the 'birthdate' reference passed as parameter in the form of a LocalDate
     * instance.
     * Returns a List of Athletes objects if success else null.
     */
    public List<Athlete> athletesByDate(LocalDate birthdate) {
        TypedQuery<Athlete> query = em.createQuery(
                "SELECT a FROM Athlete a WHERE a.birthDate=:birthdate",
                Athlete.class);
        query.setParameter("birthdate", birthdate);
        try {
            return query.getResultList();
        }catch (EJBException |NullPointerException e) {
            return null;
        }
    }
}
