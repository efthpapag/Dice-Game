package gr.aueb.resources;

import gr.aueb.domain.User;
import gr.aueb.persistence.UserRepo;
import gr.aueb.representation.StringRepresentation;
import gr.aueb.representation.UserMapper;
import gr.aueb.representation.UserRepresentation;
import gr.aueb.security.jwt.JwtService;
import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.annotation.security.PermitAll;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Status;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;

import java.net.URI;

@RequestScoped
@Path("/users")
public class UserResource {

    @Inject
    UserRepo repo;

    @Inject
    UserMapper userMapper;

    @Inject
    JwtService service;

    @POST
    @Transactional
    @PermitAll
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/login")
    public Response login(UserRepresentation representation) {
        //User user = repo.findById(representation.username);
        User user = repo.findByUsername(representation.username);
        if (user == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }else if(BcryptUtil.matches(representation.password,user.getPassword())) {
            String jwt = service.generateJwt(user.getUsername());
            user.setAttempts(3);
            StringRepresentation res = new StringRepresentation();
            res.value=jwt;
            return Response.ok().entity(res).build();

        }else{
            user.setAttempts(user.getAttempts() - 1);
            Response.Status response = Response.Status.UNAUTHORIZED;
            if (user.getAttempts() == 0) {
                repo.delete(user);
                response = Response.Status.GONE;
            }
            return Response.status(response).build();
        }
    }

    @POST
    @Transactional
    @PermitAll
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/register")
    public Response register(UserRepresentation representation) {
        User user = repo.findByUsername(representation.username);
        if (user != null){
            return Response.status(Response.Status.FORBIDDEN).entity("The user already exists").build();
        }
        else if (!isValidFirstName(representation.firstName)) {
            return Response.status(Response.Status.FORBIDDEN).entity("Invalid first name").build();
        }
        else if (!isValidLastName(representation.lastName)) {
            return Response.status(Response.Status.FORBIDDEN).entity("Invalid last name").build();
        }
        else if (!isValidUsername(representation.username)) {
            return Response.status(Response.Status.FORBIDDEN).entity("Invalid username").build();
        }
        else if (!isValidPassword(representation.password)) {
            return Response.status(Response.Status.FORBIDDEN).entity("Invalid password").build();
        } else{
            User newUser = new User(representation.username, representation.password, representation.firstName, representation.lastName);
            repo.persist(newUser);
            URI uri = UriBuilder.fromResource(UserResource.class).path(String.valueOf(newUser.getUsername())).build();
            return Response.created(uri).entity(userMapper.toRepresentation(newUser)).build();
        }
    }

    public static boolean isValidPassword(String input) {
        // Regular expression to check:
        // - At least one uppercase letter: (?=.[A-Z])
        // - At least one lowercase letter: (?=.[a-z])
        // - At least one special character: (?=.[@#$%^&+=!])
        String pattern = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[@#$%^&+=!]).{8,60}$";

        return input.matches(pattern) && !input.isEmpty();
    }

    public static boolean isValidUsername(String input) {
        return input.length() <= 60 && input.matches("^[a-zA-Z0-9_]+$") && !input.isEmpty();
    }

    public static boolean isValidFirstName(String input){
        return input.length() <= 60 && input.matches("^[a-zA-Z0-9_]+$") && !input.isEmpty();
    }

    public static boolean isValidLastName(String input){
        return input.length() <= 60 && input.matches("^[a-zA-Z0-9_]+$") && !input.isEmpty();
    }

}
