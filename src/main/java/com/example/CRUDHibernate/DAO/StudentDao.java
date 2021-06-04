package com.example.CRUDHibernate.DAO;

import com.example.CRUDHibernate.StudentModel.Student;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class StudentDao {

    private String jdbcURL="jdbc:mysql://localhost:3306/webjavaserver" ;
    private String jdbcUsername="root";
    private String jdbcPassword="pauline@2019";
    private Connection jdbcConnection;
    public StudentDao(String jdbcURL, String jdbcUsername, String jdbcPassword) {
        this.jdbcURL = jdbcURL;
        this.jdbcUsername = jdbcUsername;
        this.jdbcPassword = jdbcPassword;
    }
    protected void connect() throws SQLException {
        if (jdbcConnection == null || jdbcConnection.isClosed()) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                throw new SQLException(e);
            }
            jdbcConnection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
        }
    }
    protected void disconnect() throws SQLException {
        if (jdbcConnection != null && !jdbcConnection.isClosed()) {
            jdbcConnection.close();
        }
    }
    public boolean insertStudent(Student student) throws SQLException {
        String sql = "INSERT INTO student (first_name, last_name, gender) VALUES (?, ?, ?)";
        connect();
        PreparedStatement statement = jdbcConnection.prepareStatement(sql);
        statement.setString(1, student.getFirstName());
        statement.setString(2, student.getLastName());
        statement.setString(3, student.getGender());
        boolean rowInserted = statement.executeUpdate() > 0;
        statement.close();
        disconnect();
        return rowInserted;
    }
    public List<Student> listAllStudents() throws SQLException {
        List<Student> listStudent = new ArrayList<Student>();
        String sql = "SELECT * FROM student";
        connect();
        Statement statement = jdbcConnection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            Long id = resultSet.getLong("id");
            String firstName = resultSet.getString("first_name");
            String lastName = resultSet.getString("last_name");
            String gender = resultSet.getString("gender");
            Student Student = new Student(id, firstName, lastName, gender);
            listStudent.add(Student);
        }
        resultSet.close();
        statement.close();
        disconnect();
        return listStudent;
    }
    public boolean deleteStudent(Student student) throws SQLException {
        String sql = "DELETE FROM student where id = ?";
        connect();
        PreparedStatement statement = jdbcConnection.prepareStatement(sql);
        statement.setInt(1, student.getId().intValue());
        boolean rowDeleted = statement.executeUpdate() > 0;
        statement.close();
        disconnect();
        return rowDeleted;
    }
    public boolean updateStudent(Student Student) throws SQLException {
        String sql = "UPDATE student SET first_name = ?, last_name = ?, gender = ?";
        sql += " WHERE id = ?";
        connect();
        PreparedStatement statement = jdbcConnection.prepareStatement(sql);
        statement.setString(1, Student.getFirstName());
        statement.setString(2, Student.getLastName());
        statement.setString(3, Student.getGender());
        statement.setLong(4, Student.getId());
        boolean rowUpdated = statement.executeUpdate() > 0;
        statement.close();
        disconnect();
        return rowUpdated;
    }
    public Student getStudent(Student sst) throws SQLException {
        Student student = null;
        String sql = "SELECT * FROM student WHERE id = ?";
        connect();
        int stdId = sst.getId().intValue();
        PreparedStatement statement = jdbcConnection.prepareStatement(sql);
        statement.setInt(1, stdId);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            String firstName = resultSet.getString("first_name");
            String lastName = resultSet.getString("last_name");
            String gender = resultSet.getString("gender");
            student = new Student(new Long(stdId), firstName, lastName, gender);
        }
        resultSet.close();
        statement.close();
        return student;
    }

    public Student view(int stdId) throws SQLException {
        Student vstudent = null;
        String sql = "SELECT * FROM student WHERE id = ?";
        connect();

        PreparedStatement stmt = jdbcConnection.prepareStatement(sql);
        stmt.setInt(1, stdId);
        ResultSet resultSet = stmt.executeQuery();
        if (resultSet.next()) {
            String firstName = resultSet.getString("first_name");
            String lastName = resultSet.getString("last_name");
            String gender = resultSet.getString("gender");
            vstudent = new Student(new Long(stdId), firstName, lastName, gender);
        }
        resultSet.close();
        stmt.close();
        return vstudent;
    }
}

