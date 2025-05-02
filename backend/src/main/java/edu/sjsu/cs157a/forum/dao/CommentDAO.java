package edu.sjsu.cs157a.forum.dao;

import edu.sjsu.cs157a.forum.model.Element;

import java.sql.SQLException;

public class CommentDAO extends BaseDAO{
    public Element createSelfReferencingComment(String text, int userId, int postId) throws SQLException {
        Element created = createTuple(new Element(
                "comments", "CommentID", null,
                new String[]{"CommentText", "UserID", "PostID", "ParentID"},
                new Object[]{text, userId, postId, 0} // placeholder
        ));

        Object id = created.getPrimaryKeyValue();
        updateTuple(new Element(
                "comments", "CommentID", id,
                new String[]{"ParentID"},
                new Object[]{id}
        ));

        return created;
    }

}
