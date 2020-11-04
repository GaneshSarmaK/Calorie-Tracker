/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package restws.service;

import java.util.List;
import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import restws.Consumption;

/**
 *
 * @author ganesh
 */
@Stateless
@Path("restws.consumption")
public class ConsumptionFacadeREST extends AbstractFacade<Consumption> {

    @PersistenceContext(unitName = "Test1PU")
    private EntityManager em;

    public ConsumptionFacadeREST() {
        super(Consumption.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Consumption entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, Consumption entity) {
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
    public Consumption find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Consumption> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Consumption> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }
    
   @GET
    @Path("findByConsumptionId/{consumptionId}")
    @Produces({"application/json"})
    public List<Consumption> findByConsumptionId(@PathParam("consumptionId") int consumptionId) {
        Query query = em.createNamedQuery("Consumption.findByConsumptionId");
        query.setParameter("consumptionId",consumptionId);
        return query.getResultList();
    }
    
    @GET
    @Path("findByConsumptionDate/{consumptionDate}")
    @Produces({"application/json"})
    public List<Consumption> findByConsumptionDate(@PathParam("consumptionDate") String consumptionDate) {
        Query query = em.createNamedQuery("Consumption.findByConsumptionDate");
        query.setParameter("consumptionDate",consumptionDate);
        return query.getResultList();
    }
    
    @GET
    @Path("findByQuantity/{quantity}")
    @Produces({"application/json"})
    public List<Consumption> findByQuantity(@PathParam("quantity") int quantity) {
        Query query = em.createNamedQuery("Consumption.findByQuantity");
        query.setParameter("quantity",quantity);
        return query.getResultList();
    }
    
    @GET
    @Path("findByUserIdConsumption/{userId}")
    @Produces({"application/json"})
    public List<Consumption> findByUserIdConsumption(@PathParam("userId") int userId) {
        TypedQuery<Consumption> query = em.createQuery("SELECT c.userId FROM Consumption c WHERE c.userId.userId = :userId", Consumption.class);
        query.setParameter("userId",userId);
        return query.getResultList();
    }
    
    @GET
    @Path("findByFoodIdConsumption/{foodId}")
    @Produces({"application/json"})
    public List<Consumption> findByFoodIdConsumption(@PathParam("foodId") int foodId) {
        TypedQuery<Consumption> query = em.createQuery("SELECT c.foodId FROM Consumption c WHERE c.foodId.foodId = :foodId", Consumption.class);
        query.setParameter("foodId",foodId);
        return query.getResultList();
    }
    
    @GET
    @Path("CaloriesConsumedByUserInOneDay/{userId}/{consumptionDate}")
    @Produces({"application/json"})
    public Object caloriesConsumedOnDate(@PathParam("userId")int userId,@PathParam("consumptionDate")String consumptionDate) {
        List<Object[]> queryList = em.createQuery("SELECT c.userId.userId,c.consumptionDate,c.foodId.calorieAmount,c.quantity FROM Consumption c").getResultList();
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        double totalCaloriesConsumed = 0;
        for (Object[] row : queryList) {
            if(userId == (int)row[0] && consumptionDate.equals((String)row[1]))
            {
                totalCaloriesConsumed += ((int)row[2]*(int)row[3])*1.0;
            }
            
        }
        JsonObject object = Json.createObjectBuilder()
                .add("Total calories consumed: ",totalCaloriesConsumed)
                .build();
            arrayBuilder.add(object);
        JsonArray jArray= arrayBuilder.build();
        return jArray;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}
