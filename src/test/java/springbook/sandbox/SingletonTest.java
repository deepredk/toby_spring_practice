package springbook.sandbox;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import springbook.user.dao.DaoFactory;
import springbook.user.dao.UserDaoJdbc;

import static org.hamcrest.CoreMatchers.sameInstance;

public class SingletonTest {
	@Test
	public void testSingletonInSpringContext() {
		ConfigurableApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
		Assert.assertThat(context.getBean("userDao", UserDaoJdbc.class), sameInstance(context.getBean("userDao", UserDaoJdbc.class)));
		context.close();
	}
} 
