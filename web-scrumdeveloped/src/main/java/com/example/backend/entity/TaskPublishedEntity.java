package com.example.backend.entity;
import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "TaskPublished", schema = "SE-Platform", catalog = "")
public class TaskPublishedEntity {
    private int taskId;
    private String publisher;
    private Timestamp publishtime;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TaskPublishedEntity that = (TaskPublishedEntity) o;

        if (taskId != that.taskId) return false;
        if (publisher != null ? !publisher.equals(that.publisher) : that.publisher != null) return false;
        if (publishtime != null ? !publishtime.equals(that.publishtime) : that.publishtime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = taskId;
        result = 31 * result + (publisher != null ? publisher.hashCode() : 0);
        result = 31 * result + (publishtime != null ? publishtime.hashCode() : 0);
        return result;
    }
}
