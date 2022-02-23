package com.iedaas.checklisttask.controller;

import com.iedaas.checklisttask.AuthorizationFilter;
import com.iedaas.checklisttask.constants.ErrorMessageConstant;
import com.iedaas.checklisttask.dto.MyChecklistDTO;
import com.iedaas.checklisttask.dto.MyChecklistGetDTO;
import com.iedaas.checklisttask.exception.ObjectNotFoundException;
import com.iedaas.checklisttask.service.MyChecklistService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

@RestController
public class MyChecklistController {

    private static final Logger logger = LoggerFactory.getLogger(MyChecklistController.class);

    @Autowired
    AuthorizationFilter authorizationFilter;

    @Autowired
    MyChecklistService myChecklistService;

    @PostMapping("/myChecklist")
    public MyChecklistDTO addMyChecklist(HttpServletRequest request, @Valid @RequestBody MyChecklistDTO myChecklistDTO){
        UUID userUid = UUID.fromString(authorizationFilter.authenticate(request));
        logger.info("user uid :={} adding myChecklistDTO :={}", userUid, myChecklistDTO);
        MyChecklistDTO myChecklistDTODb = myChecklistService.addMyChecklist(userUid, myChecklistDTO);
        logger.debug("user uid :={}, added myChecklistDTO :={}", userUid, myChecklistDTODb);
        return myChecklistDTODb;
    }

    @GetMapping("/myChecklists")
    public MyChecklistGetDTO getMyChecklists(HttpServletRequest request, @RequestParam Optional<Integer> page, @RequestParam Optional<Integer> size,
                                               @RequestParam Optional<String> order, @RequestParam Optional<String> sort)
    {
        UUID userUid = UUID.fromString(authorizationFilter.authenticate(request));
        logger.info("user uid :={} fetching for their checklists", userUid);
        if(page.isPresent()){
            if(page.get()<=0) {
                logger.warn("user uid :={}, page :={} can not be less than 1", userUid, page.get());
                throw new IllegalArgumentException(ErrorMessageConstant.ILLEGAL_PAGE);
            }
            page = Optional.of(page.get()-1);
        } else {
            page = Optional.of(0);
        }
        MyChecklistGetDTO myChecklistGetDTO = myChecklistService.getMyChecklists(userUid, page,size,order,sort);
        logger.debug("user uid :={}, myChecklists :={}", userUid, myChecklistGetDTO);
        return myChecklistGetDTO;
    }

    @GetMapping("/myChecklists/{status}")
    public MyChecklistGetDTO getMyChecklists(HttpServletRequest request, @PathVariable String status, @RequestParam Optional<Integer> page, @RequestParam Optional<Integer> size,
                                             @RequestParam Optional<String> order, @RequestParam Optional<String> sort)
    {
        UUID userUid = UUID.fromString(authorizationFilter.authenticate(request));
        logger.info("user uid :={}, myChecklistStatus :={} fetching for their checklists", userUid, status);
        if(page.isPresent()){
            if(page.get()<=0) {
                logger.warn("user uid :={}, page :={} can not be less than 1", userUid, page.get());
                throw new IllegalArgumentException(ErrorMessageConstant.ILLEGAL_PAGE);
            }
            page = Optional.of(page.get()-1);
        } else {
            page = Optional.of(0);
        }
        MyChecklistGetDTO myChecklistGetDTO = myChecklistService.getMyChecklistsByStatus(userUid, status, page,size,order,sort);
        logger.debug("user uid :={}, myChecklistStatus :={}, myChecklists :={}", userUid, status, myChecklistGetDTO);
        return myChecklistGetDTO;
    }

    @GetMapping("/myChecklist/{myChecklistUid}")
    public MyChecklistDTO getMyChecklistByUid(HttpServletRequest request, @PathVariable UUID myChecklistUid){
        UUID userUid = UUID.fromString(authorizationFilter.authenticate(request));
        logger.info("user uid ={}, myChecklistUid :={}", userUid, myChecklistUid);
        MyChecklistDTO myChecklistDTO = null;
        try {
            myChecklistDTO = myChecklistService.getMyChecklistByUid(userUid, myChecklistUid);
            logger.debug("userUid :={}, myChecklistUid :={}, myChecklistDTO :={}",userUid, myChecklistUid, myChecklistDTO);
        }
        catch (ObjectNotFoundException e){
            logger.error("userUid :={}, myChecklistUid :={}, Not Found", userUid, myChecklistUid);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return myChecklistDTO;
    }

    @PutMapping("/myChecklist/{myChecklistUid}")
    public void updateMyChecklist(HttpServletRequest request, @PathVariable UUID myChecklistUid,@Valid @RequestBody MyChecklistDTO myChecklistDTO)
    {
        UUID userUid = UUID.fromString(authorizationFilter.authenticate(request));
        logger.info("userUid :={}, myChecklistUid :={}, myChecklistDTO :={}", userUid, myChecklistUid, myChecklistDTO);
        boolean updated;
        try {
            updated = myChecklistService.updateMyChecklist(userUid, myChecklistUid, myChecklistDTO);
            logger.debug("userUid :={}, myChecklistUid :={}, myChecklistDTO:={}, updated:={}",userUid, myChecklistUid, myChecklistDTO, updated);
        }
        catch (ObjectNotFoundException e){
            logger.error("userUid :={}, myChecklistUid :={} Not Found", userUid, myChecklistUid);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        if(!updated){
            logger.warn("userUid :={}, myChecklistUid :={} not authorised for updation", userUid, myChecklistUid);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, ErrorMessageConstant.UNAUTHORISED_USER);
        }
    }

    @DeleteMapping("/myChecklist/{myChecklistUid}")
    public void deleteChecklistTask(HttpServletRequest request, @PathVariable UUID myChecklistUid){
        UUID userUid = UUID.fromString(authorizationFilter.authenticate(request));
        logger.info("userUid :={}, myChecklistUid :={}", userUid, myChecklistUid);
        boolean deleted;
        try {
            deleted = myChecklistService.deleteMyChecklist(userUid, myChecklistUid);
            logger.debug("userUid :={}, myChecklistUid :={}, deleted:={}",userUid, myChecklistUid, deleted);

        }
        catch (ObjectNotFoundException e){
            logger.error("userUid :={}, myChecklistUid :={} Not Found", userUid, myChecklistUid);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        if(!deleted){
            logger.warn("userUid :={}, myChecklistUid :={} not authorised to delete", userUid, myChecklistUid);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, ErrorMessageConstant.UNAUTHORISED_USER);
        }
    }
}
