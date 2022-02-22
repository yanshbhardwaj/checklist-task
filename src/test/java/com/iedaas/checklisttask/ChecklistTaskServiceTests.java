package com.iedaas.checklisttask;

import com.iedaas.checklisttask.dao.ChecklistTaskOwnerRepository;
import com.iedaas.checklisttask.dao.ChecklistTaskRepository;
import com.iedaas.checklisttask.dao.CustomRepository;
import com.iedaas.checklisttask.entity.ChecklistTask;
import com.iedaas.checklisttask.service.ChecklistTaskService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class ChecklistTaskServiceTests {

    @Mock
    ModelMapper modelMapper;

    @Mock
    private ChecklistTaskRepository checklistTaskRepository;

    @Mock
    private ChecklistTaskOwnerRepository checklistTaskOwnerRepository;

    @Mock
    private CustomRepository customRepository;

    @InjectMocks
    ChecklistTaskService checklistTaskService;

    ChecklistTask checklistTask1 = new ChecklistTask(UUID.fromString("557f0afd-7f20-4840-97d6-7243db63e2a9"), "How to open bank account", "{\"Step1\":\"abc\", \"Step2\":\"def\"}");
    ChecklistTask checklistTask2 = new ChecklistTask(UUID.fromString("557f0afd-7f20-4840-97d6-7243db63e3a8"), "How to close bank account", "{\"Step1\":\"def\", \"Step2\":\"abc\"}");

    @Test
    public void findAllTest(){

    }

}
