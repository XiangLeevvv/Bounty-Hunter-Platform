package com.example.backend.entity;

import org.joda.time.DateTime;

import javax.persistence.*;

@Entity
@Table(name = "ChatMessage", schema = "SE-Platform", catalog = "")
public class ChatMessageEntity {
    private int id;
    private String content;
    private DateTime time;
    private int sender;
    private Integer receiver;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "content")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Basic
    @Column(name = "time")
    public DateTime getTime() {
        return time;
    }

    public void setTime(DateTime time) {
        this.time = time;
    }

    @Basic
    @Column(name = "sender")
    public int getSender() {
        return sender;
    }

    public void setSender(int sender) {
        this.sender = sender;
    }

    @Basic
    @Column(name = "receiver")
    public Integer getReceiver() {
        return receiver;
    }

    public void setReceiver(Integer receiver) {
        this.receiver = receiver;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChatMessageEntity that = (ChatMessageEntity) o;

        if (id != that.id) return false;
        if (sender != that.sender) return false;
        if (content != null ? !content.equals(that.content) : that.content != null) return false;
        if (time != null ? !time.equals(that.time) : that.time != null) return false;
        if (receiver != null ? !receiver.equals(that.receiver) : that.receiver != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (time != null ? time.hashCode() : 0);
        result = 31 * result + sender;
        result = 31 * result + (receiver != null ? receiver.hashCode() : 0);
        return result;
    }
}
