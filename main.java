import java.sql.*;
import java.util.Scanner;

import static java.lang.System.exit;

public class main
{
    public static void main(String[] args) {
        String url="jdbc:mysql://localhost:3306/emp_db";
        String username="root";
        String password="";
        try {
            Connection conn = DriverManager.getConnection(url,username,password);
            Scanner in = new Scanner(System.in);

            while(true) {
                System.out.println("Select the option for a operation to be performed");
                System.out.println("1.View all employee data\n"+
                        "2.Add employee to the list\n"+
                        "3.Update employee details\n"+
                        "4.Delete employee\n"+
                        "5.Exit");
                int ch = in.nextInt();

                switch(ch)
                {
                    case 1 :
                        view(conn);
                        break;
                    case 2 :
                        add(conn, in);
                        break;
                    case 3 :
                        update(conn, in);
                        break;
                    case 4 :
                        delete(conn, in);
                        break;
                    case 5 :
                        exit(0);
                    default :
                        System.out.println("Wrong choice entered");
                        exit(0);
                }
            }
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    private static void view(Connection conn) throws SQLException {
        String sql = "select * from employee";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("empID");
                String name = rs.getString("name");
                String designation = rs.getString("designation");
                int experience = rs.getInt("experience");
                int age = rs.getInt("age");
                System.out.println("ID: " + id + ", Name: " + name + ", Designation: " + designation + " , Experience: " + experience + ", Age: " + age);
            }
        }
    }

    private static void add(Connection conn, Scanner in) throws SQLException {
        System.out.print("Enter employee ID: ");
        int empID = in.nextInt();
        System.out.print("Enter employee name: ");
        String name = in.next();
        System.out.print("Enter employee designation: ");
        String designation = in.next();
        System.out.print("Enter employee experience: ");
        int experience = in.nextInt();
        System.out.print("Enter employee age: ");
        int age = in.nextInt();

        String sql = "INSERT INTO employee (empID, name, designation, experience, age) VALUES (?, ?, ?, ?,?)";
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, empID);
            pst.setString(2, name);
            pst.setString(3, designation);
            pst.setInt(4, experience);
            pst.setInt(5, age);
            int rowsInserted = pst.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Employee added successfully.");
            } else {
                System.out.println("Failed to add employee.");
            }
        }
    }

    private static void update(Connection conn, Scanner in) throws SQLException {
        System.out.print("Enter employee ID to update: ");
        int empID = in.nextInt();

        String sqlCheck = "SELECT * FROM employee WHERE empID = ?";
        String sqlUpdate = "UPDATE employee SET name = ?, designation = ?, experience = ?, age = ? WHERE empID = ?";
        try (PreparedStatement pstCheck = conn.prepareStatement(sqlCheck);
             PreparedStatement pstUpdate = conn.prepareStatement(sqlUpdate)) {

            pstCheck.setInt(1, empID);
            ResultSet rs = pstCheck.executeQuery();
            if (!rs.next()) {
                System.out.println("Employee with ID " + empID + " not found.");
                return;
            }

            System.out.print("Enter new name: ");
            String name = in.next();
            System.out.print("Enter new designation: ");
            String designation = in.next();
            System.out.print("Enter new experience: ");
            int experience = in.nextInt();
            System.out.print("Enter new age: ");
            int age = in.nextInt();

            pstUpdate.setString(1, name);
            pstUpdate.setString(2, designation);
            pstUpdate.setInt(3, experience);
            pstUpdate.setInt(4, age);
            pstUpdate.setInt(5, empID);

            int rowsUpdated = pstUpdate.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Employee details updated successfully");
            } else {
                System.out.println("Failed to update employee details");
            }
        }
    }

    private static void delete(Connection conn, Scanner in) throws SQLException {
        System.out.print("Enter employee ID to delete: ");
        int empID = in.nextInt();

        String sqlCheck = "SELECT * FROM employee WHERE empID = ?";
        String sqlDelete = "DELETE FROM employee WHERE empID = ?";
        try (PreparedStatement pstCheck = conn.prepareStatement(sqlCheck);
             PreparedStatement pstDelete = conn.prepareStatement(sqlDelete)) {

            pstCheck.setInt(1, empID);
            ResultSet rs = pstCheck.executeQuery();
            if (!rs.next()) {
                System.out.println("Employee with ID " + empID + " not found.");
                return;
            }

            pstDelete.setInt(1, empID);
            int rowsDeleted = pstDelete.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Employee deleted successfully");
            } else {
                System.out.println("Failed to delete employee");
            }
        }
    }


}
