package com.LionKing.Teamply.global.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DbFixer {

    private final JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void fixDatabase() {
        try {
            log.info("Attempting to drop the unused 'name' column from 'projects' table...");
            jdbcTemplate.execute("ALTER TABLE projects DROP COLUMN name");
            log.info("Successfully dropped 'name' column from 'projects' table!");
        } catch (Exception e) {
            log.warn("Could not drop 'name' column (it might have already been deleted): {}", e.getMessage());
        }
    }
}
