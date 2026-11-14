import java.sql.*;
import java.util.Scanner;

public class Shopcon {
    public static void main(String[] args) throws SQLException {
       Connection con=Database.connect();
        Scanner sc=new Scanner(System.in);
        boolean running = true;
        while (running) {
            System.out.println("1. Add student");
            System.out.println("2. Add book");
            System.out.println("3. Issue book");
            System.out.println("4.Return book");
            System.out.println("5. searchbook");
            System.out.println("6. DUETODAY");
            System.out.println("7.overduereport");
            System.out.println("8. EXIT");
            System.out.println("Enter your choice");
            int choice = sc.nextInt();
            sc.nextLine();
            switch (choice) {
                case 1:
                    Studentservice.addStudent(con, sc);
                    break;
                case 2:
                    Bookservice.addBook(con, sc);
                    break;
                case 3:
                    Issueservice.issueFlow(con, sc);
                    break;
                case 4:
                    Issueservice.returnFlow(con,sc);
                    break;
                case 5:
                    Bookservice.search(con, sc);
                    break;
                case 6:
                    Issueservice.duetoday(con, sc);
                    break;
                case 7:
                    Issueservice.overduereport(con);
                    break;
                case 8:
                    running = false;
                    break;
                default:
                    System.out.println("invalid choice");
            }
        }
        con.close();
    }
}