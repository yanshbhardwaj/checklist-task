package com.iedaas.checklisttask;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iedaas.checklisttask.controller.ChecklistTaskController;
import com.iedaas.checklisttask.dto.ChecklistTaskDTO;
import com.iedaas.checklisttask.dto.ChecklistTaskGetDTO;
import com.iedaas.checklisttask.entity.ChecklistTask;
import com.iedaas.checklisttask.service.ChecklistTaskService;
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
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ChecklistTaskController.class)
class ChecklistTaskApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ChecklistTaskService checklistTaskService;

    @MockBean
    private AuthorizationFilter authorizationFilter;

    ChecklistTask checklistTask1 = new ChecklistTask(UUID.fromString("557f0afd-7f20-4840-97d6-7243db63e2a9"), "How to open bank account", "{\"Step1\":\"abc\", \"Step2\":\"def\"}");
    ChecklistTask checklistTask2 = new ChecklistTask(UUID.fromString("557f0afd-7f20-4840-97d6-7243db63e3a8"), "How to close bank account", "{\"Step1\":\"def\", \"Step2\":\"abc\"}");

    String token= "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiI4ZjBjOGIwMC1hODdkLTQzNTgtYThhNy02NmQxZjBiNTQwZTYiLCJuYW1lIjoiQWJoYXkiLCJpYXQiOjE1MTYyMzkwMjJ9.ZsHz20KdlFaoCWVS2w3mgofFsaWNVNW2j5CJsoFR_2w";

    @Test
    public void getTasks_success() throws Exception {
        ModelMapper mapper = new ModelMapper();
        ChecklistTaskDTO checklistTaskDTO1 = mapper.map(checklistTask1, ChecklistTaskDTO.class);
        ChecklistTaskDTO checklistTaskDTO2 = mapper.map(checklistTask2, ChecklistTaskDTO.class);

        List<ChecklistTaskDTO> checklistTaskDTOList = new ArrayList<>();
        checklistTaskDTOList.add(checklistTaskDTO1);
        checklistTaskDTOList.add(checklistTaskDTO2);

        ChecklistTaskGetDTO checklistTaskGetDTO = new ChecklistTaskGetDTO();
        checklistTaskGetDTO.setChecklistTasks(checklistTaskDTOList);
        checklistTaskGetDTO.setTotalCount(2);

        Mockito.when(authorizationFilter.authenticate(Mockito.any())).thenReturn("8f0c8b00-a87d-4358-a8a7-66d1f0b540e6");
        Mockito.when(checklistTaskService.getChecklistTasks(Mockito.any(),Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(checklistTaskGetDTO);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/checklistTasks")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.checklistTasks[0].checklistTaskTitle", Matchers.is(checklistTaskDTO1.getChecklistTaskTitle())));
    }

    @Test
    public void testGetTaskByUUID() throws Exception{
        ModelMapper mapper = new ModelMapper();
        ChecklistTaskDTO checklistTaskDTO = mapper.map(checklistTask1, ChecklistTaskDTO.class);

        Mockito.when(authorizationFilter.authenticate(Mockito.any())).thenReturn("8f0c8b00-a87d-4358-a8a7-66d1f0b540e6");
        Mockito.when(checklistTaskService.getChecklistTaskByUid(UUID.fromString("8f0c8b00-a87d-4358-a8a7-66d1f0b540e6"), UUID.fromString("557f0afd-7f20-4840-97d6-7243db63e2a9"))).thenReturn(checklistTaskDTO);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/checklistTask/557f0afd-7f20-4840-97d6-7243db63e2a9")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.checklistTaskTitle", Matchers.is(checklistTaskDTO.getChecklistTaskTitle())));
    }

    @Test
    public void testAddTask() throws Exception{
        ModelMapper mapper = new ModelMapper();
        ChecklistTaskDTO checklistTaskDTO = mapper.map(checklistTask1, ChecklistTaskDTO.class);

        Mockito.when(authorizationFilter.authenticate(Mockito.any())).thenReturn("8f0c8b00-a87d-4358-a8a7-66d1f0b540e6");
        Mockito.when(checklistTaskService.addChecklistTask(Mockito.any(), Mockito.any())).thenReturn(checklistTaskDTO);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token);
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/checklistTask")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(checklistTaskDTO))
                .headers(headers);

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.checklistTaskTitle", Matchers.is(checklistTaskDTO.getChecklistTaskTitle())));
    }

    @Test
    public void testUpdateTask() throws Exception{
        ModelMapper mapper = new ModelMapper();
        ChecklistTaskDTO checklistTaskDTO = mapper.map(checklistTask2, ChecklistTaskDTO.class);

        Mockito.when(authorizationFilter.authenticate(Mockito.any())).thenReturn("8f0c8b00-a87d-4358-a8a7-66d1f0b540e6");
        Mockito.when(checklistTaskService.updateChecklistTask(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(true);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token);
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/checklistTask/557f0afd-7f20-4840-97d6-7243db63e2a9")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(checklistTaskDTO))
                .headers(headers);

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk());
    }
}
