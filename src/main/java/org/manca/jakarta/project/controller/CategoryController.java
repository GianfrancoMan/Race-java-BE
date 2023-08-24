package org.manca.jakarta.project.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.manca.jakarta.project.model.Category;
import org.manca.jakarta.project.service.CategoryService;

import java.util.List;

@Path(value = "/ctg")
public class CategoryController {

    @Inject
    private CategoryService cs;

    /*REST to retrieve all categories from DB*/
    @GET
    @Path(value = "/all")
    @Produces({ MediaType.APPLICATION_JSON })
    public List<Category> getAll() {
        return cs.findAllCategories();
    }

    /*REST to persist a new athlete category*/
    @POST
    @Path(value = "/new")
    @Consumes({ MediaType.APPLICATION_JSON })
    public Category save(Category category) {
        return cs.saveCategory(category);
    }

    /*REST to change category data*/
    @PUT
    @Path(value = "/update")
    @Consumes({ MediaType.APPLICATION_JSON })
    public Category update(Category category) {
        return cs.updateCategory(category);
    }

    /*REST to delete a category*/
    @DELETE
    @Path(value = "/delete")
    public boolean delete(@QueryParam("id") long id) {
        return cs.deleteCategory(id);
    }

    /*REST to retrieve a specific category passing his ID*/
    @GET
    @Path(value = "/category")
    @Produces({ MediaType.APPLICATION_JSON })
    public Category getById(@QueryParam("id") long id) {
        return cs.findCategoryById(id);
    }
    @GET
    @Path(value = "/sbt")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Category> getBySubName(
            @QueryParam(value = "subTitle") String subTitle,
            @QueryParam(value = "pageNumber") int pageNumber,
            @QueryParam(value = "pageSize") int pageSize) {
        return cs.findCategoriesBySubTitle(subTitle, pageNumber, pageSize);
    }
}
