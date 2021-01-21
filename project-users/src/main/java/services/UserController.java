package services;


import Entities.User;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

@Path("/users")
@ApplicationScoped
@OpenAPIDefinition(info = @Info(title = "User endpoint", version = "1.0"))
public class UserController {

    private static EntityManagerFactory factory;
    private EntityManager em;
    
    public UserController() {
        factory = Persistence.createEntityManagerFactory("users");
        em = factory.createEntityManager();
    }
    
    @APIResponses(value = {
        @APIResponse(
            responseCode = "200",
                description = "Register a new user",
                content = @Content(
                        mediaType = MediaType.APPLICATION_JSON,
                        schema = @Schema(
                                ref = "User",
                                implementation = User.class))
        )
    })
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/register")
    public User register(User user) {
        List<Object> users = em.createNamedQuery("User_findByUsername")
                   .setParameter("username", user.getUsername())
                   .getResultList();
        if (users.isEmpty()) {
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
            return user;
        }
        return null;
    }
    
    @APIResponses(value = {
        @APIResponse(
            responseCode = "200",
                description = "Logs an existing user in",
                content = @Content(
                        mediaType = MediaType.APPLICATION_JSON,
                        schema = @Schema(
                            ref = "User",
                            implementation = User.class
                        )
                )
        )
    })
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/login")
    public User login(User user) {
        User u = (User) em.createNamedQuery("User_findByUsername")
                   .setParameter("username", user.getUsername())
                   .getSingleResult();
        if (u.getPassword().equals(user.getPassword())) {
            return u;
        }
        return null;
    }
    
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @APIResponses(value = {
        @APIResponse(
            responseCode = "200",
                description = "Gets the information for an existing user",
                content = @Content(
                        mediaType = MediaType.APPLICATION_JSON,
                        schema = @Schema(
                                ref = "User",
                                implementation = User.class))
        )
    })
    public User getUser(@Parameter(description = "The id of a specific existing user", required = true) @PathParam("id") int id) {
//        em.getTransaction().begin();
        User u = em.find(User.class, id);
//        em.getTransaction().
        return u;
    }
}