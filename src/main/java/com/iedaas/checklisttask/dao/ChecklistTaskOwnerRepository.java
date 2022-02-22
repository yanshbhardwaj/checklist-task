package com.iedaas.checklisttask.dao;

import com.iedaas.checklisttask.entity.ChecklistTaskOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ChecklistTaskOwnerRepository extends JpaRepository<ChecklistTaskOwner, Integer> {

    @Query(value = CustomQueries.FIND_OWNER_BY_TASK_UUID, nativeQuery = true)
    ChecklistTaskOwner findByUUID(String checklistTaskUid);
}
