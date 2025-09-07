package DAT250.Exercise.Experiment.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Poll {
    private Long id;
    private String question;
    private Instant publishedAt;
    private Instant validUntil;
    private boolean isPublic;
    private boolean singleVotePerUser;


    private User creator;
    private List<VoteOption> options = new ArrayList<>();
    private List<Vote> votes = new ArrayList<>();

    public Poll() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }

    public Instant getPublishedAt() { return publishedAt; }
    public void setPublishedAt(Instant publishedAt) { this.publishedAt = publishedAt; }

    public Instant getValidUntil() { return validUntil; }
    public void setValidUntil(Instant validUntil) { this.validUntil = validUntil; }

    public boolean isPublic() { return isPublic; }
    public void setPublic(boolean isPublic) { this.isPublic = isPublic; }

    public boolean isSingleVotePerUser() { return singleVotePerUser; }
    public void setSingleVotePerUser(boolean singleVotePerUser) {
        this.singleVotePerUser = singleVotePerUser;
    }

    public User getCreator() { return creator; }
    public void setCreator(User creator) { this.creator = creator; }

    public List<VoteOption> getOptions() { return options; }
    public void setOptions(List<VoteOption> options) { this.options = options; }

    public List<Vote> getVotes() { return votes; }
    public void setVotes(List<Vote> votes) { this.votes = votes; }

}
