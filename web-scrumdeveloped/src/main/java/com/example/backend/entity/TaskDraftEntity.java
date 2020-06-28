package com.example.backend.entity;

import javax.persistence.*;

@Entity
@Table(name = "TaskDraft", schema = "SE-Platform", catalog = "")
public class TaskDraftEntity {
    private int taskId;
    private String creator;

    @Id
    @Column(name = "task_id")
    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    @Basic
    @Column(name = "creator")
    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TaskDraftEntity that = (TaskDraftEntity) o;

        if (taskId != that.taskId) return false;
        if (creator != null ? !creator.equals(that.creator) : that.creator != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = taskId;
        result = 31 * result + (creator != null ? creator.hashCode() : 0);
        return result;
    }
}
