package com.iedaas.checklisttask.dto;

import java.util.List;

public class MyChecklistGetDTO {

    private long totalCount;

    private List<MyChecklistDTO> myChecklists;

    public MyChecklistGetDTO() {
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public List<MyChecklistDTO> getMyChecklists() {
        return myChecklists;
    }

    public void setMyChecklists(List<MyChecklistDTO> myChecklists) {
        this.myChecklists = myChecklists;
    }
}
