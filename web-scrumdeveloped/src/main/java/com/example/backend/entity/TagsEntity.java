package com.example.backend.entity;

import javax.persistence.*;

@Entity
@Table(name = "Tags", schema = "SE-Platform", catalog = "")
public class TagsEntity {
    private int typeId;
    private String taskType;

    @Id
    @Column(name = "type_id")
    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    @Basic
    @Column(name = "task_type")
    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TagsEntity that = (TagsEntity) o;

        if (typeId != that.typeId) return false;
        if (taskType != null ? !taskType.equals(that.taskType) : that.taskType != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = typeId;
        result = 31 * result + (taskType != null ? taskType.hashCode() : 0);
        return result;
    }
}
