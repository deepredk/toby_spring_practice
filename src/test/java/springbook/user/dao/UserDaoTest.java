package springbook.user.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.SQLException;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import springbook.user.domain.User;

public class UserDaoTest {

	@Test
	public void addAndGet() throws SQLException, ClassNotFoundException {
		ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
		UserDao dao = context.getBean("userDao", UserDao.class);

		dao.deleteAll();
		assertEquals(dao.getCount(), 0);

		User user = new User();
		user.setId("gyumee");
		user.setName("박성철");
		user.setPassword("springno1");

		dao.add(user);
		assertEquals(dao.getCount(), 1);

		User user2 = dao.get(user.getId());
        assertEquals(user.getName(), user2.getName());
        assertEquals(user.getPassword(), user2.getPassword());

		((ConfigurableApplicationContext) context).close();
	}

}
