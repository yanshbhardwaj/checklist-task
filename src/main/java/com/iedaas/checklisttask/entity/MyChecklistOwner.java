package com.iedaas.checklisttask.entity;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "my_checklist_owner")
public class MyChecklistOwner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "my_checklist_id")
    private int id;

    @Column(name = "owner_uid")
    @Type(type= "org.hibernate.type.UUIDCharType")
    private UUID ownerUid;

    @Column(name = "my_checklist_uid")
    @Type(type= "org.hibernate.type.UUIDCharType")
    private UUID myChecklistUid;

    public MyChecklistOwner() {
    }

    public MyChecklistOwner(UUID ownerUid, UUID myChecklistUid) {
        this.ownerUid = ownerUid;
        this.myChecklistUid = myChecklistUid;
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

    public UUID getMyChecklistUid() {
        return myChecklistUid;
    }

    public void setMyChecklistUid(UUID myChecklistUid) {
        this.myChecklistUid = myChecklistUid;
    }

    @Override
    public String toString() {
        return "MyChecklistOwner{" +
                "ownerUid=" + ownerUid +
                ", myChecklistUid=" + myChecklistUid +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyChecklistOwner that = (MyChecklistOwner) o;
        return ownerUid.equals(that.ownerUid) && myChecklistUid.equals(that.myChecklistUid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ownerUid, myChecklistUid);
    }
}
