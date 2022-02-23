package com.iedaas.checklisttask;

import com.iedaas.checklisttask.dao.MyChecklistOwnerRepository;
import com.iedaas.checklisttask.dao.MyChecklistRepository;
import com.iedaas.checklisttask.entity.MyChecklist;
import com.iedaas.checklisttask.service.MyChecklistService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class MyChecklistServiceTests {

    @Mock
    ModelMapper modelMapper;

    @Mock
    private MyChecklistRepository myChecklistRepository;

    @Mock
    private MyChecklistOwnerRepository myChecklistOwnerRepository;

    @InjectMocks
    MyChecklistService myChecklistService;

    MyChecklist myChecklist1 = new MyChecklist(UUID.fromString("557f0afd-7f20-4840-97d6-7243db63e2a9"), "How to open bank account", "{\"Step1\":\"abc\", \"Step2\":\"def\"}", "in_progress");
    MyChecklist myChecklist2 = new MyChecklist(UUID.fromString("557f0afd-7f20-4840-97d6-7243db63e3a8"), "How to close bank account", "{\"Step1\":\"def\", \"Step2\":\"abc\"}", "completed");

    @Test
    public void findAllTest(){

    }

}
