package com.iedaas.checklisttask;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iedaas.checklisttask.controller.MyChecklistController;
import com.iedaas.checklisttask.dto.MyChecklistDTO;
import com.iedaas.checklisttask.dto.MyChecklistGetDTO;
import com.iedaas.checklisttask.entity.MyChecklist;
import com.iedaas.checklisttask.service.MyChecklistService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MyChecklistController.class)
class MyChecklistApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MyChecklistService myChecklistService;

    @MockBean
    private AuthorizationFilter authorizationFilter;

    MyChecklist myChecklist1 = new MyChecklist(UUID.fromString("557f0afd-7f20-4840-97d6-7243db63e2a9"), "How to open bank account", "{\"Step1\":\"abc\", \"Step2\":\"def\"}", "in_progress");
    MyChecklist myChecklist2 = new MyChecklist(UUID.fromString("557f0afd-7f20-4840-97d6-7243db63e3a8"), "How to close bank account", "{\"Step1\":\"def\", \"Step2\":\"abc\"}", "completed");

    String token= "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiI4ZjBjOGIwMC1hODdkLTQzNTgtYThhNy02NmQxZjBiNTQwZTYiLCJuYW1lIjoiQWJoYXkiLCJpYXQiOjE1MTYyMzkwMjJ9.ZsHz20KdlFaoCWVS2w3mgofFsaWNVNW2j5CJsoFR_2w";

    @Test
    public void getTasks_success() throws Exception {
        ModelMapper mapper = new ModelMapper();
        MyChecklistDTO myChecklistDTO1 = mapper.map(myChecklist1, MyChecklistDTO.class);
        MyChecklistDTO myChecklistDTO2 = mapper.map(myChecklist2, MyChecklistDTO.class);

        List<MyChecklistDTO> myChecklistDTOList = new ArrayList<>();
        myChecklistDTOList.add(myChecklistDTO1);
        myChecklistDTOList.add(myChecklistDTO2);

        MyChecklistGetDTO myChecklistGetDTO = new MyChecklistGetDTO();
        myChecklistGetDTO.setMyChecklists(myChecklistDTOList);
        myChecklistGetDTO.setTotalCount(2);

        Mockito.when(authorizationFilter.authenticate(Mockito.any())).thenReturn("8f0c8b00-a87d-4358-a8a7-66d1f0b540e6");
        Mockito.when(myChecklistService.getMyChecklists(Mockito.any(),Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(myChecklistGetDTO);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/checklistTasks")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.checklistTasks[0].checklistTaskTitle", Matchers.is(myChecklistDTO1.getMyChecklistTitle())));
    }

    @Test
    public void testGetTaskByUUID() throws Exception{
        ModelMapper mapper = new ModelMapper();
        MyChecklistDTO myChecklistDTO = mapper.map(myChecklist1, MyChecklistDTO.class);

        Mockito.when(authorizationFilter.authenticate(Mockito.any())).thenReturn("8f0c8b00-a87d-4358-a8a7-66d1f0b540e6");
        Mockito.when(myChecklistService.getMyChecklistByUid(UUID.fromString("8f0c8b00-a87d-4358-a8a7-66d1f0b540e6"), UUID.fromString("557f0afd-7f20-4840-97d6-7243db63e2a9"))).thenReturn(myChecklistDTO);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/checklistTask/557f0afd-7f20-4840-97d6-7243db63e2a9")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.checklistTaskTitle", Matchers.is(myChecklistDTO.getMyChecklistTitle())));
    }

    @Test
    public void testAddTask() throws Exception{
        ModelMapper mapper = new ModelMapper();
        MyChecklistDTO myChecklistDTO = mapper.map(myChecklist1, MyChecklistDTO.class);

        Mockito.when(authorizationFilter.authenticate(Mockito.any())).thenReturn("8f0c8b00-a87d-4358-a8a7-66d1f0b540e6");
        Mockito.when(myChecklistService.addMyChecklist(Mockito.any(), Mockito.any())).thenReturn(myChecklistDTO);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token);
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/checklistTask")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(myChecklistDTO))
                .headers(headers);

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.checklistTaskTitle", Matchers.is(myChecklistDTO.getMyChecklistTitle())));
    }

    @Test
    public void testUpdateTask() throws Exception{
        ModelMapper mapper = new ModelMapper();
        MyChecklistDTO myChecklistDTO = mapper.map(myChecklist2, MyChecklistDTO.class);

        Mockito.when(authorizationFilter.authenticate(Mockito.any())).thenReturn("8f0c8b00-a87d-4358-a8a7-66d1f0b540e6");
        Mockito.when(myChecklistService.updateMyChecklist(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(true);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token);
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/checklistTask/557f0afd-7f20-4840-97d6-7243db63e2a9")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(myChecklistDTO))
                .headers(headers);

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk());
    }
}
