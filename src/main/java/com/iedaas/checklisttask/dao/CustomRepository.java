package com.iedaas.checklisttask.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CustomRepository {

    private static final Logger logger = LoggerFactory.getLogger(CustomRepository.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CustomRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public UUID getUserUUID(String userEmail){
        String sql = String.format("select u.user_uid from user u where u.email_id='%s'", userEmail);
        String userUid = jdbcTemplate.queryForObject(sql, String.class);
        return UUID.fromString(userUid);
    }
}
