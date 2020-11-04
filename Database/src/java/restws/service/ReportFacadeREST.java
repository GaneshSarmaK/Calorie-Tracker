/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package restws.service;

import java.time.LocalDate;
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
import restws.Report;

/**
 *
 * @author ganesh
 */
@Stateless
@Path("restws.report")
public class ReportFacadeREST extends AbstractFacade<Report> {

    @PersistenceContext(unitName = "Test1PU")
    private EntityManager em;

    public ReportFacadeREST() {
        super(Report.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Report entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, Report entity) {
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
    public Report find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Report> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Report> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }
    @GET
    @Path("findByReportId/{reportId}")
    @Produces({"application/json"})
    public List<Report> findByReportId(@PathParam("reportId") int reportId) {
        Query query = em.createNamedQuery("Report.findByReportId");
        query.setParameter("reportId",reportId);
        return query.getResultList();
    }
    
    @GET
    @Path("findByReportDate/{reportDate}")
    @Produces({"application/json"})
    public List<Report> findByReportDate(@PathParam("reportDate") String reportDate) {
        Query query = em.createNamedQuery("Report.findByReportDate");
        query.setParameter("reportDate",reportDate);
        return query.getResultList();
    }
    
    @GET
    @Path("findByCaloriesConsumed/{caloriesConsumed}")
    @Produces({"application/json"})
    public List<Report> findByCaloriesConsumed(@PathParam("caloriesConsumed") double caloriesConsumed) {
        Query query = em.createNamedQuery("Report.findByCaloriesConsumed");
        query.setParameter("caloriesConsumed",caloriesConsumed);
        return query.getResultList();
    }
    
    @GET
    @Path("findByCaloriesBurned/{caloriesBurned}")
    @Produces({"application/json"})
    public List<Report> findByCaloriesBurned(@PathParam("caloriesBurned") double caloriesBurned) {
        Query query = em.createNamedQuery("Report.findByCaloriesBurned");
        query.setParameter("caloriesBurned",caloriesBurned);
        return query.getResultList();
    }
    
    @GET
    @Path("findByStepsTaken/{stepsTaken}")
    @Produces({"application/json"})
    public List<Report> findByStepsTaken(@PathParam("stepsTaken") int stepsTaken) {
        Query query = em.createNamedQuery("Report.findByStepsTaken");
        query.setParameter("stepsTaken",stepsTaken);
        return query.getResultList();
    }
    
    @GET
    @Path("findByCalorieGoal/{calorieGoal}")
    @Produces({"application/json"})
    public List<Report> findByCalorieGoal(@PathParam("calorieGoal") double calorieGoal) {
        Query query = em.createNamedQuery("Report.findByCalorieGoal");
        query.setParameter("calorieGoal",calorieGoal);
        return query.getResultList();
    }
    
    @GET
    @Path("findByUserIdReport/{userId}")
    @Produces({"application/json"})
    public List<Report> findByUserIdReport(@PathParam("userId") int userId) {
        TypedQuery<Report> query = em.createQuery("SELECT r.userId FROM Report r WHERE r.userId.userId = :userId", Report.class);
        query.setParameter("userId",userId);
        return query.getResultList();
    }
    
    @GET
    @Path("calorieGoalByDateAndByUserId/{userId}/{reportDate}")
    @Produces({"application/json"})
    public Object caloriesConsumedOnDate(@PathParam("userId")int userId,@PathParam("reportDate")String reportDate) {
        List<Object[]> queryList = em.createQuery("SELECT r.userId.userId,r.reportDate,r.caloriesConsumed,r.caloriesBurned,r.calorieGoal FROM Report r").getResultList();
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (Object[] row : queryList) 
        {
            JsonObject object ;
            if(userId == (int)row[0] && reportDate.equals((String)row[1]))
            {
                object = Json.createObjectBuilder()
                .add("Calories consumed: ",(int)row[2])
                .add("Calories burned: ",(int)row[3])
                .add("Remaining Calories to reach calorie goal: ",(int)row[4]-((int)row[2]-(int)row[3]))
                .add("Calorie goal",(int)row[4])
                .build();  
                arrayBuilder.add(object);
            }
        }
        JsonArray jArray= arrayBuilder.build();
        return jArray;
    }
    
    public boolean dateBetween(String date1,String dateReport, String date2)
    {
        LocalDate firstDate = LocalDate.parse(date1);  
        LocalDate reportDate = LocalDate.parse(dateReport);
        LocalDate secondDate = LocalDate.parse(date2);
        boolean result = false;
        
        if(reportDate.isAfter(firstDate) && reportDate.isBefore(secondDate) )
            result = true;
        
        else if(firstDate.compareTo(reportDate) == 0 && secondDate.compareTo(reportDate) == 0 )
            result = true;
        
        else 
            result = false;
        
        return result;
    }
    
    @GET
    @Path("caloriesByDateRangeAndByUserId/{userId}/{firstDate}/{secondDate}")
    @Produces({"application/json"})
    public Object caloriesConsumedForRangeOfDates(@PathParam("userId")int userId,@PathParam("firstDate")String firstDate,@PathParam("secondDate")String secondDate) {
        List<Object[]> queryList = em.createQuery("SELECT r.userId.userId,r.reportDate,r.caloriesConsumed,r.caloriesBurned,r.stepsTaken FROM Report r").getResultList();
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        int totalCaloriesConsumed = 0;
        int totalCaloriesBurned = 0;
        int totalStepsTaken = 0;
        for (Object[] row : queryList) 
        {  
            if(userId == (int)row[0] && dateBetween(firstDate,(String)row[1],secondDate))
            {
                totalCaloriesConsumed += (int)row[2];
                totalCaloriesBurned += (int)row[3];
                totalStepsTaken += (int)row[4];
            }
        }
        JsonObject object = Json.createObjectBuilder()
                .add("Total Calories consumed: ",totalCaloriesConsumed)
                .add("Total Calories burned: ",totalCaloriesBurned)
                .add("Total Steps Taken: ",totalStepsTaken)
                .build();  
                arrayBuilder.add(object);
        JsonArray jArray= arrayBuilder.build();
        return jArray;
    }
    
     @GET
    @Path("caloriesPerDayAndByUserId/{userId}/{firstDate}/{secondDate}")
    @Produces({"application/json"})
    public Object caloriesPerDayAndByUserId(@PathParam("userId")int userId,@PathParam("firstDate")String firstDate,@PathParam("secondDate")String secondDate) {
        List<Object[]> queryList = em.createQuery("SELECT r.userId.userId,r.reportDate,r.caloriesConsumed,r.caloriesBurned,r.stepsTaken FROM Report r").getResultList();
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        int totalCaloriesConsumed = 0;
        int totalCaloriesBurned = 0;
        int totalStepsTaken = 0;
        for (Object[] row : queryList) 
        {  
            if(userId == (int)row[0] && dateBetween(firstDate,(String)row[1],secondDate))
            {
                JsonObject object = Json.createObjectBuilder()
                .add("Total Calories consumed: ",(int)row [2])
                .add("Total Calories burned: ",(int)row [3])
                .add("Report Date: ",(String) row[1])
                .build();  
                arrayBuilder.add(object);
            }
        }
        
        JsonArray jArray= arrayBuilder.build();
        return jArray;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}
