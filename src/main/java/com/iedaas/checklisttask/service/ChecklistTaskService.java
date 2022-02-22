package com.iedaas.checklisttask.service;

import com.iedaas.checklisttask.constants.ErrorMessageConstant;
import com.iedaas.checklisttask.dao.ChecklistTaskOwnerRepository;
import com.iedaas.checklisttask.dao.ChecklistTaskRepository;
import com.iedaas.checklisttask.dao.CustomRepository;
import com.iedaas.checklisttask.dto.ChecklistTaskDTO;
import com.iedaas.checklisttask.dto.ChecklistTaskGetDTO;
import com.iedaas.checklisttask.dto.ChecklistTaskOwnerDTO;
import com.iedaas.checklisttask.entity.ChecklistTask;
import com.iedaas.checklisttask.entity.ChecklistTaskOwner;
import com.iedaas.checklisttask.exception.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class ChecklistTaskService {

    private static final Logger logger = LoggerFactory.getLogger(ChecklistTaskService.class);

    @Autowired
    private ChecklistTaskRepository checklistTaskRepository;

    @Autowired
    private ChecklistTaskOwnerRepository checklistTaskOwnerRepository;

    @Autowired
    private CustomRepository customRepository;

    @Autowired
    private ModelMapper modelMapper;

    public ChecklistTaskGetDTO getChecklistTasks(UUID userUid, Optional<Integer> page, Optional<Integer> size,
                                                 Optional<String> order, Optional<String> sort)
    {
        logger.info("userUid:={} fetching for their tasks", userUid);
        Page<ChecklistTask> checklistTaskList = checklistTaskRepository.findUserTasks(String.valueOf(userUid), PageRequest.of(page.orElse(0), size.orElse(5), Sort.Direction.valueOf(order.orElse("DESC")), sort.orElse("updated_date")));
        ChecklistTaskGetDTO checklistTaskGetDTO = new ChecklistTaskGetDTO();
        List<ChecklistTaskDTO> checklistTaskDTOList = new ArrayList<>();
        for(ChecklistTask task : checklistTaskList.getContent()){
            logger.info("userUid:={}, checklistTask:={} before DTO mapping", userUid, task);
            ChecklistTaskDTO checklistTaskDTO = modelMapper.map(task, ChecklistTaskDTO.class);
            logger.info("userUid:={}, checklistTaskDTO:={} after DTO mapping", userUid, checklistTaskDTO);
            ChecklistTaskOwnerDTO checklistTaskOwnerDTO = customRepository.getOwner(userUid);
            checklistTaskDTO.setChecklistTaskOwner(checklistTaskOwnerDTO);
            checklistTaskDTOList.add(checklistTaskDTO);
        }
        logger.debug("userUid:={}, checklistTaskDTO list :={}", userUid, checklistTaskDTOList);
        checklistTaskGetDTO.setChecklistTasks(checklistTaskDTOList);
        checklistTaskGetDTO.setTotalCount(checklistTaskList.getTotalElements());
        logger.debug("userUid:={}, checklistTaskGetDTO:={}", userUid, checklistTaskGetDTO);
        return checklistTaskGetDTO;
    }

    public ChecklistTaskDTO getChecklistTaskByUid(UUID userUid, UUID checklistTaskUid){
        logger.info("userUid :={}, checklistTaskUid :={}", userUid, checklistTaskUid);
        ChecklistTask checklistTask = checklistTaskRepository.findByUUID(String.valueOf(checklistTaskUid));
        if(checklistTask==null){
            logger.error("userUid :={}, checklistTaskUid :={} Not Found", userUid, checklistTaskUid);
            throw new ObjectNotFoundException(String.format(ErrorMessageConstant.NO_CHECKLIST_REQUEST, checklistTaskUid));
        }
        logger.info("userUid :={}, checklistTaskUid :={}, checklistTask :={} before DTO mapping", userUid, checklistTaskUid, checklistTask);
        ChecklistTaskDTO checklistTaskDTO = modelMapper.map(checklistTask, ChecklistTaskDTO.class);
        logger.info("userUid :={}, checklistTaskUid :={}, checklistTaskDTO :={} after DTO mapping", userUid, checklistTaskUid, checklistTaskDTO);
        UUID ownerUid = checklistTaskOwnerRepository.findByUUID(String.valueOf(checklistTaskUid)).getOwnerUid();
        ChecklistTaskOwnerDTO checklistTaskOwnerDTO = customRepository.getOwner(ownerUid);
        checklistTaskDTO.setChecklistTaskOwner(checklistTaskOwnerDTO);
        logger.debug("userUid :={}, checklistTaskUid :={}, result :={}", userUid, checklistTaskUid, checklistTaskDTO);
        return checklistTaskDTO;
    }

    public ChecklistTaskDTO addChecklistTask(UUID userUid, ChecklistTaskDTO checklistTaskDTO){
        logger.info("userUid :={}, checklistTaskDto :={}", userUid, checklistTaskDTO);
        ChecklistTask checklistTask = modelMapper.map(checklistTaskDTO, ChecklistTask.class);
        logger.info("userUid :={}, checklistTask :={} after conversion", userUid, checklistTask);
        ChecklistTask checklistTaskDb = checklistTaskRepository.save(checklistTask);
        ChecklistTaskOwner owner = new ChecklistTaskOwner(userUid, checklistTask.getChecklistTaskUid());
        checklistTaskOwnerRepository.save(owner);
        ChecklistTaskDTO checklistTaskDTODb = modelMapper.map(checklistTaskDb, ChecklistTaskDTO.class);
        ChecklistTaskOwnerDTO checklistTaskOwnerDTO = customRepository.getOwner(userUid);
        checklistTaskDTODb.setChecklistTaskOwner(checklistTaskOwnerDTO);
        logger.debug("userUid :={}, checklistTaskDTO :={}, checklistTaskDbDTO :={}", userUid, checklistTaskDTO, checklistTaskDTODb);
        return checklistTaskDTODb;
    }

    public Boolean updateChecklistTask(UUID userUid, UUID checklistTaskUid, ChecklistTaskDTO checklistTaskDTO){
        logger.info("userUid :={}, checklistTaskUid :={}, checklistTaskDTO :={}", userUid, checklistTaskUid, checklistTaskDTO);
        ChecklistTaskOwner owner = checklistTaskOwnerRepository.findByUUID(String.valueOf(checklistTaskUid));
        logger.info("userUid :={}, checklistTaskUid :={}, ownerUid :={}", userUid, checklistTaskUid, owner.getOwnerUid());
        if(!owner.getOwnerUid().equals(userUid)){
            return false;
        }
        ChecklistTask checklistTask = checklistTaskRepository.findByUUID(String.valueOf(checklistTaskUid));
        logger.info("userUid :={}, checklistTaskUid :{}, checklistTask :={}", userUid, checklistTaskUid, checklistTask);
        if(checklistTask==null){
            logger.error("userUid :={}, checklistTaskUid :={} Not Found", userUid, checklistTaskUid);
            throw new ObjectNotFoundException(String.format(ErrorMessageConstant.NO_CHECKLIST_REQUEST, checklistTaskUid));
        }
        checklistTask.setChecklistTaskTitle(checklistTaskDTO.getChecklistTaskTitle());
        checklistTask.setChecklistTask(checklistTaskDTO.getChecklistTask());
        checklistTaskRepository.save(checklistTask);
        logger.debug("userUid :={}, checklistTaskUid :={}, checklistTaskDTO :={}, updatedTask :={}", userUid, checklistTaskUid, checklistTaskDTO, checklistTask);
        return true;
    }

    public Boolean deleteChecklistTask(UUID userUid, UUID checklistTaskUid){
        logger.info("userUid :={}, checklistTaskUid :={}", userUid, checklistTaskUid);
        ChecklistTaskOwner owner = checklistTaskOwnerRepository.findByUUID(String.valueOf(checklistTaskUid));
        logger.info("userUid :={}, checklistTaskUid :={}, ownerUid :={}", userUid, checklistTaskUid, owner.getOwnerUid());
        if(!owner.getOwnerUid().equals(userUid)){
            return false;
        }
        ChecklistTask checklistTask = checklistTaskRepository.findByUUID(String.valueOf(checklistTaskUid));
        logger.info("userUid :={}, checklistTaskUid :{}, checklistTask :={}", userUid, checklistTaskUid, checklistTask);
        if(checklistTask==null){
            throw new ObjectNotFoundException(String.format(ErrorMessageConstant.NO_CHECKLIST_REQUEST, checklistTaskUid));
        }
        checklistTaskRepository.delete(checklistTask);
        logger.debug("userUid :{}, checklistTaskUid :={}, deleted :={}", userUid, checklistTaskUid, true);
        return true;
    }
}
