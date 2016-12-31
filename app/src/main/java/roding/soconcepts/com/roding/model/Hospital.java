package roding.soconcepts.com.roding.model;

/**
 * Created by mac on 2/22/16.
 */
public class Hospital {

    private int id;
    private String name;
    private String address;
    private String city;
    private String email;
    private String phone;
    private String state;


    public Hospital() {

    }

    public Hospital(int id, String name, String city, String address, String state, String phone, String email) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.state = state;
        this.city = city;
        this.email = email;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String title) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String toString() {
        return (getName() + "\n" + ((getAddress() != null) ? ("Address: " + getAddress() + "\n") : "") +
                ((getCity() != null) ?  ("City: " + getCity() + "\n") : "") +
                ((getEmail() != null) ?  ("E-mail: " + getEmail() + "\n") : "") +
                ((getPhone() != null) ?  ("Phone: " + getPhone()) : ""));
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
