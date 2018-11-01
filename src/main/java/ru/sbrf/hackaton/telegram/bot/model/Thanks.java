package ru.sbrf.hackaton.telegram.bot.model;


import javax.persistence.*;

@Entity
@Table(name="thanks")
public class Thanks {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @Column
    private Long chatId;

    @OneToOne(fetch = FetchType.EAGER)
    private GeoPosition position;

    @Column(length = 2048)
    private String message;


    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setPosition(GeoPosition position) {
        this.position = position;
    }

    public GeoPosition getPosition() {
        return position;
    }

    public Long getId() {
        return id;
    }



}
