package pl.edu.pw.mini.msi.diagnoser.models;

import javafx.beans.property.SimpleMapProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

public class Disease {
    private final SimpleStringProperty name;
    private final SimpleMapProperty<String, Symptom> symptoms;

    public Disease(String name) {
        this.name = new SimpleStringProperty(name);
        symptoms = new SimpleMapProperty<>(FXCollections.observableHashMap());
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public ObservableMap<String, Symptom> getSymptoms() {
        return symptoms.get();
    }

    public SimpleMapProperty<String, Symptom> symptomsProperty() {
        return symptoms;
    }

    public void setSymptoms(ObservableMap<String, Symptom> symptoms) {
        this.symptoms.set(symptoms);
    }

    public void removeSymptom(String symptom) {
        symptoms.remove(symptom);
    }

    public void addSymptom(String symptom) {
        symptoms.put(symptom, new Symptom(symptom));
    }

    public Symptom getSymptom(String symptom) {
        return symptoms.get(symptom);
    }

    public void clearSymptoms() {
        symptoms.clear();
    }

    @Override
    public String toString() {
        return "Disease{" +
                "name=" + name +
                ", symptoms=" + symptoms +
                '}';
    }
}
