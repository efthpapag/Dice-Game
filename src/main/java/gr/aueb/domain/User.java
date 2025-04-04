package gr.aueb.domain;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.security.jpa.Password;
import io.quarkus.security.jpa.UserDefinition;
import io.quarkus.security.jpa.Username;
import io.smallrye.common.constraint.NotNull;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "users")
//@UserDefinition
public class User {

    @Id
    @Username
    @NotNull
    @Pattern(regexp = "^[a-zA-Z0-9_]+$")
    @Column(name = "username", length = 20, unique = true)
    public String username;

    @NotNull
    @Password
    @Pattern(regexp = "^[a-zA-Z0-9_]+$")
    @Column(name = "password", length = 60)
    public String password;

    @Column(name = "firstname", length = 60)
    @Pattern(regexp = "^[a-zA-Z0-9_]+$")
    private String firstName;

    @Column(name = "lastname", length = 60)
    @Pattern(regexp = "^[a-zA-Z0-9_]+$")
    private String lastName;

    @Column(name = "attempts")
    private int attempts;

    public User() {}

    public User(String username, String password, String firstName, String lastName) {
        this.username = username;
        this.password = BcryptUtil.bcryptHash(password);
        this.firstName = firstName;
        this.lastName = lastName;
        this.attempts = 3;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public int getAttempts() { return attempts; }

    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setAttempts(int attempts) { this.attempts = attempts; }
}
