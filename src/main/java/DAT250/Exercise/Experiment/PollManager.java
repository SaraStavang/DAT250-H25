package DAT250.Exercise.Experiment;

import DAT250.Exercise.Experiment.model.Poll;
import DAT250.Exercise.Experiment.model.User;
import DAT250.Exercise.Experiment.model.Vote;
import DAT250.Exercise.Experiment.model.VoteOption;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.*;


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
        User u = new User();
        u.setId(nextUserId++);
        u.setUsername(username);
        u.setEmail(email);
        users.put(username, u);
        return u;
    }

    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    public User getUser(String username) {
        User u = users.get(username);
        if (u == null) throw new NoSuchElementException("User not found: " + username);
        return u;
    }

    public List<Poll> getPolls() {
        return new ArrayList<>(polls.values());
    }

    public Poll getPoll(Long pollId) {
        Poll p = polls.get(pollId);
        if (p == null) throw new NoSuchElementException("Poll not found: " + pollId);
        return p;
    }

    public Poll createPoll(String creatorUsername, String question, boolean isPublic, LocalDate validUntil) {
        User creator = getUser(creatorUsername);

        Poll p = new Poll();
        p.setId(nextPollId++);
        p.setQuestion(question);
        p.setPublic(isPublic);
        p.setPublishedAt(Instant.now());

        if (validUntil != null) {
            Instant until = validUntil.atTime(23, 59, 59)
                    .atZone(ZoneOffset.UTC)
                    .toInstant();
            p.setValidUntil(until);
        }

        p.setCreator(creator);
        p.setOptions(new ArrayList<>());
        p.setVotes(new ArrayList<>());

        polls.put(p.getId(), p);

        if (creator.getCreatedPolls() == null) {
            creator.setCreatedPolls(new ArrayList<>());
        }
        creator.getCreatedPolls().add(p);

        return p;
    }

    public VoteOption addOption(Long pollId, String caption, int presentationOrder) {
        Poll p = getPoll(pollId);
        VoteOption o = new VoteOption();
        o.setId(nextOptionId++);
        o.setCaption(caption);
        o.setPresentationOrder(presentationOrder);
        o.setPoll(p);
        p.getOptions().add(o);
        return o;
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

        Vote v = new Vote();
        v.setId(nextVoteId++);
        v.setPublishedAt(Instant.now());
        v.setVoteOption(opt);
        v.setUser(voter);
        v.setPoll(p);

        p.getVotes().add(v);
        if (voter.getVotes() == null) voter.setVotes(new ArrayList<>());
        voter.getVotes().add(v);

        return v;
    }

    public Vote changeVote(Long voteId, Long newOptionId) {
        Vote target = null;
        Poll owner = null;

        for (Poll p : polls.values()) {
            for (Vote v : p.getVotes()) {
                if (v.getId().equals(voteId)) {
                    target = v;
                    owner = p;
                    break;
                }
            }
            if (target != null) break;
        }
        if (target == null) throw new NoSuchElementException("Vote not found: " + voteId);

        var opt = owner.getOptions().stream()
                .filter(o -> o.getId().equals(newOptionId))
                .findFirst();

        if (opt.isEmpty()) {
            throw new NoSuchElementException("Option " + newOptionId + " not in poll " + owner.getId());
        }
        target.setVoteOption(opt.get());
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

        if (p.getCreator() != null && p.getCreator().getCreatedPolls() != null) {
            p.getCreator().getCreatedPolls().remove(p);
        }
        p.getVotes().clear();
        p.getOptions().clear();
    }
}
