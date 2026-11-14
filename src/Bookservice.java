import java.sql.*;
import java.util.Scanner;

public class Bookservice {
    static void addBook(Connection con, Scanner sc) throws SQLException {
        System.out.println("enter book id:");
        int bookid = sc.nextInt();
        sc.nextLine();
        System.out.println("enter book name:");
        String bookname = sc.nextLine();
        String sql = "INSERT INTO book (book_id, book_name) VALUES (?, ?)";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, bookid);
        ps.setString(2, bookname);
        try {
            ps.executeUpdate();
            System.out.println("book inserted");
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("book_id " + bookid + " already exists");
        }
    }
    static int bookOut(Connection con, int book_id) throws SQLException {
        String sql = "select count(*) from issue_record where book_id = ? and return_date IS NULL";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, book_id);
        ResultSet rs = ps.executeQuery();
        rs.next();
        return rs.getInt(1);
    }
    static  void search(Connection con, Scanner sc) throws SQLException {
        System.out.println("enter the book name:");
        String bookname=sc.nextLine();
        String sql="select book_id, book_name from book where book_name like ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1,"%"+bookname+"%");
        ResultSet rs = ps.executeQuery();
        boolean found=false;
        while (rs.next()) {
            int id=rs.getInt("book_id");
            String name=rs.getString("book_name");
            String status;
            if(bookOut(con,id) > 0) {
                status="book already issued";
            }else {
                status="book available";
            }
            System.out.println("book id "+id+" book name "+name+"-"+status);
            found=true;
        }
        if (!found) {
            System.out.println("no book found");
        }
    }
}