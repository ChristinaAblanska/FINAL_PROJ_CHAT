package com.example.chat.repository;

import com.example.chat.enumeration.UserStatus;
import com.example.chat.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@RequiredArgsConstructor
@Repository
public class UserRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public User getById(long id) {
        return jdbcTemplate.queryForObject("""
                SELECT id, first_name, last_name, email, user_name, password, status
                FROM academy_chat.user
                WHERE id=:id;
                """, new MapSqlParameterSource("id", id), new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                User user = new User();
                user.setId(rs.getLong("id"));
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                user.setEmail(rs.getString("email"));
                user.setUserName(rs.getString("user_name"));
                user.setPassword(rs.getString("password"));
                user.setStatus(UserStatus.valueOf(rs.getString("status" )));
                return user;
            }
        });
    }

    public User getByUserName(String userName) {
        return jdbcTemplate.queryForObject("""
                SELECT id, first_name, last_name, email, user_name, password, status
                FROM academy_chat.user
                WHERE user_name=:userName;
                """, new MapSqlParameterSource("userName", userName), new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                User user = new User();
                user.setId(rs.getLong("id"));
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                user.setEmail(rs.getString("email"));
                user.setUserName(rs.getString("user_name"));
                user.setPassword(rs.getString("password"));
                user.setStatus(UserStatus.valueOf(rs.getString("status" )));
                return user;
            }
        });
    }

    public UserStatus getStatusByUserName(String userName) {
        User user = getByUserName(userName);
        return user.getStatus();
    }

    public void save(String firstName, String lastName, String email, String userName, String password, String status) {
        jdbcTemplate.update("""
                INSERT INTO user
                (first_name, last_name, email, user_name, password, status)
                VALUES(:firstName, :lastName, :email, :userName, :password, :status);
                """, new MapSqlParameterSource("firstName", firstName)
                .addValue("lastName", lastName)
                .addValue("email", email)
                .addValue("userName", userName)
                .addValue("password", password)
                .addValue("status", status));
    }

    public void updateStatusByUserName(String userName, String status) {
        jdbcTemplate.update("""
                UPDATE academy_chat.user
                SET status=:status
                WHERE user_name=:userName
                """, new MapSqlParameterSource("userName", userName).addValue("status", status));
    }

    public void delete(long id) {
        jdbcTemplate.update("DELETE FROM user WHERE id=:id", new MapSqlParameterSource("id", id));
    }

    public boolean existsByUserName(String userName) {
        User user = getByUserName(userName);
        return (user != null) || !user.isEmpty();
    }

}