package org.manca.jakarta.project.dao;

import jakarta.ejb.EJBException;
import jakarta.enterprise.context.Dependent;
import jakarta.persistence.*;
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
        return result;
    }

    /*
     Make persistent a new entity instance of Category in the persistent context.
     */
    public Category save(Category category ) {
        try {
            em.persist(category);
            return category;
        } catch (EJBException e) {
            return null;
        }
    }

    /*
     Make persistent all changes on the entity instance in the persistent context
     */
    public Category update(Category category) {
        Category result= em.find(Category.class, (Long)category.getId());
        if(this.findById(category.getId()) == null) return null;
        result.setId(category.getId());
        result.setTitle(category.getTitle());
        result.setRaces(category.getRaces());
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

    /**
     * Implements pagination to get all categories of the race that have the string 'subTitle' passed as parameter
     * ito their title.
     * Pagination leverage on 'pageNumber' and 'pageSize' passed as parameter
     * Returns a List of Category objects if success else null.
     */
    public List<Category> categoriesBySubTitle(String subTitle,  int pageNumber, int pageSize) {
        subTitle = subTitle.translateEscapes();

        var queryString = "SELECT c FROM Category c ";
        queryString += "WHERE c.title LIKE CONCAT('%', :sub_title, '%') ";
        TypedQuery<Category> query = em.createQuery(
                queryString,
                Category.class);
        query.setParameter("sub_title", subTitle);
        query.setFirstResult( (pageNumber-1) * pageSize );
        query.setMaxResults(pageSize);
        try {
            return query.getResultList();
        } catch (EJBException | NullPointerException e) {
            System.out.println("CUSTOM ERROR: org.manca.jakarta.project.dao.CategoryDao.categoriesBySubTitle[Pagination Failed]");
            return null;
        }
    }
}
