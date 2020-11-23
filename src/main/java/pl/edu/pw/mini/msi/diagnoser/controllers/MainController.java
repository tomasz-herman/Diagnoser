package pl.edu.pw.mini.msi.diagnoser.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import pl.edu.pw.mini.msi.diagnoser.models.Disease;
import pl.edu.pw.mini.msi.diagnoser.ai.KnowledgeBase;
import pl.edu.pw.mini.msi.diagnoser.models.Patient;
import pl.edu.pw.mini.msi.diagnoser.models.Symptom;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MainController {
    @FXML
    private TableView<Disease> diseasesTable;
    @FXML
    private TableView<Patient> patientsTable;
    @FXML
    private Button addPatientButton;
    @FXML
    private Button removePatientButton;
    private Stage stage;

    private KnowledgeBase knowledgeBase;

    @FXML
    private void initialize() {
        diseasesTable.getSelectionModel().setCellSelectionEnabled(true);
        patientsTable.getSelectionModel().setCellSelectionEnabled(true);
        initKnowledgeBase();
    }

    private void initKnowledgeBase() {
        knowledgeBase = new KnowledgeBase(diseasesTable, patientsTable);
        for (int i = 0; i < 10; i++) {
            knowledgeBase.addKnownDisease("Disease " + (char)('A' + i));
            knowledgeBase.addKnownSymptom("Symptom " + (char)('A' + i));
        }
    }

    @FXML
    private void onAddPatient(ActionEvent event) {
        knowledgeBase.addPatient(new Patient((new Random().nextBoolean() ? "Anna Wanna " : "Jan Dzban ") + new Random().nextInt(100)));
    }

    @FXML
    private void onRemovePatient(ActionEvent event) {
        Patient patient = patientsTable.getSelectionModel().getSelectedItem();
        if(patient != null){
            knowledgeBase.removePatient(patient);
        }
    }

    @FXML
    private void onLoadCsv(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(stage);
        if(file != null) {
            try {
                List<Disease> diseases = new ArrayList<>();
                List<String> lines = Files.readAllLines(file.toPath());
                if(lines.size() == 0) throw new IOException("Illegal csv file.");
                List<String> symptoms = new ArrayList<>(Arrays.asList(lines.get(0).split(",", -1)));
                symptoms.remove(0);
                if(symptoms.size() <= 1) throw new IOException("Illegal csv file.");
                for (int i = 1; i < lines.size(); i++) {
                    List<String> tokens =  new ArrayList<>(Arrays.asList(lines.get(i).split(",", -1)));
                    if(tokens.size() <= 0) throw new IOException("Illegal csv file.");
                    if(tokens.size() != symptoms.size() + 1) throw new IOException("Illegal csv file.");
                    Disease disease = new Disease(tokens.get(0));
                    double[] values = new double[tokens.size()];
                    for (int j = 1; j < tokens.size(); j++) {
                        try {
                            values[j] = Double.parseDouble(tokens.get(j));
                            disease.getSymptoms().put(symptoms.get(j - 1), new Symptom(symptoms.get(j - 1), values[j]));
                        } catch (NumberFormatException e) {
                            throw new IOException("Illegal csv file.");
                        }
                    }
                    diseases.add(disease);
                }
                knowledgeBase.clearKnownDiseases();
                knowledgeBase.clearKnownSymptoms();
                for (String symptom : symptoms) {
                    knowledgeBase.addKnownSymptom(symptom);
                }
                for (Disease disease : diseases) {
                    knowledgeBase.addKnownDisease(disease);
                }
            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText(e.getMessage());
                alert.show();
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void onAbout(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Made by:\n" +
                "Tomasz Herman\n" +
                "Karol Krupa\n" +
                "Piotr Samborski\n" +
                "Bartek Truszkowski\n" +
                "Paweł Flis");
        alert.show();
    }

    public void setStageAndSetupListeners(Stage stage) {
        this.stage = stage;
    }
}
