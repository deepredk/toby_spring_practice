package springbook.user;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springbook.user.service.UserServiceTest.TestUserService;
import springbook.user.sqlservice.EnableSqlService;

import com.mysql.jdbc.Driver;

import springbook.user.dao.UserDao;
import springbook.user.service.DummyMailSender;
import springbook.user.service.UserService;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = "springbook.user")
@EnableSqlService
@PropertySource("/database.properties")
public class AppContext implements SqlMapConfig {

    @Autowired
    Environment env;

    @Bean
    @SuppressWarnings("unchecked")
    public DataSource dataSource() {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();

        try {
            dataSource.setDriverClass((Class<? extends Driver>) Class.forName(env.getProperty("db.driverClass")));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        dataSource.setUrl(env.getProperty("db.url"));
        dataSource.setUsername(env.getProperty("db.username"));
        dataSource.setPassword(env.getProperty("db.password"));

        return dataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        DataSourceTransactionManager tm = new DataSourceTransactionManager();
        tm.setDataSource(dataSource());
        return tm;
    }

    @Override
    public Resource getSqlMapResource() {
        return new ClassPathResource("sqlmap.xml", UserDao.class);
    }


    @Configuration
    @Profile("production")
    public static class ProductionAppContext {
        
        @Bean
        public MailSender mailSender() {
            return new JavaMailSenderImpl();
        }

    }

    @Configuration
    @Profile("test")
    public static class TestAppContext {

        @Autowired UserDao userDao;

        @Bean
        public UserService testUserService() {
            return new TestUserService();
        }

        @Bean
        public MailSender mailSender() {
            return new DummyMailSender();
        }

    }

}
