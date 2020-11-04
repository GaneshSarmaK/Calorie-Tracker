/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package restws.service;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import restws.Food;

/**
 *
 * @author ganesh
 */
@Stateless
@Path("restws.food")
public class FoodFacadeREST extends AbstractFacade<Food> {

    @PersistenceContext(unitName = "Test1PU")
    private EntityManager em;

    public FoodFacadeREST() {
        super(Food.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Food entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, Food entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Food find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Food> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Food> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }
    
    @GET
    @Path("findByFoodId/{foodId}")
    @Produces({"application/json"})
    public List<Food> findByFoodId(@PathParam("foodId") int foodId) {
        Query query = em.createNamedQuery("Food.findByFoodId");
        query.setParameter("foodId",foodId);
        return query.getResultList();
    }
    
    @GET
    @Path("findByFoodName/{foodName}")
    @Produces({"application/json"})
    public List<Food> findByFoodName(@PathParam("foodName") String foodName) {
        Query query = em.createNamedQuery("Food.findByFoodName");
        query.setParameter("foodName",foodName);
        return query.getResultList();
    }
    
    @GET
    @Path("findByCategory/{category}")
    @Produces({"application/json"})
    public List<Food> findByFoodCategory(@PathParam("category") String category) {
        Query query = em.createNamedQuery("Food.findByCategory");
        query.setParameter("category",category);
        return query.getResultList();
    }
    
    @GET
    @Path("findByCalorieAmount/{calorieAmount}")
    @Produces({"application/json"})
    public List<Food> findByCalorieAmount(@PathParam("calorieAmount") double calorieAmount) {
        Query query = em.createNamedQuery("Food.findByCalorieAmount");
        query.setParameter("calorieAmount",calorieAmount);
        return query.getResultList();
    }
    
    @GET
    @Path("findByServingUnit/{servingUnit}")
    @Produces({"application/json"})
    public List<Food> findByServingUnit(@PathParam("servingUnit") String servingUnit) {
        Query query = em.createNamedQuery("Food.findByServingUnit");
        query.setParameter("servingUnit",servingUnit);
        return query.getResultList();
    }
    
    @GET
    @Path("findByServingAmount/{servingAmount}")
    @Produces({"application/json"})
    public List<Food> findByServingAmount(@PathParam("servingAmount") double servingAmount) {
        Query query = em.createNamedQuery("Food.findByServingAmount");
        query.setParameter("servingAmount",servingAmount);
        return query.getResultList();
    }
    
    @GET
    @Path("findByFat/{fat}")
    @Produces({"application/json"})
    public List<Food> findByFat(@PathParam("fat") int fat) {
        Query query = em.createNamedQuery("Food.findByFat");
        query.setParameter("fat",fat);
        return query.getResultList();
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}
