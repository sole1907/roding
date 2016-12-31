package roding.soconcepts.com.roding.model;

/**
 * Created by mac on 9/1/16.
 */
public class State {
    private String code;
    private String state;

    public State(String code, String state) {
        this.code = code;
        this.state = state;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
