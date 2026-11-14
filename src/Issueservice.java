import java.sql.*;
import java.util.Scanner;

public class Issueservice {
    static void issueFlow(Connection con, Scanner sc) throws SQLException {
        System.out.println("enter roll number:");
        int issueRoll = sc.nextInt();
        sc.nextLine();
        int limit = 5;
        if (booksHeld(con, issueRoll) >= limit) {
            System.out.println("too many books");
        } else {
            System.out.println("enter book id:");
            int bookId = sc.nextInt();
            sc.nextLine();
            if (bookservice.bookOut(con, bookId) > 0) {
                System.out.println("book already issued");
            } else {
                issueBook(con, issueRoll, bookId);
            }
        }
    }
    static int booksHeld(Connection con, int rollNo) throws SQLException {
        String sql = "SELECT COUNT(*) FROM issue_record WHERE roll_no = ? AND return_date IS NULL";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, rollNo);
        ResultSet rs = ps.executeQuery();
        rs.next();
        return rs.getInt(1);
    }
    static void issueBook(Connection con, int rollNo, int bookId) throws SQLException {
        String sql = "INSERT INTO issue_record (roll_no, book_id, issue_date, due_date) "
                + "VALUES (?, ?, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 7 DAY))";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, rollNo);
        ps.setInt(2, bookId);
        try {
            ps.executeUpdate();
            System.out.println("book issued");
        }catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("cant add or update book");
        }

    }
    static void returnBook(Connection con, int bookId,int roll_no) throws SQLException {
        String sql="update issue_record set return_date = CURDATE() where book_id = ? and roll_no=? and return_date IS NULL";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, bookId);
        ps.setInt(2, roll_no);
        int late=daysleft(con,roll_no,bookId) ;
        int fine=5;
        int rows = ps.executeUpdate();
        if (rows > 0) {
            System.out.println("book returned from rollno " + roll_no +"and book_id "+ bookId);
            if(late>5){
                System.out.println("late by"+late+"days fine:"+late*fine);
            }
        }else {
            System.out.println("no book returned");
        }

    }
    static void returnFlow(Connection con, Scanner sc) throws SQLException {
        System.out.println("enter book id:");
        int book=sc.nextInt();
        sc.nextLine();
        System.out.println("enter roll number:");
        int roll=sc.nextInt();
        sc.nextLine();
        returnBook(con, book, roll);
    }
    static int daysleft(Connection con, int rollNo,int book_id) throws SQLException {
        String sql="Select datediff(curdate(),due_date)from issue_record where roll_no=? and book_id=? and return_date IS NULL";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, rollNo);
        ps.setInt(2, book_id);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt(1);
        }
        return 0;
    }
    static  void duetoday(Connection con, Scanner sc) throws SQLException {
        String sql="Select book.book_name ,issue_record.roll_no from issue_record join book on issue_record.book_id=book.book_id where issue_record.due_date=CURDATE() and issue_record.return_date IS NULL";
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        boolean found=false;
        while (rs.next()) {
            System.out.println(rs.getString("book_name")+"-roll number "+rs.getInt("roll_no"));
            found=true;
        }
        if (!found) {
            System.out.println("no book due today");
        }
    }
    static void overduereport(Connection con) throws SQLException {
        String sql = "select book.book_name, issue_record.roll_no, "
                + "DATEDIFF(curdate(), issue_record.due_date) as days_overdue "
                + "from issue_record join book on issue_record.book_id = book.book_id "
                + "where issue_record.due_date < CURDATE() "
                + "and issue_record.return_date IS NULL";
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        boolean found=false;
        while (rs.next()) {
            System.out.println(rs.getString("book_name")+"-roll number "+rs.getInt("roll_no")+
                    "-"+rs.getInt("days_overdue")+"-days overdue");
            found=true;
        }
        if (!found) {
            System.out.println("no book due today");
        }
    }
}
