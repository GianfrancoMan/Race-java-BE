package org.manca.jakarta.project.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import org.manca.jakarta.project.dao.CategoryDao;
import org.manca.jakarta.project.dao.RaceDao;
import org.manca.jakarta.project.model.Category;
import org.manca.jakarta.project.model.Race;

import java.util.List;

@Stateless
public class CategoryService {

    @Inject
    private CategoryDao cd;
    @Inject
    RaceService raceService;

    /**
     * The goal is to save a new category in the database but this will only be possible if there are no
     * streamlined Category titles in the database that match the streamlined 'category.title'.
     * (streamlined means that at a string has been removed the following character " ", "-", "_", "/" and made
     * uppercase, the consequence is that if the new category title is "Master 1" for tha application it is equals
     * to "master-1" or "Master_1" or "maSTer/1" and so on, this is due to the fact that the streamlineTitle()
     * method return the same result in each this cases which is "MASTER1")
     * @param category the new Category that should be persistent
     * @return the category instance that has been persisted
     */
    public Category saveCategory(Category category) {
        if(this.categoryTitleAlreadyExists(category.getTitle()))
            return null;//todo look up a way to attach a header with message in the response

        this.makeUpperCase(category);
        return cd.save(category);
    }

    /**
     * Retrieves all categories from the persistence context by the CategoryDao findAll() method
     * @return A list of Category instances.
     */
    public List<Category> findAllCategories() {
        return cd.findAll();
    }

    /**
     * Persists changes in the Category entity instance and retrieve the updated instance
     * by the CategoryDao update() method, but this only be possible if this category hasn't been used in any Race.
     * @return the updated Category instance if operation success otherwise null.
     */
    public Category updateCategory(Category category) {
        if(raceService.categoryIsUsed(category.getId()) || this.categoryTitleAlreadyExists(category.getTitle()))
            return null;

        this.makeUpperCase(category);
        return cd.update(category);
    }

    /**
     * Delete the Category entity instance leveraging on its unique 'id' but this only be possible
     * if this category hasn't been used in any Race.
     * @param id the unique id category attribute
     * @return true if operation success otherwise null.
     */
    public boolean deleteCategory(long id) {
        if(id > 0 && !raceService.categoryIsUsed(id)) {
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

    /**
     * Implements pagination to get all categories of the race that have the string 'subTitle' passed as parameter
     * ito their title.
     * Pagination leverage on 'pageNumber' and 'pageSize' passed as parameter
     * Returns a List of Category objects if success else null.
     */
    public List<Category> findCategoriesBySubTitle(String subTitle, int pageNumber, int pageSize) {
        if(subTitle != null && pageNumber != 0 && pageSize != 0) {
            return cd.categoriesBySubTitle(subTitle, pageNumber, pageSize);
        }
        return null;
    }

    /**
     * The idea is to remove all the following character from the passed string: " "(blank space), "-", "_","/"
     * @param title the string to streamline.
     * @return a result string.
     */
    private String streamlineTitle(String title) {
        return title
                .toUpperCase()
                .replace("-", "")
                .replace("_", "")
                .replace(" ", "")
                .replace("/", "");
    }

    /**
     * The method checks if the streamlined <strong>newTitle</strong> is equals to the
     * streamlined <strong>persistentTitle</strong>
     * @param newTitle the streamlined title of a new category
     * @return boolean true if a streamlined newTitle already exists in the database otherwise false
     */
    private boolean categoryTitleAlreadyExists(String newTitle) {
        List<Category> categories = cd.findAll();

        for(Category category : categories) {
            if(this.streamlineTitle(newTitle).equalsIgnoreCase(this.streamlineTitle(category.getTitle())))
                return true;
        }

        return false;
    }
 }
