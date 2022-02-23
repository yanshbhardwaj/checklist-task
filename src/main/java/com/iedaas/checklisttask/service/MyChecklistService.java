package com.iedaas.checklisttask.service;

import com.iedaas.checklisttask.constants.ErrorMessageConstant;
import com.iedaas.checklisttask.dao.MyChecklistOwnerRepository;
import com.iedaas.checklisttask.dao.MyChecklistRepository;
import com.iedaas.checklisttask.dto.MyChecklistDTO;
import com.iedaas.checklisttask.dto.MyChecklistGetDTO;
import com.iedaas.checklisttask.entity.MyChecklist;
import com.iedaas.checklisttask.entity.MyChecklistOwner;
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
public class MyChecklistService {

    private static final Logger logger = LoggerFactory.getLogger(MyChecklistService.class);

    @Autowired
    private MyChecklistRepository myChecklistRepository;

    @Autowired
    private MyChecklistOwnerRepository myChecklistOwnerRepository;

    @Autowired
    private ModelMapper modelMapper;

    public MyChecklistGetDTO getMyChecklists(UUID userUid, Optional<Integer> page, Optional<Integer> size,
                                               Optional<String> order, Optional<String> sort)
    {
        logger.info("userUid:={} fetching for their checklists", userUid);
        Page<MyChecklist> myChecklistPage = myChecklistRepository.findUserTasks(String.valueOf(userUid), PageRequest.of(page.orElse(0), size.orElse(5), Sort.Direction.valueOf(order.orElse("DESC")), sort.orElse("updated_date")));
        MyChecklistGetDTO myChecklistGetDTO = new MyChecklistGetDTO();
        List<MyChecklistDTO> myChecklistDTOList = new ArrayList<>();
        for(MyChecklist myChecklist : myChecklistPage.getContent()){
            logger.info("userUid:={}, myChecklist:={} before DTO mapping", userUid, myChecklist);
            MyChecklistDTO myChecklistDTO = modelMapper.map(myChecklist, MyChecklistDTO.class);
            logger.info("userUid:={}, myChecklistDTO:={} after DTO mapping", userUid, myChecklistDTO);
            myChecklistDTOList.add(myChecklistDTO);
        }
        logger.debug("userUid:={}, myChecklistDTO list :={}", userUid, myChecklistDTOList);
        myChecklistGetDTO.setMyChecklists(myChecklistDTOList);
        myChecklistGetDTO.setTotalCount(myChecklistPage.getTotalElements());
        logger.debug("userUid:={}, myChecklistGetDTO:={}", userUid, myChecklistGetDTO);
        return myChecklistGetDTO;
    }

    public MyChecklistGetDTO getMyChecklistsByStatus(UUID userUid, String status, Optional<Integer> page, Optional<Integer> size,
                                                     Optional<String> order, Optional<String> sort){
        logger.info("userUid:={}, myChecklistStatus:={} fetching for their checklists", userUid, status);
        Page<MyChecklist> myChecklistPage = myChecklistRepository.findByStatus(String.valueOf(userUid), status, PageRequest.of(page.orElse(0), size.orElse(5), Sort.Direction.valueOf(order.orElse("DESC")), sort.orElse("updated_date")));
        MyChecklistGetDTO myChecklistGetDTO = new MyChecklistGetDTO();
        List<MyChecklistDTO> myChecklistDTOList = new ArrayList<>();
        for(MyChecklist myChecklist : myChecklistPage.getContent()){
            logger.info("userUid:={}, myChecklistStatus:={},  myChecklist:={} before DTO mapping", userUid, status, myChecklist);
            MyChecklistDTO myChecklistDTO = modelMapper.map(myChecklist, MyChecklistDTO.class);
            logger.info("userUid:={}, myChecklistStatus:={}, myChecklistDTO:={} after DTO mapping", userUid, status, myChecklistDTO);
            myChecklistDTOList.add(myChecklistDTO);
        }
        logger.debug("userUid:={}, myChecklistStatus:={}, myChecklistDTO list :={}", userUid, status, myChecklistDTOList);
        myChecklistGetDTO.setMyChecklists(myChecklistDTOList);
        myChecklistGetDTO.setTotalCount(myChecklistPage.getTotalElements());
        logger.debug("userUid:={}, myChecklistStatus:={}, myChecklistGetDTO:={}", userUid, status, myChecklistGetDTO);
        return myChecklistGetDTO;
    }

    public MyChecklistDTO getMyChecklistByUid(UUID userUid, UUID myChecklistUid){
        logger.info("userUid :={}, myChecklistUid :={}", userUid, myChecklistUid);
        MyChecklist myChecklist = myChecklistRepository.findByUUID(String.valueOf(myChecklistUid));
        if(myChecklist ==null){
            logger.error("userUid :={}, myChecklistUid :={} Not Found", userUid, myChecklistUid);
            throw new ObjectNotFoundException(String.format(ErrorMessageConstant.NO_CHECKLIST_REQUEST, myChecklistUid));
        }
        logger.info("userUid :={}, myChecklistUid :={}, myChecklist :={} before DTO mapping", userUid, myChecklistUid, myChecklist);
        MyChecklistDTO myChecklistDTO = modelMapper.map(myChecklist, MyChecklistDTO.class);
        logger.info("userUid :={}, myChecklistUid :={}, myChecklistDTO :={} after DTO mapping", userUid, myChecklistUid, myChecklistDTO);
        logger.debug("userUid :={}, myChecklistUid :={}, result :={}", userUid, myChecklistUid, myChecklistDTO);
        return myChecklistDTO;
    }

    public MyChecklistDTO addMyChecklist(UUID userUid, MyChecklistDTO myChecklistDTO){
        logger.info("userUid :={}, checklistTaskDto :={}", userUid, myChecklistDTO);
        MyChecklist myChecklist = modelMapper.map(myChecklistDTO, MyChecklist.class);
        logger.info("userUid :={}, myChecklist :={} after conversion", userUid, myChecklist);
        MyChecklist myChecklistDb = myChecklistRepository.save(myChecklist);
        MyChecklistOwner owner = new MyChecklistOwner(userUid, myChecklist.getMyChecklistUid());
        myChecklistOwnerRepository.save(owner);
        MyChecklistDTO myChecklistDTODb = modelMapper.map(myChecklistDb, MyChecklistDTO.class);
        logger.debug("userUid :={}, myChecklistDTO :={}, checklistTaskDbDTO :={}", userUid, myChecklistDTO, myChecklistDTODb);
        return myChecklistDTODb;
    }

    public Boolean updateMyChecklist(UUID userUid, UUID myChecklistUid, MyChecklistDTO myChecklistDTO){
        logger.info("userUid :={}, myChecklistUid :={}, myChecklistDTO :={}", userUid, myChecklistUid, myChecklistDTO);
        MyChecklistOwner owner = myChecklistOwnerRepository.findByUUID(String.valueOf(myChecklistUid));
        logger.info("userUid :={}, myChecklistUid :={}, ownerUid :={}", userUid, myChecklistUid, owner.getOwnerUid());
        if(!owner.getOwnerUid().equals(userUid)){
            return false;
        }
        MyChecklist myChecklist = myChecklistRepository.findByUUID(String.valueOf(myChecklistUid));
        logger.info("userUid :={}, myChecklistUid :{}, myChecklist :={}", userUid, myChecklistUid, myChecklist);
        if(myChecklist ==null){
            logger.error("userUid :={}, myChecklistUid :={} Not Found", userUid, myChecklistUid);
            throw new ObjectNotFoundException(String.format(ErrorMessageConstant.NO_CHECKLIST_REQUEST, myChecklistUid));
        }
        myChecklist.setMyChecklistTitle(myChecklistDTO.getMyChecklistTitle());
        myChecklist.setMyChecklist(myChecklistDTO.getMyChecklist());
        myChecklist.setMyChecklistStatus(myChecklistDTO.getMyChecklistStatus());
        myChecklistRepository.save(myChecklist);
        logger.debug("userUid :={}, myChecklistUid :={}, myChecklistDTO :={}, updatedTask :={}", userUid, myChecklistUid, myChecklistDTO, myChecklist);
        return true;
    }

    public Boolean deleteMyChecklist(UUID userUid, UUID myChecklistUid){
        logger.info("userUid :={}, myChecklistUid :={}", userUid, myChecklistUid);
        MyChecklistOwner owner = myChecklistOwnerRepository.findByUUID(String.valueOf(myChecklistUid));
        logger.info("userUid :={}, myChecklistUid :={}, ownerUid :={}", userUid, myChecklistUid, owner.getOwnerUid());
        if(!owner.getOwnerUid().equals(userUid)){
            return false;
        }
        MyChecklist myChecklist = myChecklistRepository.findByUUID(String.valueOf(myChecklistUid));
        logger.info("userUid :={}, myChecklistUid :{}, myChecklist :={}", userUid, myChecklistUid, myChecklist);
        if(myChecklist ==null){
            throw new ObjectNotFoundException(String.format(ErrorMessageConstant.NO_CHECKLIST_REQUEST, myChecklistUid));
        }
        myChecklistRepository.delete(myChecklist);
        logger.debug("userUid :{}, myChecklistUid :={}, deleted :={}", userUid, myChecklistUid, true);
        return true;
    }
}
