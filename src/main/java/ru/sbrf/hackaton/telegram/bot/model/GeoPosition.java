package ru.sbrf.hackaton.telegram.bot.model;

import javax.persistence.*;

/**
 * Геопозиция
 */
@Entity
@Table(name="geo_positions")
public class GeoPosition {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name="longitude", nullable = false)
    private Float longitude;
    @Column(name="latitude", nullable = false)
    private Float latitude;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    @Override
    public String toString() {
        return "GeoPosition{" +
                "id=" + id +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }
}
