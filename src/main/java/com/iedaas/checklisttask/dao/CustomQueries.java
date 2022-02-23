package com.iedaas.checklisttask.dao;

public interface CustomQueries {
    public static final String FIND_TASK_BY_UUID_QUERY = "select * from my_checklist c where c.my_checklist_uid= ?1";
    public static final String FIND_USER_TASKS_QUERY = "select * from my_checklist c join my_checklist_owner o on c.my_checklist_uid=o.my_checklist_uid where o.owner_uid=?1";
    public static final String FIND_OWNER_BY_TASK_UUID = "select * from my_checklist_owner c where c.my_checklist_uid= ?1";
    public static final String FIND_BY_STATUS_QUERY = "select * from my_checklist c join my_checklist_owner o on c.my_checklist_uid=o.my_checklist_uid where o.owner_uid=?1 and c.my_checklist_status=?2";
}
