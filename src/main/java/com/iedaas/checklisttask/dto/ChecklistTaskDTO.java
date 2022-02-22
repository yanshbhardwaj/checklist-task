package com.iedaas.checklisttask.dto;

import java.sql.Timestamp;
import java.util.UUID;

public class ChecklistTaskDTO {

    private UUID checklistTaskUid;
    private String checklistTaskTitle;
    private String checklistTask;
    private ChecklistTaskOwnerDTO checklistTaskOwner;
    private Timestamp createdDate;
    private Timestamp updatedDate;

    public UUID getChecklistTaskUid() {
        return checklistTaskUid;
    }

    public void setChecklistTaskUid(UUID checklistTaskUid) {
        this.checklistTaskUid = checklistTaskUid;
    }

    public String getChecklistTaskTitle() {
        return checklistTaskTitle;
    }

    public void setChecklistTaskTitle(String checklistTaskTitle) {
        this.checklistTaskTitle = checklistTaskTitle;
    }

    public String getChecklistTask() {
        return checklistTask;
    }

    public void setChecklistTask(String checklistTask) {
        this.checklistTask = checklistTask;
    }

    public ChecklistTaskOwnerDTO getChecklistTaskOwner() {
        return checklistTaskOwner;
    }

    public void setChecklistTaskOwner(ChecklistTaskOwnerDTO checklistTaskOwner) {
        this.checklistTaskOwner = checklistTaskOwner;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public Timestamp getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Timestamp updatedDate) {
        this.updatedDate = updatedDate;
    }
}
