package DAT250.Exercise.Experiment.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.*;

@RestController
public class RootController {

    @GetMapping("/")
    public Map<String, Object> index() {
        Map<String, Object> out = new LinkedHashMap<>();
        out.put("name", "DAT250 Polls API");
        out.put("baseUrl", "/api");
        out.put("timestamp", Instant.now().toString());

        List<Map<String, Object>> groups = new ArrayList<>();

        groups.add(group("users", Arrays.asList(
                entry("POST", "/api/users", "Create user",
                        "{ \"username\": \"alice\", \"email\": \"alice@example.com\" }"),
                entry("GET", "/api/users", "List all users", null),
                entry("GET", "/api/users/{username}", "Get one user by username", null)
        )));

        groups.add(group("polls", Arrays.asList(
                entry("POST", "/api/polls?username={creator}",
                        "Create poll (creator via query param)",
                        "{ \"question\": \"Favorite color?\", \"isPublic\": true, \"validUntil\": \"2030-01-01\" }"),
                entry("POST", "/api/polls/{pollId}/options",
                        "Add option to a poll",
                        "{ \"caption\": \"Blue\", \"presentationOrder\": 1 }"),
                entry("GET", "/api/polls", "List all polls", null),
                entry("GET", "/api/polls/{pollId}", "Get poll by id", null),
                entry("DELETE", "/api/polls/{pollId}", "Delete a poll", null)
        )));

        groups.add(group("votes", Arrays.asList(
                entry("POST", "/api/polls/{pollId}/votes",
                        "Cast vote (JSON body)",
                        "{ \"username\": \"bob\", \"optionId\": 2 }"),
                entry("PATCH", "/api/polls/{pollId}/votes/{voteId}",
                        "Change vote to another option",
                        "{ \"newOptionId\": 3 }"),
                entry("GET", "/api/polls/{pollId}/votes",
                        "List votes for a poll (returns [] if poll deleted)", null),
                entry("GET", "/api/polls/{pollId}/votes/count",
                        "Vote count for a poll", null)
        )));

        out.put("endpoints", groups);
        out.put("hint", "Use Content-Type: application/json for POST/PATCH bodies.");
        return out;
    }

    private Map<String, Object> entry(String method, String path, String desc, String exampleBody) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("method", method);
        m.put("path", path);
        m.put("desc", desc);
        if (exampleBody != null) m.put("exampleBody", exampleBody);
        return m;
    }

    private Map<String, Object> group(String name, List<Map<String, Object>> items) {
        Map<String, Object> g = new LinkedHashMap<>();
        g.put("group", name);
        g.put("items", items);
        return g;
    }
}
