package ru.sbrf.hackaton.telegram.bot.model;


import javax.persistence.*;
import java.util.Objects;

/**
 * Банкомат
 */
@Entity
@Table(name="cash_points")
public class CashPoint {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private City city;
    @ManyToOne(fetch = FetchType.LAZY)
    private BankFilial bankFilial;

    @Column(name = "address", nullable = false)
    private String address;

    @Column
    private String shortAddress;

    public String getShortAddress() {
        return shortAddress;
    }

    public void setShortAddress(String shortAddress) {
        this.shortAddress = shortAddress;
    }

    @OneToOne
    private GeoPosition geoPosition;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public BankFilial getBankFilial() {
        return bankFilial;
    }

    public void setBankFilial(BankFilial bankFilial) {
        this.bankFilial = bankFilial;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public GeoPosition getGeoPosition() {
        return geoPosition;
    }

    public void setGeoPosition(GeoPosition geoPosition) {
        this.geoPosition = geoPosition;
    }

    @Override
    public String toString() {
        return "CashPoint{" +
                "id=" + id +
                ", city=" + city +
                ", bankFilial=" + bankFilial +
                ", address='" + address + '\'' +
                ", geoPosition=" + geoPosition +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CashPoint cashPoint = (CashPoint) o;
        return Objects.equals(id, cashPoint.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
