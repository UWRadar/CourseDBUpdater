package CSVProcesser;

import com.google.common.collect.ImmutableList;
import lombok.extern.log4j.Log4j2;
import model.Course;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public class CSVProcessor {
    private BufferedReader csvReader;
    private final int COL_DEPARTMENT = 0;
    private final int COL_COURSENUMBER = 1;
    private final int COL_COURSETITLE = 2;
    private final int COL_MINCREDIT = 3;
    private final int COL_MAXCREDIT = 4;
    private final int COL_CREDITTYPE = 5;
    private final int COL_DESCRIPTION = 6;
    private final String MYPLANPREFIX = "https://myplan.uw.edu/course/#/courses/";

    public CSVProcessor() {
        this.csvReader = null;
    }

    public CSVProcessor(String filename) {
        try{
            this.csvReader = new BufferedReader(new FileReader(filename));
        } catch (FileNotFoundException e) {
            // File Not Found
            e.printStackTrace();
        }
    }

    public List<Course> getAllCourseFromCSV() {
        List<Course> courseResult = new ArrayList<>();
        try {
            // Skips the first line
            String row = this.csvReader.readLine();
            while (row != null) {
                row = this.csvReader.readLine();
                Course course = convertLineToCourse(row);
                if (course != null) {
                    courseResult.add(course);
                }
//                if (count == 10) {
//                    break;
//                }
//                count++;
            }
        }
        catch (IOException e) {
            log.error(e.getStackTrace());
            return ImmutableList.of();
        }

        return courseResult;
    }

    public Course convertLineToCourse(String line) {

        Course output = Course.builder().build();
        try {
            // escape sequence to match the pipe character
            String[] columns = line.split("\\|");



            String courseCode = columns[COL_DEPARTMENT] + columns[COL_COURSENUMBER];
            output.setCourseCode(courseCode.toLowerCase());
            output.setCourseName(columns[COL_COURSETITLE]);
            output.setMyplanLink(MYPLANPREFIX + columns[COL_DEPARTMENT] + " "
                    + columns[COL_COURSENUMBER]);
            output.setCredits(Integer.parseInt(columns[COL_MINCREDIT]));

            if (!columns[COL_MINCREDIT].equals(columns[COL_MAXCREDIT]) && !columns[COL_MAXCREDIT].equals("")) {
                output.setMaxCredit(Integer.parseInt(columns[COL_MAXCREDIT]));
            }


            String creditType = changeCreditTypeDelimiter(columns[COL_CREDITTYPE]);
            output.setCreditType(creditType);

            if (columns.length > COL_DESCRIPTION) {
                output.setDescription(columns[COL_DESCRIPTION]);
            }
        }
        catch (Exception e) {
            return null;
        }
        System.out.println(output);
        return output;
    }


    public String changeCreditTypeDelimiter(String creditTypes) {
        String result = "";
        creditTypes = creditTypes.substring(1, creditTypes.length() - 1);
        String[] types = creditTypes.split(", ");
        if (types.length == 0) {
            return "None";
        }

        result = types[0].replace("'", "");

        for (int i = 1; i < types.length; i++) {
            result += "/" + types[i].replace("'", "");
        }
        if (result.equals(""))
            return "None";
        return result;
    }

    private String emitBracket(String target) {
        if (target.charAt(0) == '[') {
            return target.substring(1);
        } else if  (target.charAt(target.length() - 1) == ']') {
            return target.substring(0, target.length() - 1);
        } else {
            return target;
        }
    }

}
