package com.iedaas.checklisttask.dao;

import com.iedaas.checklisttask.dto.ChecklistTaskOwnerDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class CustomRepository {

    private static final Logger logger = LoggerFactory.getLogger(CustomRepository.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CustomRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public ChecklistTaskOwnerDTO getOwner(UUID userUUID) {
        String sql = "select user_uid as owner_uid, concat(first_name, ' ', last_name) as owner_name from user where user_uid='" + userUUID + "'";
        List<ChecklistTaskOwnerDTO> query = jdbcTemplate.query(sql, (resultSet, i) -> new ChecklistTaskOwnerDTO(UUID.fromString(resultSet.getString(1)), resultSet.getString(2)));
        logger.info("fetching the user details for the uuid:= {}", userUUID);
        ChecklistTaskOwnerDTO ownerDTO = new ChecklistTaskOwnerDTO(query.get(0).getOwnerUid(), query.get(0).getOwnerName());
        logger.debug("Owner := {}", ownerDTO);
        return ownerDTO;
    }
}
