package org.manca.jakarta.project.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import org.manca.jakarta.project.dao.CategoryDao;
import org.manca.jakarta.project.model.Category;

import java.util.List;

@Stateless
public class CategoryService {

    @Inject
    private CategoryDao cd;

    /*
     Save a new category by the CategoryDao save() method.
     */
    public Category saveCategory(Category category) {
        this.makeUpperCase(category);
        return cd.save(category);
    }

    /*
     Retrieves all categories from the persistence context by the CategoryDao findAll() method
     */
    public List<Category> findAllCategories() {
        return cd.findAll();
    }

    /*
     Persists changes in the Category entity instance and retrieve the updated instance
     by the CategoryDao update() method
     */
    public Category updateCategory(Category category) {
        this.makeUpperCase(category);
        return cd.update(category);
    }

    public boolean deleteCategory(long id) {
        if(id > 0) {
            return cd.delete(id);
        }
        else return false;
    }

    /*
     set uppercase the category's title.
     */
    private void makeUpperCase(Category category) {
        category.setTitle(category.getTitle().toUpperCase());
    }

    public Category findCategoryById(long id) {
        if(id > 0) {
            return cd.findById(id);
        }
        return null;
    }
}
