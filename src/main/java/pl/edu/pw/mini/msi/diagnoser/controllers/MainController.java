package pl.edu.pw.mini.msi.diagnoser.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import pl.edu.pw.mini.msi.diagnoser.models.Disease;
import pl.edu.pw.mini.msi.diagnoser.models.KnowledgeBase;
import pl.edu.pw.mini.msi.diagnoser.models.Patient;

import java.util.Random;

public class MainController {
    public TableView<Disease> diseasesTable;
    public TableView<Patient> patientsTable;
    public Button addPatientButton;
    public Button removePatientButton;

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
}
