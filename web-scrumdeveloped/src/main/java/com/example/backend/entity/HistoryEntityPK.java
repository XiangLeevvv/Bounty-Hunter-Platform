package com.example.backend.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

public class HistoryEntityPK implements Serializable {
    private int taskId;
    private String userName;

    @Column(name = "task_id")
    @Id
    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    @Column(name = "user_name")
    @Id
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HistoryEntityPK that = (HistoryEntityPK) o;

        if (taskId != that.taskId) return false;
        if (userName != null ? !userName.equals(that.userName) : that.userName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = taskId;
        result = 31 * result + (userName != null ? userName.hashCode() : 0);
        return result;
    }
}
