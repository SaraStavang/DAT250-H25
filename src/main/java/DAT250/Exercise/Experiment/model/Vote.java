package DAT250.Exercise.Experiment.model;
import java.time.Instant;
import jakarta.persistence.*;

@Entity
@Table(name = "votes")
public class Vote {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "voted_by_id", nullable = false)
    private User votedBy;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "option_id", nullable = false)
    private VoteOption votesOn;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "poll_id", nullable = false)
    private Poll poll;

    private Instant publishedAt;

    protected Vote() {}

    public Vote(User votedBy, VoteOption votesOn) {
        this.votedBy = votedBy;
        this.votesOn = votesOn;
        this.poll = votesOn.getPoll();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getVotedBy() { return votedBy; }
    public void setVotedBy(User votedBy) { this.votedBy = votedBy; }

    public VoteOption getVotesOn() { return votesOn; }

    public void setVotesOn(VoteOption votesOn) {
        this.votesOn = votesOn;
        this.poll = (votesOn != null) ? votesOn.getPoll() : null;
    }
    public Poll getPoll() { return poll; }

    public Instant getPublishedAt() { return publishedAt; }
    public void setPublishedAt(Instant publishedAt) { this.publishedAt = publishedAt; }

}
