package springbook.learningtest.spring.oxm;

import java.io.IOException;
import java.util.List;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.XmlMappingException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import springbook.user.sqlservice.jaxb.SqlType;
import springbook.user.sqlservice.jaxb.Sqlmap;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/OxmTest-context.xml")
public class OxmTest {

    @Autowired Unmarshaller unmarshaller;

    @Test
    public void unmarshallSqlMap() throws XmlMappingException, IOException {
        Source xmlSource = new StreamSource(getClass().getResourceAsStream("/sqlmap.xml"));
        Sqlmap sqlmap = (Sqlmap) unmarshaller.unmarshal(xmlSource);

        List<SqlType> sqlList = sqlmap.getSql();
        assertThat(sqlList.size(), is(6));
        assertThat(sqlList.get(0).getKey(), is("add"));
        assertThat(sqlList.get(0).getValue(), is("insert into users(id, name, password, email, level, login, recommend) values(?,?,?,?,?,?,?)"));
        assertThat(sqlList.get(1).getKey(), is("get"));
        assertThat(sqlList.get(1).getValue(), is("select * from users where id = ?"));
        assertThat(sqlList.get(2).getKey(), is("getAll"));
        assertThat(sqlList.get(2).getValue(), is("select * from users order by id"));
        assertThat(sqlList.get(3).getKey(), is("deleteAll"));
        assertThat(sqlList.get(3).getValue(), is("delete from users"));
        assertThat(sqlList.get(4).getKey(), is("getCount"));
        assertThat(sqlList.get(4).getValue(), is("select count(*) from users"));
        assertThat(sqlList.get(5).getKey(), is("userUpdate"));
        assertThat(sqlList.get(5).getValue(), is("update users set name = ?, password = ?, email = ?, level = ?, login = ?, recommend = ? where id = ?"));
    }

}
