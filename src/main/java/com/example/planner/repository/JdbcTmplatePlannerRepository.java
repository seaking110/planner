package com.example.planner.repository;

import com.example.planner.dto.PlannerResponseDto;
import com.example.planner.entity.Planner;
import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
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
    public PlannerResponseDto savedPlanner(String task, Long user_id, String password) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("planner").usingGeneratedKeyColumns("id");
        String writer;
        try {
            writer = jdbcTemplate.queryForObject(
                    "SELECT writer FROM user WHERE user_id = ?",
                    new Object[]{user_id},
                    String.class
            );
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalArgumentException("존재하지 않는 유저 아이디 입니다 : " + user_id);
        }
        Date currentDate = new Date();

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("task", task);
        parameters.put("user_id",user_id);
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
    public String findWriterById(Long user_id) {
        String writer;
        try {
            writer = jdbcTemplate.queryForObject(
                    "SELECT writer FROM user WHERE user_id = ?",
                    new Object[]{user_id},
                    String.class
            );
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalArgumentException("존재하지 않는 유저 아이디 입니다 : " + user_id);
        }
        return writer;
    }

    @Override
    public List<PlannerResponseDto> findAllPlanners(Date updated_at, String writer) {
        if (updated_at == null && writer == null) {
            return jdbcTemplate.query("SELECT p.id, p.task, u.writer, p.password, p.created_at, p.updated_at FROM planner p JOIN user u ON p.user_id = u.user_id order by p.updated_at"
                    , plannerRowMapper());
        } else if (updated_at == null) {
            return jdbcTemplate.query("SELECT p.id, p.task, u.writer, p.password, p.created_at, p.updated_at FROM planner p JOIN user u ON p.user_id = u.user_id  where u.writer = ? order by p.updated_at", plannerRowMapper(), writer);
        } else if (writer == null) {
            return jdbcTemplate.query("SELECT p.id, p.task, u.writer, p.password, p.created_at, p.updated_at FROM planner p JOIN user u ON p.user_id = u.user_id  where p.updated_at = ? order by p.updated_at", plannerRowMapper(), updated_at);
        } else {
            return jdbcTemplate.query("SELECT p.id, p.task, u.writer, p.password, p.created_at, p.updated_at FROM planner p JOIN user u ON p.user_id = u.user_id where p.updated_at = ? and u.writer = ? order by p.updated_at", plannerRowMapper(), updated_at, writer);
        }

    }

    @Override
    public int updatePlanner(Long id, String task, Long user_id) {
        Date currentDate = new Date();
        return jdbcTemplate.update("update planner set task = ?, user_id = ?, updated_at = ? where id = ?",task, user_id, currentDate, id);
    }

    @Override
    public String findPassWordById(Long id) {
        String password;
        try {
            password = jdbcTemplate.queryForObject(
                    "SELECT password FROM planner WHERE id = ?",
                    new Object[]{id},
                    String.class
            );
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalArgumentException(" : " + id);
        }
        return password;
    }

    @Override
    public int deletePlanner(Long id) {
        return jdbcTemplate.update("delete from planner where id = ? ",id);
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
                        rs.getLong("user_id"),
                        rs.getString("password"),
                        rs.getDate("created_at"),
                        rs.getDate("updated_at")
                );
            }
        };
    }

}
