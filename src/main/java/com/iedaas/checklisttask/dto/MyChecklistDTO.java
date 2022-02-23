package com.iedaas.checklisttask.dto;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.UUID;

public class MyChecklistDTO {

    private UUID myChecklistUid;
    @Size(max = 160)
    private String myChecklistTitle;
    private String myChecklist;
    @Pattern(regexp = "^(in_progress|completed)$")
    private String myChecklistStatus;
    private Timestamp createdDate;
    private Timestamp updatedDate;

    public UUID getMyChecklistUid() {
        return myChecklistUid;
    }

    public void setMyChecklistUid(UUID myChecklistUid) {
        this.myChecklistUid = myChecklistUid;
    }

    public String getMyChecklistTitle() {
        return myChecklistTitle;
    }

    public void setMyChecklistTitle(String myChecklistTitle) {
        this.myChecklistTitle = myChecklistTitle;
    }

    public String getMyChecklist() {
        return myChecklist;
    }

    public void setMyChecklist(String myChecklist) {
        this.myChecklist = myChecklist;
    }

    public String getMyChecklistStatus() {
        return myChecklistStatus;
    }

    public void setMyChecklistStatus(String myChecklistStatus) {
        this.myChecklistStatus = myChecklistStatus;
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
