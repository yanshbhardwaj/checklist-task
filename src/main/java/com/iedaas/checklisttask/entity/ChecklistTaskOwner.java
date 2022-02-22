package com.iedaas.checklisttask.entity;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "checklist_task_owner")
public class ChecklistTaskOwner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "owner_uid")
    @Type(type= "org.hibernate.type.UUIDCharType")
    private UUID ownerUid;

    @Column(name = "checklist_task_uid")
    @Type(type= "org.hibernate.type.UUIDCharType")
    private UUID checklistTaskUid;

    public ChecklistTaskOwner() {
    }

    public ChecklistTaskOwner(UUID ownerUid, UUID checklistTaskUid) {
        this.ownerUid = ownerUid;
        this.checklistTaskUid = checklistTaskUid;
    }

    public int getId() {
        return id;
    }

    public UUID getOwnerUid() {
        return ownerUid;
    }

    public void setOwnerUid(UUID ownerUid) {
        this.ownerUid = ownerUid;
    }

    public UUID getChecklistTaskUid() {
        return checklistTaskUid;
    }

    public void setChecklistTaskUid(UUID checklistTaskUid) {
        this.checklistTaskUid = checklistTaskUid;
    }

    @Override
    public String toString() {
        return "ChecklistTaskOwner{" +
                "ownerUid=" + ownerUid +
                ", checklistTaskUid=" + checklistTaskUid +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChecklistTaskOwner that = (ChecklistTaskOwner) o;
        return ownerUid.equals(that.ownerUid) && checklistTaskUid.equals(that.checklistTaskUid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ownerUid, checklistTaskUid);
    }
}
