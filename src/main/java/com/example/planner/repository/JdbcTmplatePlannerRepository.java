package com.example.planner.repository;

import com.example.planner.dto.PlannerResponseDto;
import com.example.planner.entity.Planner;
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

    // JdbcTemplate 초기화
    public JdbcTmplatePlannerRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // 일정 저장 메서드
    @Override
    public PlannerResponseDto savedPlanner(String task, Long user_id, String password, String writer) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("planner").usingGeneratedKeyColumns("id");
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

    // 일정 조회 메서드
    @Override
    public Optional<Planner> findPlannerById(Long id) {
        List<Planner> result = jdbcTemplate.query("select * from planner where id = ?", plannerRowMapper2(),id);
        return result.stream().findAny();
    }


    // 일정 전체 조회 메서드
    @Override
    public List<PlannerResponseDto> findAllPlanners(Date updated_at, String writer) {
        // 모두 있는 경우 수정일만 있는 경우, 작성자만 있는 경우 둘다 없는 경우 4가지로 구분
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

    // 일정 수정 메서드
    @Override
    public int updatePlanner(Long id, String task, Long user_id) {
        Date currentDate = new Date();
        return jdbcTemplate.update("update planner set task = ?, user_id = ?, updated_at = ? where id = ?",task, user_id, currentDate, id);
    }

    // 비밀번호 얻는 메서드
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

    //삭제 메서드
    @Override
    public int deletePlanner(Long id) {
        return jdbcTemplate.update("delete from planner where id = ? ",id);
    }

    // 페이징 일정 조회 메서드
    @Override
    public List<PlannerResponseDto> findPlannersByPage(int page_num, int page_size) {
        int offset = (page_num - 1) * page_size;
        return jdbcTemplate.query("SELECT p.id, p.task, u.writer, p.password, p.created_at, p.updated_at FROM planner p JOIN user u ON p.user_id = u.user_id LIMIT ? OFFSET ?"
                , plannerRowMapper(),page_size, offset);
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
