package edu.sjsu.cs157a.forum.dao;

import org.junit.Test;
import edu.sjsu.cs157a.forum.dao.BaseDAO;
import java.util.List;
import java.util.Map;
import java.sql.SQLException;
import static org.junit.Assert.*;
import edu.sjsu.cs157a.forum.model.Element;
import java.util.Arrays;

public class BaseDAOTest {

    private BaseDAO dao = new BaseDAO();

    @Test
    public void test_getTable_returnsData() throws SQLException {
        List<Map<String, Object>> users = dao.getTable("Users", new String[]{"UserID"});
        assertNotNull(users);
        assertFalse(users.isEmpty());
    }

    @Test
    public void test_getTable_withLimit() throws SQLException {
        List<Map<String, Object>> limited = dao.getTable("Users", new String[]{"UserID"}, 1);
        assertNotNull(limited);
        assertTrue(limited.size() <= 1);
    }

    @Test
    public void test_getTableWithCondition_nullCondition() throws SQLException {
        List<Map<String, Object>> all = dao.getTableWithCondition("Users", new String[]{"UserID"}, null, null);
        assertNotNull(all);
        assertFalse(all.isEmpty());
    }

    @Test
    public void test_getTableWithCondition_withCondition() throws SQLException {
        List<Map<String, Object>> result = dao.getTableWithCondition("Users", new String[]{"UserID","Username"}, "Username = ?", new Object[]{"flower_girl"});
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("flower_girl", result.get(0).get("Username"));
    }

    @Test
    public void test_createUpdateAndDeleteTuple() throws SQLException {
        String username = "temp_e_" + System.currentTimeMillis();
        String email = username + "@ex.com";
        Element el = new Element("Users", "UserID", null,
            new String[]{"Username","Password","Email"},
            new Object[]{username, "pass", email});
        Element created = dao.createTuple(el);
        assertNotNull(created.getPrimaryKeyValue());
        created.setColumnValue("Email", "upd_" + email);
        Element updated = dao.updateTuple(created);
        assertEquals("upd_" + email, updated.getColumnValue("Email"));
        boolean deleted = dao.deleteTuple(created);
        assertTrue(deleted);
    }

    @Test
    public void test_createTuplesBatch() throws SQLException {
        String prefix = "batch_e_" + System.currentTimeMillis();
        Element user1 = new Element("Users", "UserID", null,
            new String[]{"Username","Password","Email"},
            new Object[]{prefix + "1", "p1", prefix + "1@ex.com"});
        Element user2 = new Element("Users", "UserID", null,
            new String[]{"Username","Password","Email"},
            new Object[]{prefix + "2", "p2", prefix + "2@ex.com"});
        List<Element> created = dao.createTuplesBatch(Arrays.asList(user1, user2));
        assertEquals(2, created.size());
        for (Element e : created) {
            assertNotNull(e.getPrimaryKeyValue());
            dao.deleteTuple(e);
        }
    }
} 