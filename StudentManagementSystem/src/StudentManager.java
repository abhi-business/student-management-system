import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class StudentManager {
    private List<Student> students;

    public StudentManager() {
        students = new ArrayList<>();
        loadData();
    }

    public List<Student> getStudents() {
        return students;
    }

    public void addStudent(Student student) {
        students.add(student);
    }

    public void deleteStudent(Student student) {
        students.remove(student);
    }

    public void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("students.dat"))) {
            oos.writeObject(students);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadData() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("students.dat"))) {
            students = (List<Student>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("No saved data found.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
