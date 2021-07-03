package model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Course {
    private  String courseCode;
    private  String courseName;
    private  String creditType;
    private  int credits;

    //optional
    @Builder.Default
    private  int maxCredit = -1;
    private  String description;
    private  String myplanLink;
}
