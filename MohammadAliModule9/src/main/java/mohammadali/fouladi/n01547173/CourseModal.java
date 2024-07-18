package mohammadali.fouladi.n01547173;

public class CourseModal {
// MohammadAli Fouladi N01547173
    // variables for our course
    // name and description.
    private String courseName;
    private String courseDescription;

    // creating constructor for our variables.
    public CourseModal(String courseName, String courseDescription) {
        this.courseName = courseName;
        this.courseDescription = courseDescription;
    }

    // creating getter and setter methods.
    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }
}
