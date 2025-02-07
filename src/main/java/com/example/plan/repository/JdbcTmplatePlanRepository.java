package com.example.plan.repository;

import com.example.plan.dto.PlanResponseDto;
import com.example.plan.entity.Plan;
import com.example.user.entity.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
public class JdbcTmplatePlanRepository implements PlanRepository {
    private final JdbcTemplate jdbcTemplate;

    // JdbcTemplate 초기화
    public JdbcTmplatePlanRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // 일정 저장 메서드
    @Override
    public PlanResponseDto save(String task, Long user_id, String password, String writer) {
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
        return new PlanResponseDto(key.longValue(), task, writer, currentDate, currentDate);
    }

    // 일정 조회 메서드
    @Override
    public Optional<Plan> findPlanById(Long id) {
        List<Plan> result = jdbcTemplate.query("select * from planner where id = ?", planRowMapper2(),id);
        return result.stream().findAny();
    }


    // 일정 전체 조회 메서드
    @Override
    public Page<Plan> findPlans(Date updated_at, String writer, int page, int size) {
        StringBuilder query = new StringBuilder("SELECT p.id, p.task, u.user_id, p.password, p.created_at, p.updated_at FROM planner p JOIN user u ON p.user_id = u.user_id where 1=1 ");
        List <Object> params = new ArrayList<>();
        if (updated_at != null) {
            query.append("and p.updated_at = ?");
            params.add(updated_at);
        }
        if (writer != null) {
            query.append("and u.writer = ?");
            params.add(writer);
        }
        query.append("order by p.updated_at DESC ");
        query.append("LIMIT ? OFFSET ? ");
        params.add(size);
        params.add(page * size);
        String countQuery = "SELECT COUNT(*) FROM planner p JOIN user u ON p.user_id = u.user_id";
        int total = jdbcTemplate.queryForObject(countQuery, Integer.class);
        return jdbcTemplate.query(query.toString(), rs -> {
            List<Plan> plans = new ArrayList<>();
            while (rs.next()) {
                Plan plan = new Plan(
                        rs.getLong("id"),
                        rs.getString("task"),
                        rs.getLong("user_id"),
                        rs.getString("password"),
                        rs.getDate("created_at"),
                        rs.getDate("updated_at")
                );
                plans.add(plan);
            }
            return new PageImpl<>(plans, PageRequest.of(page, size), total);
        }, params.toArray());
    }

    // 일정 수정 메서드
    @Override
    public int updatePlan(Long id, String task, Long user_id) {
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
            return null;
        }
        return password;
    }

    //삭제 메서드
    @Override
    public int deletePlan(Long id) {
        return jdbcTemplate.update("delete from planner where id = ? ",id);
    }

//    // 페이징 일정 조회 메서드
//    @Override
//    public List<PlanResponseDto> findPlannersByPage(int page_num, int page_size) {
//        int offset = (page_num - 1) * page_size;
//        return jdbcTemplate.query("SELECT p.id, p.task, u.writer, p.password, p.created_at, p.updated_at FROM planner p JOIN user u ON p.user_id = u.user_id LIMIT ? OFFSET ?"
//                , planRowMapper(),page_size, offset);
//    }

    private RowMapper<PlanResponseDto> planRowMapper() {
        return new RowMapper<PlanResponseDto>() {
            @Override
            public PlanResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new PlanResponseDto(
                        rs.getLong("id"),
                        rs.getString("task"),
                        rs.getString("writer"),
                        rs.getDate("created_at"),
                        rs.getDate("updated_at")
                );
            }
        };
    }

    private RowMapper<Plan> planRowMapper2() {
        return new RowMapper<Plan>() {
            @Override
            public Plan mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Plan(rs.getLong("id"),
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
