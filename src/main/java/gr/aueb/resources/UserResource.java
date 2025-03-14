package gr.aueb.resources;

import gr.aueb.domain.User;
import gr.aueb.persistence.UserRepo;
import gr.aueb.representation.UserMapper;
import gr.aueb.representation.UserRepresentation;
import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.annotation.security.PermitAll;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
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

    @POST
    @Transactional
    @PermitAll
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/login")
    public Response login(UserRepresentation representation) {
        User user = repo.findById(representation.username);
        if (user == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }else if(BcryptUtil.matches(representation.password,user.getPassword())) {
            /*String jwt = service.generateJwt();
            JwtRepresentation res = new JwtRepresentation();
            res.jwt=jwt;*/
            //return Response.ok().entity(res).build();
            return Response.ok().build();
        }else{
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    @POST
    @Transactional
    @PermitAll
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/register")
    public Response register(UserRepresentation representation) {
        User user = repo.findById(representation.username);
        if (user == null) {
            User newUser = new User(representation.username, representation.password, representation.firstName, representation.lastName);
            repo.persist(newUser);
            URI uri = UriBuilder.fromResource(UserResource.class).path(String.valueOf(newUser.getUsername())).build();
            return Response.created(uri).entity(userMapper.toRepresentation(newUser)).build();
        }else{
            return Response.status(Response.Status.CONFLICT).build();
        }
    }



}
