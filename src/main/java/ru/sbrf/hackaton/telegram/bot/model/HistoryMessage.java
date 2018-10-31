package ru.sbrf.hackaton.telegram.bot.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "history_message")
public class HistoryMessage {

    String messageText;
    Date messageTime;
    @ManyToOne(fetch = FetchType.EAGER)
    Specialist author;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(Date messageTime) {
        this.messageTime = messageTime;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public Specialist getAuthor() {
        return author;
    }

    public void setAuthor(Specialist author) {
        this.author = author;
    }
}
