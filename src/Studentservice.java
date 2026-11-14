import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Studentservice {
    static void addStudent(Connection con, Scanner sc) throws SQLException {
        System.out.println("enter ur roll.no:");
        int rollno = sc.nextInt();
        sc.nextLine();
        System.out.println("enter ur name:");
        String name = sc.nextLine();
        System.out.println("enter ur class:");
        String clas = sc.nextLine();

        String sql = "INSERT INTO student (roll_no, student_name, class) VALUES (?, ?, ?)";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, rollno);
        ps.setString(2, name);
        ps.setString(3, clas);
        try {
            ps.executeUpdate();
            System.out.println("student inserted");
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Roll number " + rollno + " already exists");
        }
    }
}