package DAT250.Exercise.Experiment.model;

public class VoteOption {
    private Long id;
    private String caption;
    private int presentationOrder;
    private Poll poll;

    public VoteOption(){}

    public VoteOption(Long id, String caption, int presentationOrder, Poll poll) {
        this.id = id;
        this.caption = caption;
        this.presentationOrder = presentationOrder;
        this.poll = poll;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCaption() { return caption; }
    public void setCaption(String caption) { this.caption = caption; }

    public int getPresentationOrder() { return presentationOrder; }
    public void setPresentationOrder(int presentationOrder) { this.presentationOrder = presentationOrder; }

    public Poll getPoll() { return poll; }
    public void setPoll(Poll poll) { this.poll = poll; }
}
