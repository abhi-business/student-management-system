import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Main extends Application {
    private StudentManager manager = new StudentManager();
    private ObservableList<Student> studentList;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Student Management System");

        // Table View
        TableView<Student> table = new TableView<>();
        TableColumn<Student, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getId()));
        TableColumn<Student, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));
        TableColumn<Student, Double> marksColumn = new TableColumn<>("Marks");
        marksColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getMarks()));

        table.getColumns().addAll(idColumn, nameColumn, marksColumn);

        studentList = FXCollections.observableArrayList(manager.getStudents());
        table.setItems(studentList);

        // Input Fields
        TextField idField = new TextField();
        idField.setPromptText("ID");
        TextField nameField = new TextField();
        nameField.setPromptText("Name");
        TextField marksField = new TextField();
        marksField.setPromptText("Marks");

        // Buttons
        Button addButton = new Button("Add");
        addButton.setOnAction(e -> {
            int id = Integer.parseInt(idField.getText());
            String name = nameField.getText();
            double marks = Double.parseDouble(marksField.getText());
            Student student = new Student(id, name, marks);
            manager.addStudent(student);
            studentList.add(student);
            idField.clear();
            nameField.clear();
            marksField.clear();
        });

        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(e -> {
            Student selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                manager.deleteStudent(selected);
                studentList.remove(selected);
            }
        });

        Button analyzeButton = new Button("Analyze");
        analyzeButton.setOnAction(e -> {
            Task<Void> task = new Task<>() {
                @Override
                protected Void call() {
                    double average = studentList.stream()
                            .mapToDouble(Student::getMarks)
                            .average()
                            .orElse(0);
                    System.out.println("Average Marks: " + average);
                    return null;
                }
            };
            new Thread(task).start();
        });

        // Layout
        VBox inputBox = new VBox(5, idField, nameField, marksField, addButton, deleteButton, analyzeButton);
        BorderPane root = new BorderPane();
        root.setCenter(table);
        root.setRight(inputBox);

        Scene scene = new Scene(root, 800, 400);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Save data on close
        primaryStage.setOnCloseRequest(e -> manager.saveData());
    }
}
