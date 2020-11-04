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
import restws.Credential;

/**
 *
 * @author ganesh
 */
@Stateless
@Path("restws.credential")
public class CredentialFacadeREST extends AbstractFacade<Credential> {

    @PersistenceContext(unitName = "Test1PU")
    private EntityManager em;

    public CredentialFacadeREST() {
        super(Credential.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Credential entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") String id, Credential entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") String id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Credential find(@PathParam("id") String id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Credential> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Credential> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }
    
    @GET
    @Path("findByUserName/{userName}")
    @Produces({"application/json"})
    public List<Credential> findByUserName(@PathParam("userName") String userName) {
        Query query = em.createNamedQuery("Credential.findByUserName");
        query.setParameter("userName",userName);
        return query.getResultList();
    }
    
    @GET
    @Path("findByPasswordHash/{passwordHash}")
    @Produces({"application/json"})
    public List<Credential> findByPasswordHash(@PathParam("passwordHash") String passwordHash) {
        Query query = em.createNamedQuery("Credential.findByPasswordHash");
        query.setParameter("passwordHash",passwordHash);
        return query.getResultList();
    }
    
    @GET
    @Path("findBySignupDate/{signupDate}")
    @Produces({"application/json"})
    public List<Credential> findBySignupDate(@PathParam("signupDate") String signupDate) {
        Query query = em.createNamedQuery("Credential.findBySignupDate");
        query.setParameter("signupDate",signupDate);
        return query.getResultList();
    }
    
    @GET
    @Path("findByUserIdCredential/{userId}")
    @Produces({"application/json"})
    public List<Credential> findByUserIdCredential(@PathParam("userId") int userId) {
        TypedQuery<Credential> query = em.createQuery("SELECT c.userId FROM Credential c WHERE c.userId.userId = :userId", Credential.class);
        query.setParameter("userId",userId);
        return query.getResultList();
    }
    
    @GET
    @Path("findByFirstNameByUserIdCredential/{firstName}")
    @Produces({"application/json"})
    public List<Credential> findByFirstNameByUserIdCredential(@PathParam("firstName") String firstName) {
        TypedQuery<Credential> query = em.createQuery("SELECT c.userId FROM Credential c WHERE c.userId.firstName = :firstName", Credential.class);
        query.setParameter("firstName",firstName);
        return query.getResultList();
    }
    
    @GET
    @Path("findEmailByUserIdCredential/{email}")
    @Produces({"application/json"})
    public List<Credential> findEmailByUserIdCredential(@PathParam("email") String email) {
        Query query = em.createNamedQuery("Credential.findEmailByUserIdCredential");
        query.setParameter("email",email);
        return query.getResultList();
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}
