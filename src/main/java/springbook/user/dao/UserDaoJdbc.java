package springbook.user.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import springbook.user.domain.Level;
import springbook.user.domain.User;
import springbook.user.sqlservice.SqlService;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

public class UserDaoJdbc implements UserDao{

    private SqlService sqlService;

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void setSqlService(SqlService sqlService) {
        this.sqlService = sqlService;
    }

    private JdbcTemplate jdbcTemplate;
    
    private RowMapper<User> userMapper = new RowMapper<User>() {
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getString("id"));
            user.setName(rs.getString("name"));
            user.setPassword(rs.getString("password"));
            user.setLevel(Level.valueOf(rs.getInt("level")));
            user.setLogin(rs.getInt("login"));
            user.setRecommend(rs.getInt("recommend"));
            user.setEmail(rs.getString("email"));
            return user;
        }
    };

    @Override
    public void add(final User user) {
        jdbcTemplate.update(this.sqlService.getSql("userAdd"),
            user.getId(), user.getName(), user.getPassword(), user.getEmail(), user.getLevel().intValue(), user.getLogin(), user.getRecommend());
    }

    @Override
    public User get(String id) {
        return jdbcTemplate.queryForObject(this.sqlService.getSql("userGet"),
            new Object[] { id },
            userMapper
        );
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query(this.sqlService.getSql("userGetAll"), userMapper);
    }

    @Override
    public void deleteAll() {
        jdbcTemplate.update(this.sqlService.getSql("userDeleteAll"));
    }

    @Override
    public int getCount() {
        return jdbcTemplate.queryForObject(this.sqlService.getSql("userGetCount"), Integer.class);
    }

    @Override
    public void update(User user) {
        this.jdbcTemplate.update(this.sqlService.getSql("userUpdate"),
            user.getName(), user.getPassword(), user.getEmail(), user.getLevel().intValue(), user.getLogin(), user.getRecommend(), user.getId());
    }
    
}
