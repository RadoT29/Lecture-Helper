package nl.tudelft.oopp.app.models;

public class Entry implements Comparable<Entry> {
    Question question;
    int upvote;

    public Entry(Question question, int upvote) {
        this.question = question;
        this.upvote = upvote;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public int getUpvote() {
        return upvote;
    }

    public void setUpvote(int upvote) {
        this.upvote = upvote;
    }

    @Override
    public int compareTo(Entry o) {
        return this.upvote - o.upvote;
    }
}
