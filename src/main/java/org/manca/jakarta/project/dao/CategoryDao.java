package org.manca.jakarta.project.dao;

import jakarta.enterprise.context.Dependent;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaQuery;
import org.manca.jakarta.project.model.Category;

import java.util.List;
@Dependent
public class CategoryDao {
    @PersistenceContext(unitName = "race")
    EntityManager em;

    public List<Category> findAll() {
        CriteriaQuery<Category> criteriaQuery = em.getCriteriaBuilder().createQuery(Category.class);
        criteriaQuery.select(criteriaQuery.from(Category.class));
        return  em.createQuery(criteriaQuery).getResultList();
    }

    /*
     Retrieve a specific entity instance of Category leverage on
     its relational mapping and its id property
     */
    public Category findById(long id) {
        Category result = em.find(Category.class, id);
        System.out.println(result);
        return result;
    }

    /*
     Make persistent a new entity instance of Category in the persistent context.
     */
    public Category save(Category category ) {
        em.persist(category);
        em.flush();
        Category persisted = this.findById(category.getId());
        return persisted;
    }

    /*
     Make persistent all changes on the entity instance in the persistent context
     */
    public Category update(Category category) {
        Category result= em.find(Category.class, (Long)category.getId());
        result.setId(category.getId());
        result.setTitle(category.getTitle());
        result.setRaces(category.getRaces());
        result.setLapsNumber(category.getLapsNumber());
        result.setRaceStartTime(category.getRaceStartTime());
        return result;
    }

    public boolean delete(long id) {
        try {
            Category result = em.find(Category.class, id);
            em.remove(result);
            return true;

        } catch (NoResultException | IllegalArgumentException e) {
            return false;
        }
    }
}
