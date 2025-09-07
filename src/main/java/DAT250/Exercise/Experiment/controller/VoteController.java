package DAT250.Exercise.Experiment.controller;

import DAT250.Exercise.Experiment.PollManager;
import DAT250.Exercise.Experiment.model.Vote;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/polls/{pollId}/votes")
@CrossOrigin(origins = { "http://localhost:8080" })
public class VoteController {

    private final PollManager pollManager;

    public VoteController(PollManager pollManager) {
        this.pollManager = pollManager;
    }

    // ===== DTOs =====
    public static class CastVoteRequest {
        public String username;
        public Long optionId;
    }
    public static class ChangeVoteRequest {
        public Long newOptionId;
    }
    public static class VoteDto {
        public Long id;
        public Long pollId;
        public Long optionId;
        public Long userId;
        public Instant publishedAt;
        public static VoteDto of(Vote v) {
            var d = new VoteDto();
            d.id = v.getId();
            d.pollId = v.getPoll() != null ? v.getPoll().getId() : null;
            d.optionId = v.getVoteOption() != null ? v.getVoteOption().getId() : null;
            d.userId = v.getUser() != null ? v.getUser().getId() : null;
            d.publishedAt = v.getPublishedAt();
            return d;
        }
    }

    @PostMapping
    public ResponseEntity<?> cast(@PathVariable Long pollId, @RequestBody CastVoteRequest req) {
        try {
            Vote v = pollManager.castVote(req.username, pollId, req.optionId);
            return ResponseEntity.status(HttpStatus.CREATED).body(VoteDto.of(v));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Return [] if the poll doesn't exist (so your scenario "list votes -> empty" after delete works)
    @GetMapping
    public ResponseEntity<List<VoteDto>> list(@PathVariable Long pollId) {
        try {
            var votes = pollManager.getVotesForPoll(pollId).stream().map(VoteDto::of).toList();
            return ResponseEntity.ok(votes);
        } catch (NoSuchElementException e) {
            return ResponseEntity.ok(List.of());
        }
    }

    @GetMapping("/count")
    public ResponseEntity<Map<String, Object>> count(@PathVariable Long pollId) {
        try {
            int c = pollManager.getVotesForPoll(pollId).size();
            return ResponseEntity.ok(Map.of("pollId", pollId, "count", c));
        } catch (NoSuchElementException e) {
            return ResponseEntity.ok(Map.of("pollId", pollId, "count", 0));
        }
    }

    @PatchMapping("/{voteId}")
    public ResponseEntity<?> change(@PathVariable Long pollId,
                                    @PathVariable Long voteId,
                                    @RequestBody ChangeVoteRequest req) {
        try {
            Vote v = pollManager.changeVote(voteId, req.newOptionId);
            return ResponseEntity.ok(VoteDto.of(v));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
