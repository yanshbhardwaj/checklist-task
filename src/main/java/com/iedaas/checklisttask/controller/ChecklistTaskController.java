package com.iedaas.checklisttask.controller;

import com.iedaas.checklisttask.AuthorizationFilter;
import com.iedaas.checklisttask.constants.ErrorMessageConstant;
import com.iedaas.checklisttask.dto.ChecklistTaskDTO;
import com.iedaas.checklisttask.dto.ChecklistTaskGetDTO;
import com.iedaas.checklisttask.exception.ObjectNotFoundException;
import com.iedaas.checklisttask.service.ChecklistTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.UUID;

@RestController
public class ChecklistTaskController {

    private static final Logger logger = LoggerFactory.getLogger(ChecklistTaskController.class);

    @Autowired
    AuthorizationFilter authorizationFilter;

    @Autowired
    ChecklistTaskService checklistTaskService;

    @PostMapping("/checklistTask")
    public ChecklistTaskDTO addChecklistTask(HttpServletRequest request, @RequestBody ChecklistTaskDTO checklistTaskDTO){
        UUID userUid = UUID.fromString(authorizationFilter.authenticate(request));
        logger.info("user uid :={} adding checklist task DTO :={}", userUid, checklistTaskDTO);
        ChecklistTaskDTO checklistTaskDTODb = checklistTaskService.addChecklistTask(userUid, checklistTaskDTO);
        logger.debug("user uid :={}, added checklist task DTO :={}", userUid, checklistTaskDTODb);
        return checklistTaskDTODb;
    }

    @GetMapping("/checklistTasks")
    public ChecklistTaskGetDTO getChecklistTasks(HttpServletRequest request, @RequestParam Optional<Integer> page, @RequestParam Optional<Integer> size,
                                                 @RequestParam Optional<String> order, @RequestParam Optional<String> sort)
    {
        UUID userUid = UUID.fromString(authorizationFilter.authenticate(request));
        logger.info("user uid :={} fetching for their tasks", userUid);
        if(page.isPresent()){
            if(page.get()<=0) {
                logger.warn("user uid :={}, page :={} can not be less than 1", userUid, page.get());
                throw new IllegalArgumentException(ErrorMessageConstant.ILLEGAL_PAGE);
            }
            page = Optional.of(page.get()-1);
        } else {
            page = Optional.of(0);
        }
        ChecklistTaskGetDTO checklistTaskGetDTO = checklistTaskService.getChecklistTasks(userUid, page,size,order,sort);
        logger.debug("user uid :={}, checklist tasks :={}", userUid, checklistTaskGetDTO);
        return checklistTaskGetDTO;
    }

    @GetMapping("/checklistTask/{checklistTaskUid}")
    public ChecklistTaskDTO getChecklistTaskByUid(HttpServletRequest request, @PathVariable UUID checklistTaskUid){
        UUID userUid = UUID.fromString(authorizationFilter.authenticate(request));
        logger.info("user uid ={}, checklistTaskUid :={}", userUid, checklistTaskUid);
        ChecklistTaskDTO checklistTaskDTO = null;
        try {
            checklistTaskDTO = checklistTaskService.getChecklistTaskByUid(userUid, checklistTaskUid);
            logger.debug("userUid :={}, checklistTaskUid :={}, checklistTaskDTO :={}",userUid, checklistTaskUid, checklistTaskDTO);
        }
        catch (ObjectNotFoundException e){
            logger.error("userUid :={}, checklistTaskUid :={}, Not Found", userUid, checklistTaskUid);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return checklistTaskDTO;
    }

    @PutMapping("/checklistTask/{checklistTaskUid}")
    public void updateChecklistTask(HttpServletRequest request, @PathVariable UUID checklistTaskUid, @RequestBody ChecklistTaskDTO checklistTaskDTO)
    {
        UUID userUid = UUID.fromString(authorizationFilter.authenticate(request));
        logger.info("userUid :={}, checklistTaskUid :={}, checklistTaskDTO :={}", userUid, checklistTaskUid, checklistTaskDTO);
        boolean updated;
        try {
            updated = checklistTaskService.updateChecklistTask(userUid, checklistTaskUid, checklistTaskDTO);
            logger.debug("userUid :={}, checklistTaskUid :={}, checklistTaskDTO:={}, updated:={}",userUid, checklistTaskUid, checklistTaskDTO, updated);
        }
        catch (ObjectNotFoundException e){
            logger.error("userUid :={}, checklistTaskUid :={} Not Found", userUid, checklistTaskUid);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        if(!updated){
            logger.warn("userUid :={}, checklistTaskUid :={} not authorised for updation", userUid, checklistTaskUid);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, ErrorMessageConstant.UNAUTHORISED_USER);
        }
    }

    @DeleteMapping("/checklistTask/{checklistTaskUid}")
    public void deleteChecklistTask(HttpServletRequest request, @PathVariable UUID checklistTaskUid){
        UUID userUid = UUID.fromString(authorizationFilter.authenticate(request));
        logger.info("userUid :={}, checklistTaskUid :={}", userUid, checklistTaskUid);
        boolean deleted;
        try {
            deleted = checklistTaskService.deleteChecklistTask(userUid, checklistTaskUid);
            logger.debug("userUid :={}, checklistTaskUid :={}, deleted:={}",userUid, checklistTaskUid, deleted);

        }
        catch (ObjectNotFoundException e){
            logger.error("userUid :={}, checklistTaskUid :={} Not Found", userUid, checklistTaskUid);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        if(!deleted){
            logger.warn("userUid :={}, checklistTaskUid :={} not authorised to delete", userUid, checklistTaskUid);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, ErrorMessageConstant.UNAUTHORISED_USER);
        }
    }
}
