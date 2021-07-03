import CSVProcesser.CSVProcessor;
import CourseDao.CourseDao;
import CourseDao.CourseDaoImpl;
import model.Course;

import java.util.List;

public class CLI {
    public static void main(String[] args) {
        CSVProcessor csvProcessor = new CSVProcessor("./courses.csv");
        CourseDao courseDao = new CourseDaoImpl();
        List<Course> res = csvProcessor.getAllCourseFromCSV();
//        for (Course course : res) {
//            courseDao.upLoadCourse(course);
//        }
    }
}
