package com.iedaas.checklisttask.dao;

import com.iedaas.checklisttask.entity.MyChecklist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MyChecklistRepository extends JpaRepository<MyChecklist, Integer> {

    @Query(value = CustomQueries.FIND_TASK_BY_UUID_QUERY, nativeQuery = true)
    MyChecklist findByUUID(String checklistTaskUid);

    @Query(value = CustomQueries.FIND_USER_TASKS_QUERY, nativeQuery = true)
    Page<MyChecklist> findUserTasks(String userUid, Pageable pageable);

    @Query(value = CustomQueries.FIND_BY_STATUS_QUERY, nativeQuery = true)
    Page<MyChecklist> findByStatus(String userUid, String status, Pageable pageable);
}
