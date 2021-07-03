package CourseDao;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import model.Course;

import java.sql.*;

public class CourseDaoImpl implements CourseDao {
    private final String INSERTCOURSE = "INSERT INTO Courses(courseName, creditType, description, myplanLink, courseFullName) VALUES(?, ?, ?, ?, ?)";
    private final String INSERTCREDIT = "INSERT INTO CourseCredit VALUES(?, ?)";
    private final ComboPooledDataSource cpds;
    public CourseDaoImpl() {
        this.cpds = new ComboPooledDataSource();
        try {
            cpds.setJdbcUrl("jdbc:mysql://courseradartest.cp1kkn1uksyy.us-east-2.rds.amazonaws.com:3306/CourseRadar?user=admin&password=UWcm20151001");
            cpds.setMinPoolSize(5);
            cpds.setAcquireIncrement(5);
            cpds.setMaxPoolSize(20);
            Connection temp = cpds.getConnection();


            temp.close();
        } catch (Exception e) {
            System.out.println("Error while constructing combo source");
            e.printStackTrace();
        }
    }

    public boolean uploadCredit(int courseId, int credit) {
        PreparedStatement insertCredit;
        Connection conn;
        try {
            conn = cpds.getConnection();
            insertCredit = conn.prepareStatement(this.INSERTCREDIT);

            insertCredit.clearParameters();
            insertCredit.setInt(1, courseId);
            insertCredit.setInt(2, credit);
            insertCredit.executeUpdate();

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean upLoadCourse(Course toUpload) {
        PreparedStatement insertCourse;
        Connection conn;
        try {
            conn = cpds.getConnection();
            insertCourse = conn.prepareStatement(this.INSERTCOURSE, Statement.RETURN_GENERATED_KEYS);

            insertCourse.clearParameters();
            insertCourse.setString(1, toUpload.getCourseCode());
            insertCourse.setString(2, toUpload.getCreditType());
            insertCourse.setString(3, toUpload.getDescription());
            insertCourse.setString(4, toUpload.getMyplanLink());
            insertCourse.setString(5, toUpload.getCourseName());

            insertCourse.executeUpdate();

            ResultSet generatedKeys = insertCourse.getGeneratedKeys();
            generatedKeys.next();
            uploadCredit(generatedKeys.getInt(1), toUpload.getCredits());
            generatedKeys.close();
            conn.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }



    }
}
