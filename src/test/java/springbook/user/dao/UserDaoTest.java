package springbook.user.dao;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import springbook.user.AppContext;
import springbook.user.TestAppContext;
import springbook.user.domain.Level;
import springbook.user.domain.User;

import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * Created by pilhwankim on 15/12/2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestAppContext.class, AppContext.class })
public class UserDaoTest {

    @Autowired
    private UserDao dao;
    
    @Autowired
    private DataSource dataSource;

    private User user1;
    private User user2;
    private User user3;

    @Before
    public void setUp() {
        this.user1 = new User("gyumee", "김필환", "secret2@", Level.BASIC, 1, 0, "gyumee@mail.com");
        this.user2 = new User("leegm700", "이길원", "springno1", Level.SILVER, 55, 10, "gyumee@mail.com");
        this.user3 = new User("bumjin", "박범진", "springno2", Level.GOLD, 100, 40, "gyumee@mail.com");
    }

    @Test
    public void addAndGet() throws SQLException {
        dao.deleteAll();
        assertThat(dao.getCount(), is(0));


        dao.add(user1);
        dao.add(user2);

        assertThat(dao.getCount(), is(2));

        User userget1 = dao.get(user1.getId());
        checkSameUser(userget1, user1);
        
        User userget2 = dao.get(user2.getId());
        checkSameUser(userget2, user2);
    }

    @Test
    public void count() throws SQLException {
        dao.deleteAll();
        assertThat(dao.getCount(), is(0));

        dao.add(user1);
        assertThat(dao.getCount(), is(1));

        dao.add(user2);
        assertThat(dao.getCount(), is(2));

        dao.add(user3);
        assertThat(dao.getCount(), is(3));
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void getUserFailure() throws SQLException {
        dao.deleteAll();
        assertThat(dao.getCount(), is(0));

        dao.get("unknown_id");
    }

    @Test
    public void getAll() throws SQLException {
        dao.deleteAll();

        List<User> users0 = dao.getAll();
        assertEquals(0, users0.size());

        dao.add(user1);
        List<User> users1 = dao.getAll();
        assertEquals(1, users1.size());
        checkSameUser(user1, users1.get(0));

        dao.add(user2);
        List<User> users2 = dao.getAll();
        assertEquals(2, users2.size());
        checkSameUser(user1, users2.get(0));
        checkSameUser(user2, users2.get(1));

        dao.add(user3);
        List<User> users3 = dao.getAll();
        assertEquals(3, users3.size());
        checkSameUser(user3, users3.get(0));
        checkSameUser(user1, users3.get(1));
        checkSameUser(user2, users3.get(2));
    }

    private void checkSameUser(User user1, User user2) {
        assertThat(user1.getId(), is(user2.getId()));
        assertThat(user1.getName(), is(user2.getName()));
        assertThat(user1.getPassword(), is(user2.getPassword()));
        assertThat(user1.getLevel(), is(user2.getLevel()));
        assertThat(user1.getLogin(), is(user2.getLogin()));
        assertThat(user1.getRecommend(), is(user2.getRecommend()));
    }

    @Test(expected = DataAccessException.class)
    public void duplicateKey() {
        dao.deleteAll();

        dao.add(user1);
        dao.add(user1);
    }

    @Test
    @SuppressWarnings("deprecation")
    public void sqlExceptionTranslate() {
        dao.deleteAll();
        
        try {
            dao.add(user1);
            dao.add(user1);
        } catch (DuplicateKeyException e) {
            SQLException sqlEx = (SQLException) e.getRootCause();
            SQLExceptionTranslator set = new SQLErrorCodeSQLExceptionTranslator(dataSource);
            assertThat((DuplicateKeyException) set.translate(null, null, sqlEx), is(DuplicateKeyException.class));
        }
    }

    @Test
    public void update(){
        dao.deleteAll();

        dao.add(user1);
        dao.add(user2);
        
        user1.setName("오민규");
        user1.setPassword("springno6");
        user1.setLevel(Level.GOLD);
        user1.setLogin(1000);
        user1.setRecommend(999);

        dao.update(user1);
        
        User user1update = dao.get(user1.getId());
        checkSameUser(user1, user1update);
        User user2same = dao.get(user2.getId());
        checkSameUser(user2, user2same);
    }
}
