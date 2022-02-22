package com.iedaas.checklisttask.dao;

public interface CustomQueries {
    public static final String FIND_TASK_BY_UUID_QUERY = "select * from checklist_task c where c.checklist_task_uid= ?1";
    public static final String FIND_USER_TASKS_QUERY = "select * from checklist_task c join checklist_task_owner o on c.checklist_task_uid=o.checklist_task_uid where o.owner_uid=?1";
    public static final String FIND_OWNER_BY_TASK_UUID = "select * from checklist_task_owner c where c.checklist_task_uid= ?1";
}
