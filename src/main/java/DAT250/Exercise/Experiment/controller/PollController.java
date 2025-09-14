package DAT250.Exercise.Experiment.controller;

import DAT250.Exercise.Experiment.PollManager;
import DAT250.Exercise.Experiment.PollResponse;
import DAT250.Exercise.Experiment.model.Poll;
import DAT250.Exercise.Experiment.model.VoteOption;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/polls")
@CrossOrigin(origins = { "http://localhost:8080" })
public class PollController {

    private final PollManager pollManager;

    public PollController(PollManager pollManager) {
        this.pollManager = pollManager;
    }

    public static class CreatePollRequest {
        public String question;
        public boolean isPublic = true;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        public LocalDate validUntil;
    }

    public static class CreateOptionRequest {
        @JsonAlias({ "text", "name" })
        public String caption;
        public int presentationOrder;
    }

    @PostMapping
    public ResponseEntity<Poll> createPoll(@RequestParam String username,
                                           @RequestBody CreatePollRequest body) {
        Poll created = pollManager.createPoll(username, body.question, body.isPublic, body.validUntil);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public List<Poll> getAllPolls() {
        return pollManager.getPolls();
    }

    @GetMapping("/{pollId}")
    public ResponseEntity<PollResponse> getPoll(@PathVariable Long pollId) {
        try {
            Poll poll = pollManager.getPoll(pollId);
            return ResponseEntity.ok(new PollResponse(poll));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{pollId}/options")
    public ResponseEntity<VoteOption> addOption(@PathVariable Long pollId,
                                                @RequestBody CreateOptionRequest req) {
        try {
            var created = pollManager.addOption(pollId, req.caption, req.presentationOrder);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{pollId}")
    public ResponseEntity<Void> deletePoll(@PathVariable Long pollId) {
        pollManager.deletePoll(pollId);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> badRequest(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
    }
}
