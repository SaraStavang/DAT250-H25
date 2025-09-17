package DAT250.Exercise.Experiment.model;

import jakarta.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "vote_options")
public class
VoteOption {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String caption;

    @Column(nullable = false)
    private int presentationOrder;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "poll_id", nullable = false)
    private Poll poll;

    @OneToMany(mappedBy = "votesOn", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Vote> votes = new LinkedHashSet<>();

    protected VoteOption() {}

    public VoteOption(Poll poll, String caption, int presentationOrder) {
        this.poll = poll;
        this.caption = caption;
        this.presentationOrder = presentationOrder;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCaption() { return caption; }
    public int getPresentationOrder() { return presentationOrder; }
    public Poll getPoll() { return poll; }
    public Set<Vote> getVotes() { return votes; }
}
