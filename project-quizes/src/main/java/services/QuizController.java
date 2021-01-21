
package services;

import Entities.Answer;
import Entities.Question;
import Entities.Quiz;
import com.mycompany.project.quizes.User;
import com.mycompany.project.quizes.UserControllerIntf;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@Path("/quizes")
@ApplicationScoped
@OpenAPIDefinition(info = @Info(title = "Quizes endpoints", version = "1.0"))
public class QuizController {
    EntityManager em;
    
    @Inject
    @ConfigProperty(name = "usersURL")
    private String usersUrl;
    
    public QuizController() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("quizesPU");
        em = factory.createEntityManager();
    }
    
    private boolean isUserAdmin(int userId) throws MalformedURLException, IOException, ClassNotFoundException {
        URI url = URI.create(usersUrl);
        UserControllerIntf service = RestClientBuilder.newBuilder()
                            .baseUri(url)
                            .build(UserControllerIntf.class);
        User u = service.getUser(userId);
        
        return u.getIsAdmin();
    }
    
    @APIResponses(value = {
        @APIResponse(
            responseCode = "200",
                description = "Get a quiz by it's id",
                content = @Content(
                        mediaType = MediaType.APPLICATION_JSON,
                        schema = @Schema(
                                ref = "Quiz",
                                implementation = Quiz.class))
        )
    })
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Quiz getQuiz(@PathParam("id") int id) {
        Quiz q = em.find(Quiz.class, id);
        return q;
    }
    
    @APIResponses(value = {
        @APIResponse(
            responseCode = "200",
                description = "Get a quiz's questions by the quiz's id",
                content = @Content(
                        mediaType = MediaType.APPLICATION_JSON,
                        schema = @Schema(
                                type = SchemaType.ARRAY,
                                implementation = Question.class))
        )
    })
    @GET
    @Path("/questions/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Question> getQuizQuestions(@PathParam("id") int id) {
        List<Question> questions = em.createNamedQuery("question_getByQuizId")
           .setParameter("id", id)
           .getResultList();
        
        return questions;
    }
    
    @APIResponses(value = {
        @APIResponse(
            responseCode = "200",
                description = "Get a question's answers by the question's id",
                content = @Content(
                        mediaType = MediaType.APPLICATION_JSON,
                        schema = @Schema(
                                type = SchemaType.ARRAY,
                                implementation = Answer.class))
        )
    })
    @GET
    @Path("/answers/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Answer> getQuestionAnswers(@PathParam("id") int id) {
        List<Answer> answers = em.createNamedQuery("answer_getByQuestionId")
           .setParameter("id", id)
           .getResultList();
        
        return answers;
    }
    
    @APIResponses(value = {
        @APIResponse(
            responseCode = "200",
                description = "Add a new quiz",
                content = @Content(
                        mediaType = MediaType.APPLICATION_JSON,
                        schema = @Schema(
                                ref = "quiz",
                                implementation = Quiz.class))
        )
    })
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON_PATCH_JSON)
    public Quiz addQuiz(@HeaderParam("UserId") int userId ,Quiz quiz) throws IOException, MalformedURLException, ClassNotFoundException {
        if (!this.isUserAdmin(userId)) {
            throw new Error("User doesn't have the rights");
        }
        
        em.getTransaction().begin();
        em.persist(quiz);
        em.close();
        return quiz;
    }
    
    @APIResponses(value = {
        @APIResponse(
            responseCode = "200",
                description = "Add a new question to a quiz",
                content = @Content(
                        mediaType = MediaType.APPLICATION_JSON,
                        schema = @Schema(
                                ref = "quiz",
                                implementation = Quiz.class))
        )
    })
    @PUT
    @Path("/{id}/addQuestion")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Quiz addQuestion(@PathParam("id") int id, @QueryParam("UserId") int userId, Question question)
        throws IOException, MalformedURLException, ClassNotFoundException {
        try {
            if (!this.isUserAdmin(userId)) {
                throw new Error("User doesn't have the rights");
            }

            em.getTransaction().begin();
            Quiz q = em.find(Quiz.class, id);
            question.setQuizId(q.getId());
            em.getTransaction().commit();
            q.addQuestion(question);
            return q; 
        }
        catch(MalformedURLException e) {
            return null;
        }
        catch(ClassNotFoundException e) {
            return null;
        }
        catch(IOException e) {
            return null;
        }
    }
    
    @APIResponses(value = {
        @APIResponse(
            responseCode = "200",
                description = "Delete an existing quiz",
                content = @Content(
                        mediaType = MediaType.APPLICATION_JSON,
                        schema = @Schema(
                                type = SchemaType.BOOLEAN))
        )
    })
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public boolean deleteQuiz(@PathParam("id") int id, @HeaderParam("UserId") int userId) throws IOException, MalformedURLException, ClassNotFoundException {
        if (!this.isUserAdmin(userId)) {
            throw new Error("User doesn't have the rights");
        }
        
        em.getTransaction().begin();
        Quiz q = em.find(Quiz.class, id);
        em.remove(q);
        em.getTransaction().commit();
        return true;
    }
}
