package ro.tuc.ds2020.entities;

import javax.persistence.*;

@Entity
public class Device {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

    @Column
    private String description;
    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private float maxconsumption;

    @Column
    private int personId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getpersonId() {
        return personId;
    }

    public void setpersonId(int personId) {
        this.personId = personId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public float getMaxconsumption() {
        return maxconsumption;
    }

    public void setMaxconsumption(float maxconsumption) {
        this.maxconsumption = maxconsumption;
    }

    public Device(int id, String description, String address, float maxconsumption) {
        this.id = id;
        this.description = description;
        this.address = address;
        this.maxconsumption = maxconsumption;
    }

    public Device(String description, String address, float maxconsumption) {
        this.description = description;
        this.address = address;
        this.maxconsumption = maxconsumption;
    }

    public Device(int id, String description, String address, float maxconsumption, int personId) {
        this.id = id;
        this.description = description;
        this.address = address;
        this.maxconsumption = maxconsumption;
        this.personId = personId;
    }

    public Device(){}

    public String getMhec() {
        // Replace with the actual logic to get the data you want to send
        return String.valueOf(this.maxconsumption);
    }
}
