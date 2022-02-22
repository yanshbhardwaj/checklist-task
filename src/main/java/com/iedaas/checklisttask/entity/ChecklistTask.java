package com.iedaas.checklisttask.entity;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "checklist_task")
public class ChecklistTask {

    @Id
    @GeneratedValue
    private int checklistTaskId;

    @Column(name = "checklist_task_uid")
    @Type(type= "org.hibernate.type.UUIDCharType")
    private UUID checklistTaskUid=UUID.randomUUID();

    @Column(name = "checklist_task_title")
    private String checklistTaskTitle;

    @Column(name = "checklist_task")
    private String checklistTask;

    @Column(name = "created_date")
    @CreationTimestamp
    private Timestamp createdDate;

    @Column(name = "updated_date")
    @UpdateTimestamp
    private Timestamp updatedDate;

    public ChecklistTask() {
    }

    public ChecklistTask(UUID checklistTaskUid, String checklistTaskTitle, String checklistTask) {
        this.checklistTaskUid = checklistTaskUid;
        this.checklistTaskTitle = checklistTaskTitle;
        this.checklistTask = checklistTask;
    }

    public int getChecklistTaskId() {
        return checklistTaskId;
    }

    public UUID getChecklistTaskUid() {
        return checklistTaskUid;
    }

    public String getChecklistTask() {
        return checklistTask;
    }

    public String getChecklistTaskTitle() {
        return checklistTaskTitle;
    }

    public void setChecklistTaskTitle(String checklistTaskTitle) {
        this.checklistTaskTitle = checklistTaskTitle;
    }

    public void setChecklistTask(String checklistTask) {
        this.checklistTask = checklistTask;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public Timestamp getUpdatedDate() {
        return updatedDate;
    }

    @Override
    public String toString() {
        return "ChecklistTask{" +
                "checklistTaskUid=" + checklistTaskUid +
                ", checklistTaskTitle='" + checklistTaskTitle + '\'' +
                ", checklistTask='" + checklistTask + '\'' +
                ", createdDate=" + createdDate +
                ", updatedDate=" + updatedDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChecklistTask that = (ChecklistTask) o;
        return checklistTaskUid.equals(that.checklistTaskUid) && checklistTaskTitle.equals(that.checklistTaskTitle) && checklistTask.equals(that.checklistTask) && Objects.equals(createdDate, that.createdDate) && Objects.equals(updatedDate, that.updatedDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(checklistTaskUid, checklistTaskTitle, checklistTask, createdDate, updatedDate);
    }
}
