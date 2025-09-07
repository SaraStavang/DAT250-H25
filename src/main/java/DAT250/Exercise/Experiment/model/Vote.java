package DAT250.Exercise.Experiment.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.time.Instant;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Vote {
    private Long id;
    private Instant publishedAt;
    private Poll poll;
    private VoteOption voteOption;
    private User user;

    public Vote(){}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Instant getPublishedAt() { return publishedAt; }
    public void setPublishedAt(Instant publishedAt) { this.publishedAt = publishedAt; }

    public Poll getPoll() { return poll; }
    public void setPoll(Poll poll) { this.poll = poll; }

    public VoteOption getVoteOption() { return voteOption; }
    public void setVoteOption(VoteOption voteOption) { this.voteOption = voteOption; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

}
