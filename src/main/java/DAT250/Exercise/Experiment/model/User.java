package DAT250.Exercise.Experiment.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.util.ArrayList;
import java.util.List;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class User {
    private Long id;
    private String username;
    private String password;
    private String email;

    private List<Poll> createdPolls = new ArrayList<>();
    private List<Vote> votes = new ArrayList<>();

    public User() {}


    public Long getId() {
        return id;
    }
    public void setId(Long id) { this.id = id; }

    public String getUsername() {return username;}
    public void setUsername(String username) { this.username = username; }

    public String getPassword() {return password;}
    public void setPassword(String password) { this.password = password; }

    public String getEmail() {return email;}
    public void setEmail(String email) { this.email = email; }

    public List<Poll> getCreatedPolls() { return createdPolls; }
    public void setCreatedPolls(List<Poll> createdPolls) { this.createdPolls = createdPolls; }

    public List<Vote> getVotes() { return votes; }
    public void setVotes(List<Vote> votes) { this.votes = votes; }
}
