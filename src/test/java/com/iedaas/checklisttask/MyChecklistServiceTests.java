package com.iedaas.checklisttask;

import com.iedaas.checklisttask.dao.MyChecklistOwnerRepository;
import com.iedaas.checklisttask.dao.MyChecklistRepository;
import com.iedaas.checklisttask.dto.MyChecklistDTO;
import com.iedaas.checklisttask.dto.MyChecklistGetDTO;
import com.iedaas.checklisttask.entity.MyChecklist;
import com.iedaas.checklisttask.service.MyChecklistService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

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

    ModelMapper mapper = new ModelMapper();

    MyChecklist myChecklist1 = new MyChecklist(UUID.fromString("557f0afd-7f20-4840-97d6-7243db63e2a9"), "How to open bank account", "{\"Step1\":\"abc\", \"Step2\":\"def\"}", "in_progress");
    MyChecklist myChecklist2 = new MyChecklist(UUID.fromString("557f0afd-7f20-4840-97d6-7243db63e3a8"), "How to close bank account", "{\"Step1\":\"def\", \"Step2\":\"abc\"}", "completed");

    @Test
    public void findAllTest(){
        List<MyChecklist> myChecklists = new ArrayList<>();
        myChecklists.add(myChecklist1);
        myChecklists.add(myChecklist2);

        Pageable pageable= PageRequest.of(0,2, Sort.Direction.valueOf("DESC"), "updated_date");
        Page<MyChecklist> result = new PageImpl<>(myChecklists, pageable,2);

        when(myChecklistRepository.findUserTasks("8f0c8b00-a87d-4358-a8a7-66d1f0b540e6", pageable)).thenReturn(result);

        List<MyChecklistDTO> myChecklistDTOList = new ArrayList<>();
        for(MyChecklist myChecklist : myChecklists) {
            MyChecklistDTO myChecklistDTO = mapper.map(myChecklist, MyChecklistDTO.class);
            when(modelMapper.map(myChecklist, MyChecklistDTO.class)).thenReturn(myChecklistDTO);
            myChecklistDTOList.add(myChecklistDTO);
        }

        MyChecklistGetDTO myChecklistGetDTO = myChecklistService.getMyChecklists(UUID.fromString("8f0c8b00-a87d-4358-a8a7-66d1f0b540e6"), Optional.empty(), Optional.of(2), Optional.of("DESC"), Optional.of("updated_date"));
        assertThat(myChecklistGetDTO.getMyChecklists(), is(myChecklistDTOList));
    }

    @Test
    public void testFindByUUID(){
        MyChecklistDTO myChecklistDTO = mapper.map(myChecklist1, MyChecklistDTO.class);

        when(myChecklistRepository.findByUUID("557f0afd-7f20-4840-97d6-7243db63e2a9")).thenReturn(myChecklist1);
        when(modelMapper.map(myChecklist1, MyChecklistDTO.class)).thenReturn(myChecklistDTO);

        MyChecklistDTO myChecklistDTODb = myChecklistService.getMyChecklistByUid(UUID.fromString("8f0c8b00-a87d-4358-a8a7-66d1f0b540e6"), UUID.fromString("557f0afd-7f20-4840-97d6-7243db63e2a9"));
        assertThat(myChecklistDTODb, is(myChecklistDTO));
    }
}
