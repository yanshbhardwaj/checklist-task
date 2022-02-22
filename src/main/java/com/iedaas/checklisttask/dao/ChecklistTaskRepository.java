package com.iedaas.checklisttask.dao;

import com.iedaas.checklisttask.entity.ChecklistTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ChecklistTaskRepository extends JpaRepository<ChecklistTask, Integer> {

    @Query(value = CustomQueries.FIND_TASK_BY_UUID_QUERY, nativeQuery = true)
    ChecklistTask findByUUID(String checklistTaskUid);

    @Query(value = CustomQueries.FIND_USER_TASKS_QUERY, nativeQuery = true)
    Page<ChecklistTask> findUserTasks(String userUid, Pageable pageable);
}
