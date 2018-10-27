package ru.sbrf.hackaton.telegram.bot.model;

import javax.persistence.*;
import java.util.List;

/**
 * Категория заявки
 */
@Entity
@Table(name="issue_categories")
public class IssueCategory {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "severity")
    @Enumerated(value = EnumType.STRING)
    private Severity severity;

    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<IssueCategory> children;

    @Column(name="name")
    private String name;

    @Column(name="description", length = 2048)
    private String description;

    @Column(name="answer", length = 2048)
    private String answer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Severity getSeverity() {
        return severity;
    }

    public void setSeverity(Severity severity) {
        this.severity = severity;
    }

    public List<IssueCategory> getChildren() {
        return children;
    }

    public void setChildren(List<IssueCategory> children) {
        this.children = children;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public String toString() {
        return "IssueCategory{" +
                "id=" + id +
                ", severity=" + severity +
                ", children=" + children +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", answer='" + answer + '\'' +
                '}';
    }
}
