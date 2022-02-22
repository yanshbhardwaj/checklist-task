package com.iedaas.checklisttask.dto;

import java.util.UUID;

public class ChecklistTaskOwnerDTO {
    private UUID ownerUid;
    private String ownerName;

    public ChecklistTaskOwnerDTO() {
    }

    public ChecklistTaskOwnerDTO(UUID ownerUid, String ownerName) {
        this.ownerUid = ownerUid;
        this.ownerName = ownerName;
    }

    public UUID getOwnerUid() {
        return ownerUid;
    }

    public void setOwnerUid(UUID ownerUid) {
        this.ownerUid = ownerUid;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
}
