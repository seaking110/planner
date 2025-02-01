package com.example.planner.repository;

import com.example.planner.dto.PlannerResponseDto;
import com.example.planner.entity.Planner;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

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

    @Override
    public Optional<Planner> findPlannerById(Long id) {
        List<Planner> result = jdbcTemplate.query("select * from planner where id = ?", plannerRowMapper2(),id);
        return result.stream().findAny();
    }

    @Override
    public List<PlannerResponseDto> findAllPlanners() {
        return jdbcTemplate.query("select * from planner", plannerRowMapper());
    }


    private RowMapper<PlannerResponseDto> plannerRowMapper() {
        return new RowMapper<PlannerResponseDto>() {
            @Override
            public PlannerResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new PlannerResponseDto(
                        rs.getLong("id"),
                        rs.getString("task"),
                        rs.getString("writer"),
                        rs.getDate("created_at"),
                        rs.getDate("updated_at")
                );
            }
        };
    }

    private RowMapper<Planner> plannerRowMapper2() {
        return new RowMapper<Planner>() {
            @Override
            public Planner mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Planner(rs.getLong("id"),
                rs.getString("task"),
                        rs.getString("writer"),
                        rs.getString("password"),
                        rs.getDate("created_at"),
                        rs.getDate("updated_at")
                );
            }
        };
    }

}
