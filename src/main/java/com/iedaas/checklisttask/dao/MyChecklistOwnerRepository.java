package com.iedaas.checklisttask.dao;

import com.iedaas.checklisttask.entity.MyChecklistOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MyChecklistOwnerRepository extends JpaRepository<MyChecklistOwner, Integer> {

    @Query(value = CustomQueries.FIND_OWNER_BY_TASK_UUID, nativeQuery = true)
    MyChecklistOwner findByUUID(String checklistTaskUid);
}
