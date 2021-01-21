package Entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import org.hibernate.annotations.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity(name="users")
@Table(name="users")
@NamedQuery(name="User_findByUsername", query="FROM users where username = :username")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    @Column(name = "id")
    private int id;
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;
    @Column(name = "isAdmin")
    private boolean isAdmin;
    
    public User() {}
    
    public User(String username, String password, boolean isAdmin) {
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
    }
    
    public int getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getUsername() {
        return this.username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return this.password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public boolean getIsAdmin() {
        return this.isAdmin;
    }
    
    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    } 
}
