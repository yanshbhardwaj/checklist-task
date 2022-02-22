package com.iedaas.checklisttask.dto;

import java.util.List;

public class ChecklistTaskGetDTO {

    private long totalCount;

    private List<ChecklistTaskDTO> checklistTasks;

    public ChecklistTaskGetDTO() {
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public List<ChecklistTaskDTO> getChecklistTasks() {
        return checklistTasks;
    }

    public void setChecklistTasks(List<ChecklistTaskDTO> checklistTasks) {
        this.checklistTasks = checklistTasks;
    }
}
