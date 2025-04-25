import java.sql.*;

public class MyJDBC {
    public static String gStr(ResultSet rs, String s){
        try {
            return rs.getString(s);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static void main(String[] args) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1:3306/sf_db",
                    "root",
                    "1234"
            );
            Statement statement = connection.createStatement();
            System.out.println("USERS");
            ResultSet rs = statement.executeQuery("SELECT * FROM users");
            while(rs.next()){
                System.out.println(gStr(rs, "UserID") + ", " + gStr(rs, "Username") + ", " +
                        gStr(rs, "Password") + ", " + gStr(rs, "Email") + ", " + gStr(rs, "RegisterDate"));
            }
            System.out.println("SUBFORUMS");
            Statement statement2 = connection.createStatement();
            rs = statement2.executeQuery("SELECT * FROM subforums");
            while(rs.next()){
                System.out.println(gStr(rs, "SubforumID") + ", " + gStr(rs, "Name") + ", " +
                        gStr(rs, "Description") + ", " + gStr(rs, "CreationDate") + ", " +
                        gStr(rs, "SubscriberCount") + ", " + gStr(rs, "LastUpdated"));
            }
            System.out.println("POSTS");
            Statement statement3 = connection.createStatement();
            rs = statement3.executeQuery("SELECT * FROM posts");
            while(rs.next()){
                System.out.println(gStr(rs, "PostID") + ", " + gStr(rs, "Title") + ", " + gStr(rs, "BodyText")
                        + ", " + gStr(rs, "CreationDate") + ", " + gStr(rs, "Rating") + ", " +
                        gStr(rs, "UserID") + ", " + gStr(rs, "SubforumID"));
            }
            System.out.println("COMMENTS");
            Statement statement4 = connection.createStatement();
            rs = statement4.executeQuery("SELECT * FROM comments");
            while(rs.next()){
                System.out.println(gStr(rs, "CommentID") + ", " + gStr(rs, "CommentText") + ", " +
                        gStr(rs, "CreationDate") + ", " + gStr(rs, "Rating") + ", " + gStr(rs, "UserID")+ ", "
                        + gStr(rs, "PostID") + ", " + gStr(rs, "ParentID") + ", " + gStr(rs, "LastUpdated"));
            }
            System.out.println("SUBSCRIPTIONS");
            Statement statement5 = connection.createStatement();
            rs = statement5.executeQuery("SELECT * FROM subscriptions");
            while(rs.next()){
                System.out.println(gStr(rs, "UserID") + ", " + gStr(rs, "SubforumID") + ", " +
                        gStr(rs, "SubscriptionDate"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
