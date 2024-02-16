package ro.tuc.ds2020.entities;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Entity
public class Measurement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idmeasurements;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date timest;

    @PrePersist
    protected void onCreate() {
        timest = new Date();
    }

    @Column()
    private Double val;

    @Column()
    private Double sum;

    @Column()
    private String deviceId;

    public Measurement(Date timest, Double val) {
        this.timest = timest;
        this.val = val;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId; // Update this method to actually set the device ID
    }
}
