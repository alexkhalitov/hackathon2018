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
    private Double longitude;
    @Column(name="latitude", nullable = false)
    private Double latitude;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
}
