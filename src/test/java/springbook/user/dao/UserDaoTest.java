package springbook.user.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.ejb.access.EjbAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import springbook.user.domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/applicationContext.xml")
public class UserDaoTest {
	@Autowired
	private ConfigurableApplicationContext context;

	private UserDao dao;
	private User user1;
	private User user2;
	private User user3;

	@Before
	public void setUp() {
		this.dao = this.context.getBean("userDao", UserDao.class);
		context.close();

		user1 = new User("gyumee", "박성철", "springno1");
		user2 = new User("leegw700", "이길원", "springno2");
		user3 = new User("bumjin", "박범진", "springno3");
	}

	@Test
	public void addAndGet() throws SQLException, ClassNotFoundException {
		dao.deleteAll();
		assertEquals(dao.getCount(), 0);

		dao.add(user1);
		dao.add(user2);
		assertEquals(dao.getCount(), 2);

		User userget1 = dao.get(user1.getId());
        assertEquals(userget1.getName(), user1.getName());
        assertEquals(userget1.getPassword(), user1.getPassword());

		User userget2 = dao.get(user2.getId());
        assertEquals(userget2.getName(), user2.getName());
        assertEquals(userget2.getPassword(), user2.getPassword());
	}

	@Test
	public void getUserFailure() throws SQLException, ClassNotFoundException {
		dao.deleteAll();
		assertEquals(dao.getCount(), 0);

		assertThrows(EjbAccessException.class, () -> dao.get("unknown_id"));
	}

	@Test
	public void count() throws SQLException, ClassNotFoundException {
		dao.deleteAll();
		assertEquals(dao.getCount(), 0);

		dao.add(user1);
		assertEquals(dao.getCount(), 1);

		dao.add(user2);
		assertEquals(dao.getCount(), 2);

		dao.add(user3);
		assertEquals(dao.getCount(), 3);
	}
}
