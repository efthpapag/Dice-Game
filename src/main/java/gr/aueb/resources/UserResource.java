package gr.aueb.resources;

import gr.aueb.persistence.UserRepo;
import gr.aueb.representation.UserMapper;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Path;

@RequestScoped
@Path("/users")
public class UserResource {

    @Inject
    UserRepo repo;

    @Inject
    UserMapper userMapper;





}
