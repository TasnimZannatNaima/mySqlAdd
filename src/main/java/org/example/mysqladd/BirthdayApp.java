package org.example.mysqladd;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class BirthdayApp extends Application {

    private BirthdayDAO dao = new BirthdayDAO();
    private ListView<String> listView = new ListView<>();
    private TextField idField = new TextField();
    private TextField nameField = new TextField();
    private DatePicker birthdatePicker = new DatePicker();
    private TextField searchField = new TextField();
    private List<Birthday> currentList;

    @Override
    public void start(Stage stage) {
        VBox root = new VBox(10);
        root.setPadding(new Insets(15));

        Label idLabel = new Label("আইডি:");
        Label nameLabel = new Label("নাম:");
        Label dateLabel = new Label("জন্মতারিখ:");

        idField.setEditable(false); // আইডি পরিবর্তনযোগ্য নয়

        Button addBtn = new Button("যোগ করুন");
        Button updateBtn = new Button("আপডেট করুন");
        Button deleteBtn = new Button("মুছুন");
        Button searchBtn = new Button("অনুসন্ধান");
        Button refreshBtn = new Button("সব দেখান");

        HBox inputBox = new HBox(10, idLabel, idField, nameLabel, nameField, dateLabel, birthdatePicker, addBtn, updateBtn, deleteBtn);
        inputBox.setAlignment(Pos.CENTER_LEFT);

        HBox searchBox = new HBox(10, new Label("নাম/মাস:"), searchField, searchBtn, refreshBtn);
        searchBox.setAlignment(Pos.CENTER_LEFT);

        VBox layout = new VBox(15, inputBox, searchBox, listView);
        root.getChildren().add(layout);

        addBtn.setOnAction(e -> handleAdd());
        updateBtn.setOnAction(e -> handleUpdate());
        deleteBtn.setOnAction(e -> handleDelete());
        searchBtn.setOnAction(e -> handleSearch());
        refreshBtn.setOnAction(e -> loadAll());

        listView.setOnMouseClicked(e -> populateFieldsFromSelection());

        loadAll();
        showTodaysBirthdays();

        Scene scene = new Scene(root, 950, 450);
        stage.setTitle("🎂 জন্মদিন ব্যবস্থাপনা অ্যাপ");
        stage.setScene(scene);
        stage.show();
    }

    private void handleAdd() {
        try {
            String name = nameField.getText().trim();
            LocalDate date = birthdatePicker.getValue();
            if (name.isEmpty() || date == null) {
                showAlert("ত্রুটি", "নাম এবং জন্মতারিখ দিন।");
                return;
            }

            Birthday b = new Birthday();
            b.setName(name);
            b.setBirthdate(date);
            dao.addBirthday(b);
            loadAll();
            clearFields();
        } catch (SQLException ex) {
            showAlert("ডাটাবেস ত্রুটি", ex.getMessage());
        }
    }

    private void handleUpdate() {
        String selected = listView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("নির্বাচন", "আপডেট করার জন্য একটি আইটেম নির্বাচন করুন।");
            return;
        }

        try {
            int index = listView.getSelectionModel().getSelectedIndex();
            Birthday b = currentList.get(index);
            b.setName(nameField.getText());
            b.setBirthdate(birthdatePicker.getValue());
            dao.updateBirthday(b);
            loadAll();
            clearFields();
        } catch (SQLException ex) {
            showAlert("ডাটাবেস ত্রুটি", ex.getMessage());
        }
    }

    private void handleDelete() {
        String selected = listView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("নির্বাচন", "মুছার জন্য একটি আইটেম নির্বাচন করুন।");
            return;
        }

        try {
            int index = listView.getSelectionModel().getSelectedIndex();
            int id = currentList.get(index).getId();
            dao.deleteBirthday(id);
            loadAll();
            clearFields();
        } catch (SQLException ex) {
            showAlert("ডাটাবেস ত্রুটি", ex.getMessage());
        }
    }

    private void handleSearch() {
        try {
            String keyword = searchField.getText();
            List<Birthday> results = dao.searchByNameOrMonth(keyword);
            populateListView(results);
        } catch (SQLException ex) {
            showAlert("অনুসন্ধান ত্রুটি", ex.getMessage());
        }
    }

    private void loadAll() {
        try {
            List<Birthday> list = dao.getAllBirthdays();
            populateListView(list);
        } catch (SQLException ex) {
            showAlert("লোড ত্রুটি", ex.getMessage());
        }
    }

    private void populateListView(List<Birthday> list) {
        listView.getItems().clear();
        currentList = list;
        for (Birthday b : list) {
            listView.getItems().add("আইডি: " + b.getId() + " | নাম: " + b.getName() + " | জন্মতারিখ: " + b.getBirthdate());
        }
    }

    private void populateFieldsFromSelection() {
        int index = listView.getSelectionModel().getSelectedIndex();
        if (index >= 0 && index < currentList.size()) {
            Birthday b = currentList.get(index);
            idField.setText(String.valueOf(b.getId()));
            nameField.setText(b.getName());
            birthdatePicker.setValue(b.getBirthdate());
        }
    }

    private void clearFields() {
        idField.clear();
        nameField.clear();
        birthdatePicker.setValue(null);
    }

    private void showTodaysBirthdays() {
        try {
            List<Birthday> list = dao.getTodaysBirthdays();
            for (Birthday b : list) {
                showAlert("🎉 শুভ জন্মদিন!", b.getName() + " এর আজ জন্মদিন! 🎂");
            }
        } catch (SQLException ex) {
            showAlert("ত্রুটি", ex.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
