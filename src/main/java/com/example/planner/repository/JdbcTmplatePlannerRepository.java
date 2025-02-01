package com.example.planner.repository;

import com.example.planner.dto.PlannerResponseDto;
import com.example.planner.entity.Planner;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;

@Repository
public class JdbcTmplatePlannerRepository implements PlannerRepository{
    private final JdbcTemplate jdbcTemplate;

    public JdbcTmplatePlannerRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    @Override
    public PlannerResponseDto savedPlanner(String task, String writer, String password) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("planner").usingGeneratedKeyColumns("id");

        Date currentDate = new Date();

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("task", task);
        parameters.put("writer",writer);
        parameters.put("password",password);
        parameters.put("created_at",currentDate);
        parameters.put("updated_at",currentDate);
        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));
        return new PlannerResponseDto(key.longValue(), task, writer, currentDate, currentDate);
    }
}
