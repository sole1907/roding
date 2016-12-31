package roding.soconcepts.com.roding.model;

/**
 * Created by mac on 2/22/16.
 */
public class Medication {

    private String medication;
    private double unitPrice;
    private double quantity;
    private double totalPrice;

    public Medication() {

    }

    public Medication(String medication, double unitPrice, double quantity, double totalPrice) {
        this.setMedication(medication);
        this.setUnitPrice(unitPrice);
        this.setQuantity(quantity);
        this.setTotalPrice(totalPrice);
    }


    public String getMedication() {
        return medication;
    }

    public void setMedication(String medication) {
        this.medication = medication;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
