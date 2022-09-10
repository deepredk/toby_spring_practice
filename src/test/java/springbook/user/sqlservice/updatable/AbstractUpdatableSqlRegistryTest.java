package springbook.user.sqlservice.updatable;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public abstract class AbstractUpdatableSqlRegistryTest {
    
    UpdatableSqlRegistry sqlRegistry;

    @Before
    public void setUp() {
        sqlRegistry = createUpdatableSqlRegistry();
        sqlRegistry.registerSql("KEY1", "SQL1");
        sqlRegistry.registerSql("KEY2", "SQL2");
        sqlRegistry.registerSql("KEY3", "SQL3");
    }

    abstract protected UpdatableSqlRegistry createUpdatableSqlRegistry();

    protected void checkFind(String expected1, String expected2, String expected3) {
        assertEquals(sqlRegistry.findSql("KEY1"), expected1);
        assertEquals(sqlRegistry.findSql("KEY2"), expected2);
        assertEquals(sqlRegistry.findSql("KEY3"), expected3);
    }

    @Test
    public void find() {
        checkFind("SQL1", "SQL2", "SQL3");
    }

    @Test
    public void updateSingle() {
        sqlRegistry.updateSql("KEY2", "Modified2");
        checkFind("SQL1", "Modified2", "SQL3");
    }

    @Test
    public void updateMulti() {
        sqlRegistry.updateSql("KEY1", "Modified1");
        sqlRegistry.updateSql("KEY3", "Modified3");
        checkFind("Modified1", "SQL2", "Modified3");
    }

    @Test(expected=SqlUpdateFailureException.class)
    public void updateWithNotExistingKey() {
        sqlRegistry.updateSql("SQL9999!@#$", "Modified2");
    }

    @Test(expected=SqlUpdateFailureException.class)
    public void unknownKey() {
        sqlRegistry.updateSql("SQL9999!@#$", "Modified2");
    }

}
