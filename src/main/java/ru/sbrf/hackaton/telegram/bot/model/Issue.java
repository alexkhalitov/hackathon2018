package ru.sbrf.hackaton.telegram.bot.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Заявка
 */
@Entity
@Table(name="issues")
public class Issue {
    @OneToMany(fetch = FetchType.EAGER)
    private List<HistoryMessage> historyMessages;
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Client client;

    @Column(name="description")
    private String description;

    @OneToOne(fetch = FetchType.LAZY)
    private GeoPosition geoPosition;

    @ManyToOne(fetch = FetchType.EAGER)
    private IssueCategory issueCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    private Specialist assignee;

    @Column(name="status")
    @Enumerated(value = EnumType.STRING)
    private IssueStatus status;

    @Column(name="sentiment")
    @Enumerated(value = EnumType.STRING)
    private Sentiment sentiment;

    public Sentiment getSentiment() {
        return sentiment;
    }

    public void setSentiment(Sentiment sentiment) {
        this.sentiment = sentiment;
    }

    public Issue() {
        historyMessages = new ArrayList<>();
    }

    public List<HistoryMessage> getHistoryMessages() {
        return historyMessages;
    }

    public void setHistoryMessages(List<HistoryMessage> historyMessages) {
        this.historyMessages = historyMessages;
    }

    public IssueStatus getStatus() {
        return status;
    }

    public void setStatus(IssueStatus state) {
        this.status = state;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public GeoPosition getGeoPosition() {
        return geoPosition;
    }

    public void setGeoPosition(GeoPosition geoPosition) {
        this.geoPosition = geoPosition;
    }

    public IssueCategory getIssueCategory() {
        return issueCategory;
    }

    public void setIssueCategory(IssueCategory issueCategory) {
        this.issueCategory = issueCategory;
    }

    public Specialist getAssignee() {
        return assignee;
    }

    public void setAssignee(Specialist assignee) {
        this.assignee = assignee;
    }

    @Override
    public String toString() {
        return "Issue{" +
                "id=" + id +
                ", client=" + client +
                ", description='" + description + '\'' +
                ", geoPosition=" + geoPosition +
                ", issueCategory=" + issueCategory +
                ", assignee=" + assignee +
                ", status=" + status +
                '}';
    }
}
