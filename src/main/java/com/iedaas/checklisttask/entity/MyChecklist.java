package com.iedaas.checklisttask.entity;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "my_checklist")
public class MyChecklist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int myChecklistId;

    @Column(name = "my_checklist_uid")
    @Type(type= "org.hibernate.type.UUIDCharType")
    private UUID myChecklistUid =UUID.randomUUID();

    @Column(name = "my_checklist_title")
    private String myChecklistTitle;

    @Column(name = "my_checklist")
    private String myChecklist;

    @Column(name = "my_checklist_status")
    private String myChecklistStatus;

    @Column(name = "created_date")
    @CreationTimestamp
    private Timestamp createdDate;

    @Column(name = "updated_date")
    @UpdateTimestamp
    private Timestamp updatedDate;

    public MyChecklist() {
    }

    public MyChecklist(UUID myChecklistUid, String myChecklistTitle, String myChecklist, String myChecklistStatus) {
        this.myChecklistUid = myChecklistUid;
        this.myChecklistTitle = myChecklistTitle;
        this.myChecklist = myChecklist;
        this.myChecklistStatus = myChecklistStatus;
    }

    public int getMyChecklistId() {
        return myChecklistId;
    }

    public UUID getMyChecklistUid() {
        return myChecklistUid;
    }

    public String getMyChecklist() {
        return myChecklist;
    }

    public String getMyChecklistTitle() {
        return myChecklistTitle;
    }

    public void setMyChecklistTitle(String myChecklistTitle) {
        this.myChecklistTitle = myChecklistTitle;
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

    public Timestamp getUpdatedDate() {
        return updatedDate;
    }

    @Override
    public String toString() {
        return "MyChecklist{" +
                "myChecklistUid=" + myChecklistUid +
                ", myChecklistTitle='" + myChecklistTitle + '\'' +
                ", myChecklist='" + myChecklist + '\'' +
                ", myChecklistStatus='" + myChecklistStatus + '\'' +
                ", createdDate=" + createdDate +
                ", updatedDate=" + updatedDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyChecklist that = (MyChecklist) o;
        return myChecklistUid.equals(that.myChecklistUid) && myChecklistTitle.equals(that.myChecklistTitle) && myChecklist.equals(that.myChecklist) && myChecklistStatus.equals(that.myChecklistStatus) && Objects.equals(createdDate, that.createdDate) && Objects.equals(updatedDate, that.updatedDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(myChecklistUid, myChecklistTitle, myChecklist, myChecklistStatus, createdDate, updatedDate);
    }
}
