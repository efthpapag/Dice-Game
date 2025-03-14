package gr.aueb.persistence;

import gr.aueb.domain.User;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.RequestScoped;

@RequestScoped
public class UserRepo implements PanacheRepositoryBase<User, String> {

    public User findByUsername(String username) {
        return find("username", username).firstResult();
    }
}
