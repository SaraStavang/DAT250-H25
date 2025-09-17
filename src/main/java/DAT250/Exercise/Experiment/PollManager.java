package DAT250.Exercise.Experiment;
import DAT250.Exercise.Experiment.model.Poll;
import DAT250.Exercise.Experiment.model.User;
import DAT250.Exercise.Experiment.model.Vote;
import DAT250.Exercise.Experiment.model.VoteOption;
import org.springframework.stereotype.Component;
import java.util.LinkedHashSet;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.*;
import java.util.Objects;

@Component
public class PollManager {

    private final Map<String, User> users = new HashMap<>();
    private final Map<Long, Poll> polls = new HashMap<>();

    private long nextUserId = 1;
    private long nextPollId = 1;
    private long nextOptionId = 1;
    private long nextVoteId = 1;

    public User createUser(String username, String email) {
        if (users.containsKey(username)) {
            throw new IllegalArgumentException("Username already exists: " + username);
        }

        User u = new User(username,email);
        u.setId(nextUserId++);
        u.setUsername(username);
        u.setEmail(email);
        users.put(username, u);
        return u;
    }

    public List<User> getUsers() { return new ArrayList<>(users.values()); }

    public User getUser(String username) {
        User u = users.get(username);
        if (u == null) throw new NoSuchElementException("User not found: " + username);
        return u;
    }

    public List<Poll> getPolls() { return new ArrayList<>(polls.values()); }

    public Poll getPoll(Long pollId) {
        Poll p = polls.get(pollId);
        if (p == null) throw new NoSuchElementException("Poll not found: " + pollId);
        return p;
    }

    public Poll createPoll(String creatorUsername, String question, boolean isPublic, LocalDate validUntil) {
        User creator = getUser(creatorUsername);

        Poll p = creator.createPoll(question);

        p.setId(nextPollId++);
        p.setPublic(isPublic);
        p.setPublishedAt(Instant.now());

        if (validUntil != null) {
            Instant until = validUntil.atTime(23, 59, 59)
                    .atZone(ZoneOffset.UTC)
                    .toInstant();
            p.setValidUntil(until);
        }

        if (creator.getCreated() == null) {
            creator.setCreated(new LinkedHashSet<>());
        }

        creator.getCreated().add(p);

        polls.put(p.getId(), p);
        return p;
    }

    public List<VoteOption> getOptions(Long pollId) {
        return new ArrayList<>(getPoll(pollId).getOptions());
    }

    public Vote castVote(String username, Long pollId, Long optionId) {
        User voter = getUser(username);
        Poll p = getPoll(pollId);

        VoteOption opt = p.getOptions().stream()
                .filter(o -> o.getId().equals(optionId))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Option " + optionId + " not in poll " + pollId));

        Vote v = new Vote(voter, opt);
        v.setId(nextVoteId++);
        v.setPublishedAt(Instant.now());

        p.getVotes().add(v);
        if (voter.getVotes() == null){voter.setVotes(new LinkedHashSet<>());}
        voter.getVotes().add(v);

        return v;
    }

    public Vote changeVote(Long voteId, Long newOptionId) {
        Vote target = null;
        Poll owner = null;

        // find the vote and its poll
        outer:
        for (Poll p : polls.values()) {
            for (Vote v : p.getVotes()) {
                if (Objects.equals(v.getId(), voteId)) {
                    target = v;
                    owner = p;
                    break outer;
                }
            }
        }
        if (target == null) {
            throw new NoSuchElementException("Vote not found: " + voteId);
        }

        // find the new option in the same poll
        VoteOption opt = null;
        for (VoteOption o : owner.getOptions()) {
            if (Objects.equals(o.getId(), newOptionId)) {
                opt = o;
                break;
            }
        }
        if (opt == null) {
            throw new NoSuchElementException(
                    "Option " + newOptionId + " not in poll " + owner.getId()
            );
        }

        target.setVotesOn(opt);
        return target;
    }
    public List<Vote> getVotesForPoll(Long pollId) {
        return new ArrayList<>(getPoll(pollId).getVotes());
    }

    public List<Vote> getAllVotes() {
        List<Vote> all = new ArrayList<>();
        for (Poll p : polls.values()) all.addAll(p.getVotes());
        return all;
    }

    public void deletePoll(Long pollId) {
        Poll p = polls.remove(pollId);
        if (p == null) return;

        if (p.getCreatedBy() != null && p.getCreatedBy().getCreated() != null) {
            p.getCreatedBy().getCreated().remove(p);
        }
        p.getVotes().clear();
        p.getOptions().clear();
    }

    public VoteOption addOption(Long pollId, String caption, int presentationOrder) {
        Poll p = getPoll(pollId);


        int expectedOrder = p.getOptions().size();
        if (presentationOrder != expectedOrder) {
            throw new IllegalArgumentException(
                    "presentationOrder must be " + expectedOrder + " for poll " + p.getId());
        }

        VoteOption opt = p.addVoteOption(caption);

        opt.setId(nextOptionId++);

        return opt;
    }
}
