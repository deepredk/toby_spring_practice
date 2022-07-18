package springbook.user.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import springbook.user.domain.User;

import javax.sql.DataSource;
import java.sql.*;

/**
 * UserDao - 1장은 DB connection 관심사를 어떻게 분리할 것인가에 대해 다루면서 조금씩 코드가 개선 중이다.
 */
public class UserDao {
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    public void add(final User user) throws SQLException {
        jdbcTemplate.update("insert into users(id, name, password) values(?, ?, ?)", user.getId(), user.getName(), user.getPassword());
    }

    public User get(String id) throws SQLException {
        return jdbcTemplate.queryForObject("select * from users where id = ?",
            new Object[] { id },
            new RowMapper<User>() {
                public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                    User user = new User();
                    user.setId(rs.getString("id"));
                    user.setName(rs.getString("name"));
                    user.setPassword(rs.getString("password"));
                    return user;
                }
            }
        );
    }

    public void deleteAll() throws SQLException {
        jdbcTemplate.update("delete from users");
    }

    public int getCount() throws SQLException {
        return jdbcTemplate.queryForObject("select count(*) from users", Integer.class);
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

}
