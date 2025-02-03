package com.example.user.repository;

import com.example.planner.dto.PlannerResponseDto;
import com.example.planner.entity.Planner;
import com.example.user.dto.UserResponseDto;
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
public class JdbcTmplateUserRepository implements UserRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcTmplateUserRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    // 유저 정보 저장 메서드
    @Override
    public UserResponseDto savedUser(String writer, String email) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("user").usingGeneratedKeyColumns("user_id");
        Date currentDate = new Date();

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("writer", writer);
        parameters.put("email", email);
        parameters.put("created_at",currentDate);
        parameters.put("updated_at",currentDate);
        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));
        return new UserResponseDto(key.longValue(), writer, email, currentDate, currentDate);
    }

    // 작성자 찾는 메서드
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
}
