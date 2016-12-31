package roding.soconcepts.com.roding.model;

/**
 * Created by mac on 2/22/16.
 */
public class Member {

    private int id;
    private String memberId;
    private String surname;
    private String otherNames;
    private boolean active;


    public Member() {

    }

    public Member(int id, String memberId, String surname, String otherNames, boolean active) {
        this.id = id;
        this.memberId = memberId;
        this.surname = surname;
        this.otherNames = otherNames;
        this.active = active;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getOtherNames() {
        return otherNames;
    }

    public void setOtherNames(String otherNames) {
        this.otherNames = otherNames;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
