package com.example.backend.entity;


import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "TaskFinished", schema = "SE-Platform", catalog = "")
public class TaskFinishedEntity {
    private int taskId;
    private String publisher;
    private Timestamp publishtime;
    private String receiver;
    private Timestamp receivetime;
    private Timestamp finishedtime;

    @Id
    @Column(name = "task_id")
    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
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

        TaskFinishedEntity that = (TaskFinishedEntity) o;

        if (taskId != that.taskId) return false;
        if (publisher != null ? !publisher.equals(that.publisher) : that.publisher != null) return false;
        if (publishtime != null ? !publishtime.equals(that.publishtime) : that.publishtime != null) return false;
        if (receiver != null ? !receiver.equals(that.receiver) : that.receiver != null) return false;
        if (receivetime != null ? !receivetime.equals(that.receivetime) : that.receivetime != null) return false;
        if (finishedtime != null ? !finishedtime.equals(that.finishedtime) : that.finishedtime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = taskId;
        result = 31 * result + (publisher != null ? publisher.hashCode() : 0);
        result = 31 * result + (publishtime != null ? publishtime.hashCode() : 0);
        result = 31 * result + (receiver != null ? receiver.hashCode() : 0);
        result = 31 * result + (receivetime != null ? receivetime.hashCode() : 0);
        result = 31 * result + (finishedtime != null ? finishedtime.hashCode() : 0);
        return result;
    }
}
