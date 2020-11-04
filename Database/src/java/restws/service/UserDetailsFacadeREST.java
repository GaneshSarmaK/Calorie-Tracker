/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package restws.service;

import java.time.LocalDate;
import java.time.Period;
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
import restws.UserDetails;

/**
 *
 * @author ganesh
 */
@Stateless
@Path("restws.userdetails")
public class UserDetailsFacadeREST extends AbstractFacade<UserDetails> {

    @PersistenceContext(unitName = "Test1PU")
    private EntityManager em;

    public UserDetailsFacadeREST() {
        super(UserDetails.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(UserDetails entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, UserDetails entity) {
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
    public UserDetails find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<UserDetails> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<UserDetails> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }

    @GET
    @Path("findByFirstName/{firstName}")
    @Produces({"application/json"})
    public List<UserDetails> findByFirstName(@PathParam("firstName") String firstName) {
        Query query = em.createNamedQuery("UserDetails.findByFirstName");
        query.setParameter("firstName",firstName);
        return query.getResultList();
    }
    
    @GET
    @Path("findByLastName/{lastName}")
    @Produces({"application/json"})
    public List<UserDetails> findByLastname(@PathParam("lastName") String lastName) {
        Query query = em.createNamedQuery("UserDetails.findByLastName");
        query.setParameter("lastName",lastName);
        return query.getResultList();
    }
    
    
    @GET
    @Path("findByEmail/{email}")
    @Produces({"application/json"})
    public List<UserDetails> findByEmail(@PathParam("email") String email) {
        Query query = em.createNamedQuery("UserDetails.findByEmail");
        query.setParameter("email",email);
        return query.getResultList();
    }
    
    
    @GET
    @Path("findByDateOfBirth/{dateOfBirth}")
    @Produces({"application/json"})
    public List<UserDetails> findByDateOfBirth(@PathParam("dateOfBirth") String dateOfBirth) {
        Query query = em.createNamedQuery("UserDetails.findByDateOfBirth");
        query.setParameter("dateOfBirth",dateOfBirth);
        return query.getResultList();
    }
    
    
    
    @GET
    @Path("findByHeight/{height}")
    @Produces({"application/json"})
    public List<UserDetails> findByHeight(@PathParam("height") int height) {
        Query query = em.createNamedQuery("UserDetails.findByHeight");
        query.setParameter("height",height);
        return query.getResultList();
    }

    @GET
    @Path("findByWeight/{weight}")
    @Produces({"application/json"})
    public List<UserDetails> findByWeight(@PathParam("weight") int weight) {
        Query query = em.createNamedQuery("UserDetails.findByWeight");
        query.setParameter("weight",weight);
        return query.getResultList();
    }
    
    
    @GET
    @Path("findByGender/{gender}")
    @Produces({"application/json"})
    public List<UserDetails> findByGender(@PathParam("gender") String gender) {
        Query query = em.createNamedQuery("UserDetails.findByGender");
        query.setParameter("gender",gender);
        return query.getResultList();
    }
    
    
    @GET
    @Path("findByAddress/{address}")
    @Produces({"application/json"})
    public List<UserDetails> findByAddress(@PathParam("address") String address) {
        Query query = em.createNamedQuery("UserDetails.findByAddress");
        query.setParameter("address",address);
        return query.getResultList();
    }
    
    
    @GET
    @Path("findByPostCode/{postCode}")
    @Produces({"application/json"})
    public List<UserDetails> findByPostCode(@PathParam("postCode") int postCode) {
        Query query = em.createNamedQuery("UserDetails.findByPostCode");
        query.setParameter("postCode",postCode);
        return query.getResultList();
    }
    
    
    @GET
    @Path("findByLevelOfActivity/{levelOfActivity}")
    @Produces({"application/json"})
    public List<UserDetails> findByLevelOfActivity(@PathParam("levelOfActivity") int levelOfActivity) {
        Query query = em.createNamedQuery("UserDetails.findByLevelOfActivity");
        query.setParameter("levelOfActivity",levelOfActivity);
        return query.getResultList();
    }
    
    
    @GET
    @Path("findByStepsPerMile/{stepsPerMile}")
    @Produces({"application/json"})
    public List<UserDetails> findByStepsPerMile(@PathParam("stepsPerMile") int stepsPerMile) {
        Query query = em.createNamedQuery("UserDetails.findByStepsPerMile");
        query.setParameter("stepsPerMile",stepsPerMile);
        return query.getResultList();
    }
    
    @GET
    @Path("findByFirstNameAndLastNameDynamic/{firstName}/{lastName}")
    @Produces({"application/json"})
    public List<UserDetails> findByFirstNameAndLastNameDynamic(@PathParam("firstName") String firstName,@PathParam("lastName") String lastName) {
        TypedQuery<UserDetails> query = em.createQuery("SELECT u FROM UserDetails u WHERE u.firstName = :firstName AND u.lastName = :lastName", UserDetails.class);
        query.setParameter("firstName",firstName);
        query.setParameter("lastName",lastName);
        return query.getResultList();
    }
    
    @GET
    @Path("calculateCaloriesBurntPerStep/{userId}")
    @Produces({"application/json"})
    public Object calculateCaloriesBurntPerStep(@PathParam("userId")int userId) {
        List<Object[]> queryList = em.createQuery("SELECT u.userId,u.stepsPerMile,u.weight FROM UserDetails u").getResultList();
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (Object[] row : queryList) {
            JsonObject object ;
            if(userId == (int)row[0]){
             object = Json.createObjectBuilder()
                    .add("Calories burnt per step:", (int)row[2]*0.49*2.2/(int)row[1])
                    .build();
            arrayBuilder.add(object);}
        }
        JsonArray jArray= arrayBuilder.build();
        return jArray;
    }
    
    
    public int calculateAge(String date)
    {
        LocalDate dob = LocalDate.parse(date);  
        LocalDate currentDate = LocalDate.now();
        return Period.between(dob, currentDate).getYears();
    }

    @GET
    @Path("calculateBMR/{userId}")
    @Produces({"application/json"})
    public String calculateBMR(@PathParam("userId")int userId) {
        List<Object[]> queryList = em.createQuery("SELECT u.userId,u.height,u.weight,u.gender,u.dateOfBirth FROM UserDetails u").getResultList();
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (Object[] row : queryList) {
            JsonObject object ;
            int age = calculateAge((String)row[4]);
            double bmr = 0.0;
            if(userId == (int)row[0])
            {
                if("male".equals((String)row[3]))
                {
                    bmr = ((int)row[1]*5.003)+((int)row[2]*13.75)-(6.755*age)+66.5;
                }
                else
                {
                    bmr = ((int)row[1]*1.85)+((int)row[2]*9.563)-(4.676*age)+655.1;
                    
                }
                object = Json.createObjectBuilder()
                    .add("Bmr:", bmr)
                    .build();
            arrayBuilder.add(object);
            }
        }
        JsonArray jArray= arrayBuilder.build();
        return jArray.getJsonObject(0).get("Bmr:").toString();
    }
    
    
    @GET
    @Path("calculateCaloriesBurntAtRest/{userId}")
    @Produces({"application/json"})
    public Object calculateCaloriesBurntAtRest(@PathParam("userId")int userId) {
        List<Object[]> queryList = em.createQuery("SELECT u.userId,u.levelOfActivity FROM UserDetails u").getResultList();
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (Object[] row : queryList) 
        {
            JsonObject object ;
            if(userId == (int)row[0])
            {
                  
                double  bmr= Double.parseDouble(calculateBMR((int)row[0]));
                double caloriesBurntAtRest = 0.0;
                switch((int)row[1])
                {
                    case 1:
                            caloriesBurntAtRest = bmr*1.2;
                            break;
                    case 2:
                            caloriesBurntAtRest = bmr*1.375;
                            break;
                    case 3:
                            caloriesBurntAtRest = bmr*1.55;
                            break;
                    case 4:
                            caloriesBurntAtRest = bmr*1.725;
                            break;
                    case 5:
                            caloriesBurntAtRest = bmr*1.9;
                            break;
                    default:
                            ;
                }
                object = Json.createObjectBuilder()
                    .add("Calories burnt at Rest:", caloriesBurntAtRest)
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
