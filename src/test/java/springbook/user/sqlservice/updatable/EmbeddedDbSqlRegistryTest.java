package springbook.user.sqlservice.updatable;

import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;

import static org.junit.Assert.fail;
import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.HSQL;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Test;

public class EmbeddedDbSqlRegistryTest extends AbstractUpdatableSqlRegistryTest {
    
    EmbeddedDatabase db;

    protected UpdatableSqlRegistry createUpdatableSqlRegistry() {
        db = new EmbeddedDatabaseBuilder()
                .setType(HSQL)
                .addScript("classpath:springbook/user/sqlservice/updatable/schema.sql")
                .build();

        EmbeddedDbSqlRegistry embeddedDbSqlRegistry = new EmbeddedDbSqlRegistry();
        embeddedDbSqlRegistry.setDataSource(db);
        return embeddedDbSqlRegistry;
    }

    @After
    public void tearDown() {
        db.shutdown();
    }

    @Test
    public void transactionalUpdate() {
        checkFind("SQL1", "SQL2", "SQL3");
        
        Map<String, String> sqlmap = new HashMap<>();
        sqlmap.put("KEY1", "Modified1");
        sqlmap.put("KEY9999!@#$", "Modified9999");

        try {
            sqlRegistry.updateSql(sqlmap);
            fail();
        } catch (SqlUpdateFailureException e) { }

        checkFind("SQL1", "SQL2", "SQL3");
    }

}
