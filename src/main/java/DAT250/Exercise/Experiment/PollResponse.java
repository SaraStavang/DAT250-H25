package DAT250.Exercise.Experiment;

import DAT250.Exercise.Experiment.model.Poll;
import DAT250.Exercise.Experiment.model.VoteOption;

import java.util.List;

public class PollResponse {
    public Long id;
    public String question;
    public List<OptionResponse> options;

    public PollResponse(Poll poll) {
        this.id = poll.getId();
        this.question = poll.getQuestion();
        this.options = poll.getOptions().stream()
                .map(o -> new OptionResponse(o, poll))
                .toList();
    }

    public static class OptionResponse {
        public Long id;
        public String caption;
        public int votes;


        public OptionResponse(VoteOption opt, Poll poll) {
            this.id = opt.getId();
            this.caption = opt.getCaption();
            this.votes = (int) poll.getVotes().stream()
                    .filter(v -> v.getVoteOption().getId().equals(opt.getId()))
                    .count();
        }

    }
}

