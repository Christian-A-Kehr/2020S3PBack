package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.mindrot.jbcrypt.BCrypt;

@Entity
@Table(name = "users")
public class User implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "user_name", length = 25, unique = true)
    private String userName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "user_pass")
    private String userPass;
    @JoinTable(name = "user_roles", joinColumns =
    {
        @JoinColumn(name = "user_name", referencedColumnName = "user_name")
    }, inverseJoinColumns =
    {
        @JoinColumn(name = "role_name", referencedColumnName = "role_name")
    })
    @ManyToMany
    private List<Role> roleList = new ArrayList();
    
    @ManyToMany
    @JoinTable(name = "user_country")
    private Set<Country> countryTracked;

    public List<String> getRolesAsStrings()
    {
        if (roleList.isEmpty())
        {
            return null;
        }
        List<String> rolesAsStrings = new ArrayList();
        for (Role role : roleList)
        {
            rolesAsStrings.add(role.getRoleName());
        }
        return rolesAsStrings;
    }

    public User()
    {
    }

    //TODO Change when password is hashed
    public boolean verifyPassword(String pwIn)
    {
        return (BCrypt.checkpw(pwIn, userPass));
    }

    public User(String userName, String userPass)
    {
        this.userName = userName;
        String hashed = BCrypt.hashpw(userPass, BCrypt.gensalt());
        this.userPass = hashed;
//        this.countryTracked = new HashSet();
    }

    public static long getSerialVersionUID()
    {
        return serialVersionUID;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getUserPass()
    {
        return this.userPass;
    }

    public void setUserPass(String userPass)
    {
        String hashed = BCrypt.hashpw(userPass, BCrypt.gensalt());
        this.userPass = hashed;
    }

    public List<Role> getRoleList()
    {
        return roleList;
    }

    public void setRoleList(List<Role> roleList)
    {
        this.roleList = roleList;
    }

    public void addRole(Role userRole)
    {
        roleList.add(userRole);
    }

    public Set<Country> getCountryTracked()
    {
        return countryTracked;
    }

}
/*
// Hash a password for the first time
String hashed = BCrypt.hashpw(password, BCrypt.gensalt());

// gensalt's log_rounds parameter determines the complexity
// the work factor is 2**log_rounds, and the default is 10
String hashed = BCrypt.hashpw(password, BCrypt.gensalt(12));

// Check that an unencrypted password matches one that has
// previously been hashed
if (BCrypt.checkpw(candidate, hashed))
	System.out.println("It matches");
else
	System.out.println("It does not match");
 */
