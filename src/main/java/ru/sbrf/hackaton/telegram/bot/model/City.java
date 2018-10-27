package ru.sbrf.hackaton.telegram.bot.model;

import javax.persistence.*;
import java.util.List;

/**
 * Город
 */
@Entity
@Table(name="cities")
public class City {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @ManyToOne(fetch = FetchType.LAZY)
    private Country country;
    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<BankFilial> filials;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public List<BankFilial> getFilials() {
        return filials;
    }

    public void setFilials(List<BankFilial> filials) {
        this.filials = filials;
    }

    @Override
    public String toString() {
        return "City{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", country=" + country +
                ", filials=" + filials +
                '}';
    }
}
