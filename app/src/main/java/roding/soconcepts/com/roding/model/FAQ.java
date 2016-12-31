package roding.soconcepts.com.roding.model;

/**
 * Created by mac on 2/22/16.
 */
public class FAQ {

    private int id;
    private String question;
    private String answer;

    public FAQ() {
    }

    public FAQ(int id, String question, String answer) {
        this.setId(id);
        this.question = question;
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
