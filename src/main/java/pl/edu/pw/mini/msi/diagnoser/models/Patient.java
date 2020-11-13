package pl.edu.pw.mini.msi.diagnoser.models;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class Patient {
    private final SimpleStringProperty name;
    private final SimpleObjectProperty<Disease> disease;

    public Patient(String name) {
        this.name = new SimpleStringProperty(name);
        this.disease = new SimpleObjectProperty<>(new Disease(""));
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

    public Disease getDisease() {
        return disease.get();
    }

    public SimpleObjectProperty<Disease> diseaseProperty() {
        return disease;
    }

    public void setDisease(Disease disease) {
        this.disease.set(disease);
    }
}
