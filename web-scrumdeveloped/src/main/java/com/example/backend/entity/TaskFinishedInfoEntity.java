package com.example.backend.entity;


import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "taskFinishedInfo", schema = "SE-Platform", catalog = "")
public class TaskFinishedInfoEntity {
    private Integer id;
    private String title;
    private String info;
    private String tags;
    private Double bonus;
    private Timestamp beginTime;
    private Timestamp endTime;
    private String publisher;
    private Timestamp publishtime;
    private String receiver;
    private Timestamp receivetime;
    private Timestamp finishedtime;

    @Id
    @Column(name = "id")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Basic
    @Column(name = "info")
    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Basic
    @Column(name = "tags")
    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    @Basic
    @Column(name = "bonus")
    public Double getBonus() {
        return bonus;
    }

    public void setBonus(Double bonus) {
        this.bonus = bonus;
    }

    @Basic
    @Column(name = "begin_time")
    public Timestamp getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Timestamp beginTime) {
        this.beginTime = beginTime;
    }

    @Basic
    @Column(name = "end_time")
    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    @Basic
    @Column(name = "publisher")
    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    @Basic
    @Column(name = "publishtime")
    public Timestamp getPublishtime() {
        return publishtime;
    }

    public void setPublishtime(Timestamp publishtime) {
        this.publishtime = publishtime;
    }

    @Basic
    @Column(name = "receiver")
    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    @Basic
    @Column(name = "receivetime")
    public Timestamp getReceivetime() {
        return receivetime;
    }

    public void setReceivetime(Timestamp receivetime) {
        this.receivetime = receivetime;
    }

    @Basic
    @Column(name = "finishedtime")
    public Timestamp getFinishedtime() {
        return finishedtime;
    }

    public void setFinishedtime(Timestamp finishedtime) {
        this.finishedtime = finishedtime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TaskFinishedInfoEntity that = (TaskFinishedInfoEntity) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        if (info != null ? !info.equals(that.info) : that.info != null) return false;
        if (tags != null ? !tags.equals(that.tags) : that.tags != null) return false;
        if (bonus != null ? !bonus.equals(that.bonus) : that.bonus != null) return false;
        if (beginTime != null ? !beginTime.equals(that.beginTime) : that.beginTime != null) return false;
        if (endTime != null ? !endTime.equals(that.endTime) : that.endTime != null) return false;
        if (publisher != null ? !publisher.equals(that.publisher) : that.publisher != null) return false;
        if (publishtime != null ? !publishtime.equals(that.publishtime) : that.publishtime != null) return false;
        if (receiver != null ? !receiver.equals(that.receiver) : that.receiver != null) return false;
        if (receivetime != null ? !receivetime.equals(that.receivetime) : that.receivetime != null) return false;
        if (finishedtime != null ? !finishedtime.equals(that.finishedtime) : that.finishedtime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (info != null ? info.hashCode() : 0);
        result = 31 * result + (tags != null ? tags.hashCode() : 0);
        result = 31 * result + (bonus != null ? bonus.hashCode() : 0);
        result = 31 * result + (beginTime != null ? beginTime.hashCode() : 0);
        result = 31 * result + (endTime != null ? endTime.hashCode() : 0);
        result = 31 * result + (publisher != null ? publisher.hashCode() : 0);
        result = 31 * result + (publishtime != null ? publishtime.hashCode() : 0);
        result = 31 * result + (receiver != null ? receiver.hashCode() : 0);
        result = 31 * result + (receivetime != null ? receivetime.hashCode() : 0);
        result = 31 * result + (finishedtime != null ? finishedtime.hashCode() : 0);
        return result;
    }
}
