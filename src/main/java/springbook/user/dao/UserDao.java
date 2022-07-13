package springbook.user.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import springbook.user.domain.User;

import javax.sql.DataSource;
import java.sql.*;

/**
 * UserDao - 1장은 DB connection 관심사를 어떻게 분리할 것인가에 대해 다루면서 조금씩 코드가 개선 중이다.
 */
public class UserDao {

    private DataSource dataSource;

    public void add(final User user) throws SQLException {
        jdbcContextWithStatementStrategy(
            new StatementStrategy() {
                @Override
                public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
                    PreparedStatement ps = c.prepareStatement("insert into users(id, name, password) values(?, ?, ?)");
                    ps.setString(1, user.getId());
                    ps.setString(2, user.getName());
                    ps.setString(3, user.getPassword());
            
                    return ps;
                }
            }
        );
    }

    public User get(String id) throws SQLException {
        Connection c = dataSource.getConnection();

        PreparedStatement ps = c.prepareStatement("select * from users where id = ?");
        ps.setString(1, id);

        ResultSet rs = ps.executeQuery();
        User user = null;

        if (rs.next()) {
            user = new User();
            user.setId(rs.getString("id"));
            user.setName(rs.getString("name"));
            user.setPassword(rs.getString("password"));

        }

        rs.close();
        ps.close();
        c.close();

        if(user == null) throw new EmptyResultDataAccessException(1);

        return user;
    }

    public void deleteAll() throws SQLException {
        jdbcContextWithStatementStrategy(
            new StatementStrategy() {
                @Override
                public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
                    PreparedStatement ps = c.prepareStatement("delete from users");
                    return ps;
                }
            }
        );
    }

    public void jdbcContextWithStatementStrategy(StatementStrategy stmt) throws SQLException {
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = stmt.makePreparedStatement(c)) {
            ps.executeUpdate();
        } catch(SQLException e) {
            throw e;
        }
    }

    public int getCount() throws SQLException {
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement("select count(*) from users");
             ResultSet rs = ps.executeQuery()) {
            rs.next();
            int count = rs.getInt(1);
            return count;
        } catch(SQLException e) {
            throw e;
        }
    }

    public void setDataSource(DataSource dataSource) { this.dataSource = dataSource; }
}
