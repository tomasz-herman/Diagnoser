package pl.edu.pw.mini.msi.diagnoser.models;

import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.beans.property.SimpleSetProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.collections.SetChangeListener;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.StringConverter;
import pl.edu.pw.mini.msi.diagnoser.utils.Distance;
import pl.edu.pw.mini.msi.diagnoser.utils.EditCell;
import pl.edu.pw.mini.msi.diagnoser.utils.IdentityStringConverter;
import pl.edu.pw.mini.msi.diagnoser.utils.StringDoubleConverter;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class KnowledgeBase {
    private final SimpleMapProperty<String, Disease> knownDiseases;
    private final SimpleSetProperty<String> knownSymptoms;
    private final SimpleListProperty<Patient> patients;

    private final TableView<Disease> diseasesTable;
    private final TableView<Patient> patientsTable;

    public KnowledgeBase(TableView<Disease> diseasesTable, TableView<Patient> patientsTable) {
        this.diseasesTable = diseasesTable;
        this.patientsTable = patientsTable;

        knownDiseases = new SimpleMapProperty<>(FXCollections.observableHashMap());
        knownSymptoms = new SimpleSetProperty<>(FXCollections.observableSet());
        patients = new SimpleListProperty<>(FXCollections.observableArrayList());

        knownSymptoms.addListener(this::onSymptomsChanged);
        knownDiseases.addListener(this::onKnownDiseasesChanged);
        patients.addListener(this::onPatientsChanged);

        createHeaderDiseaseColumn();
        createHeaderPatientColumn();
    }

    private void createHeaderDiseaseColumn() {
        TableColumn<Disease, String> column = new TableColumn<>("Disease");
        column.setCellValueFactory(disease -> disease.getValue().nameProperty());
        diseasesTable.getColumns().add(column);
    }

    private void createHeaderPatientColumn() {
        TableColumn<Patient, String> nameColumn = new TableColumn<>("Patient");
        nameColumn.setCellFactory(c -> new EditCell<>(new IdentityStringConverter()));
        nameColumn.setCellValueFactory(patient -> patient.getValue().nameProperty());
        patientsTable.getColumns().add(nameColumn);
        TableColumn<Patient, String> diagnosisColumn = new TableColumn<>("Diagnosis");
        diagnosisColumn.setCellValueFactory(patient -> patient.getValue().getDisease().nameProperty());
        patientsTable.getColumns().add(diagnosisColumn);
    }

    private void onSymptomsChanged(SetChangeListener.Change<? extends String> change) {
        if (change.wasRemoved()) {
            diseasesTable.getColumns().removeIf(col -> col.getText() == change.getElementRemoved());
            patientsTable.getColumns().removeIf(col -> col.getText() == change.getElementRemoved());
            knownDiseases.values().forEach(disease -> disease.removeSymptom(change.getElementRemoved()));
            patients.forEach(patient -> patient.getDisease().removeSymptom(change.getElementRemoved()));
        } else if (change.wasAdded()) {
            knownDiseases.values().forEach(disease -> disease.addSymptom(change.getElementAdded()));
            patients.forEach(patient -> patient.getDisease().addSymptom(change.getElementAdded()));

            TableColumn<Disease, Double> column = new TableColumn<>(change.getElementAdded());
            column.setCellFactory(c -> new EditCell<>(new StringDoubleConverter()));
            column.setOnEditCommit(event -> {
                event.getRowValue().getSymptom(event.getTableColumn().getText()).setDegree(event.getNewValue());
                recalculateBounds();
            });
            column.setCellValueFactory(disease -> disease.getValue().getSymptom(change.getElementAdded()).degreeProperty());
            diseasesTable.getColumns().add(column);

            TableColumn<Patient, Double> p_column = new TableColumn<>(change.getElementAdded());
            p_column.setCellFactory(c -> new EditCell<>(new StringDoubleConverter()));
            p_column.setOnEditCommit(event -> {
                event.getRowValue().getDisease().getSymptom(event.getTableColumn().getText()).setDegree(event.getNewValue());
                calculateBounds(event.getRowValue().getDisease().getSymptoms());
                diagnose(event.getRowValue(), Distance::disease_hamming);
            });
            p_column.setCellValueFactory(disease -> disease.getValue().getDisease().getSymptom(change.getElementAdded()).degreeProperty());
            patientsTable.getColumns().add(p_column);
        }
        recalculateBounds();
    }

    private void onKnownDiseasesChanged(MapChangeListener.Change<? extends String, ? extends Disease> change) {
        if(change.wasRemoved()) {
            diseasesTable.getItems().removeIf(disease -> disease.getName().equals(change.getKey()));
        } else if(change.wasAdded()) {
            diseasesTable.getItems().add(change.getValueAdded());
        }
        recalculateBounds();
    }

    private void recalculateBounds(){
        for (Disease disease : knownDiseases.values()) {
            calculateBounds(disease.getSymptoms());
        }
        for (Patient patient : patients) {
            diagnose(patient, Distance::disease_hamming);
        }
    }

    private void onPatientsChanged(ListChangeListener.Change<? extends Patient> change) {
        while (change.next()) {
            if(change.wasRemoved()) {
                patientsTable.getItems().removeAll(change.getRemoved());
            } else if(change.wasAdded()) {
                patientsTable.getItems().addAll(change.getAddedSubList());
            }
        }
    }

    public void addKnownDisease(String diseaseName){
        Disease disease = new Disease(diseaseName);
        for (String symptom : knownSymptoms) {
            disease.addSymptom(symptom);
        }
        knownDiseases.put(diseaseName, disease);
    }

    public void removeKnownDisease(String disease){
        knownDiseases.remove(disease);
    }

    public void clearKnownDiseases(){
        knownDiseases.clear();
    }

    public void addKnownSymptom(String symptom){
        knownSymptoms.add(symptom);
    }

    public void removeKnownSymptom(String symptom){
        knownSymptoms.remove(symptom);
    }

    public void clearKnownSymptoms(){
        knownSymptoms.clear();
    }

    public void addPatient(Patient patient){
        patient.getDisease().clearSymptoms();
        for (String symptom : knownSymptoms) {
            patient.getDisease().addSymptom(symptom);
        }
        calculateBounds(patient.getDisease().getSymptoms());
        diagnose(patient, Distance::disease_hamming);
        patients.add(patient);
    }

    public void removePatient(Patient patient){
        patients.remove(patient);
    }

    public void clearPatients(){
        patients.clear();
    }

    public Collection<String> getKnownDiseases(){
        return Collections.unmodifiableSet(knownDiseases.keySet());
    }

    public Collection<String> getSymptoms(){
        return Collections.unmodifiableSet(knownSymptoms);
    }

    public void calculateBounds(Map<String, Symptom> symptoms) {
        calculateLower(symptoms);
        calculateUpper(symptoms);
    }

    public void calculateLower(Map<String, Symptom> symptoms) {
        for (Symptom symptom : symptoms.values()) {
            double sup = - Double.MAX_VALUE;
            for (Disease disease : knownDiseases.values()) {
                double inf = Double.MAX_VALUE;
                for (String knownSymptom : knownSymptoms) {
                    double min = Math.min(1, 1 - disease.getSymptom(knownSymptom).getDegree() + symptoms.get(knownSymptom).getDegree());
                    if(min < inf) {
                        inf = min;
                    }
                }
                double max = Math.max(0, disease.getSymptom(symptom.getName()).getDegree() + inf - 1);
                if(max > sup) {
                    sup = max;
                }
            }
            symptom.setLower(sup);
        }
    }

    public void calculateUpper(Map<String, Symptom> symptoms) {
        for (Symptom symptom : symptoms.values()) {
            double inf = Double.MAX_VALUE;
            for (Disease disease : knownDiseases.values()) {
                double sup = - Double.MAX_VALUE;
                for (String knownSymptom : knownSymptoms) {
                    double max = Math.max(0, disease.getSymptom(knownSymptom).getDegree() + symptoms.get(knownSymptom).getDegree() - 1);
                    if(max > sup) {
                        sup = max;
                    }
                }
                double min = Math.min(1, 1 - disease.getSymptom(symptom.getName()).getDegree() + sup);
                if(min < inf) {
                    inf = min;
                }
            }
            symptom.setUpper(inf);
        }
    }

    public void diagnose(Patient patient, Distance<Disease, Double> distanceFunction) {
        double closest = Double.MAX_VALUE;
        for (Disease disease : knownDiseases.values()) {
            double distance = distanceFunction.distance(disease, patient.getDisease());
            if(distance < closest) {
                closest = distance;
                patient.getDisease().setName(disease.getName());
            }
        }
    }
}
