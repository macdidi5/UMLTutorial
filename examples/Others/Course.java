import java.util.*;

public class Course {
  private String name;
  private Teacher teacher;
  private Set students;

  public String getName() {
    return name;
  }

  public Teacher getTeacher() {
    return teacher;
  }

  public Set getStudents() {
    return students;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setTeacher(Teacher teacher) {
    this.teacher = teacher;
  }

  public void setStudents(Set students) {
    this.students = students;
  }
}
