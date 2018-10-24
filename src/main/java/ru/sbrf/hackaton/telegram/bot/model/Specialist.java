package ru.sbrf.hackaton.telegram.bot.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Специалист ВСП
 */
@Entity
@Table(name="specialists")
public class Specialist {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "firstname", nullable = false)
    private String firstname;
    @Column(name = "middlename")
    private String middlename;
    @Column(name = "lastname", nullable = false)
    private String lastname;

    @Column(name = "mobile", nullable = false)
    private String mobile;

    @ManyToMany
    private List<IssueCategory> categories = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    private List<BankFilial> bankFilials = new ArrayList<BankFilial>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getMiddlename() {
        return middlename;
    }

    public void setMiddlename(String middlename) {
        this.middlename = middlename;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public List<BankFilial> getBankFilials() {
        return bankFilials;
    }

    public List<IssueCategory> getCategories() {
        return categories;
    }
}
