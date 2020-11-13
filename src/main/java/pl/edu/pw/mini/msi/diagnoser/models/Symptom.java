package pl.edu.pw.mini.msi.diagnoser.models;

import javafx.beans.property.*;

import java.util.Random;

public class Symptom {
    private final SimpleStringProperty name;
    private final SimpleObjectProperty<Double> degree;
    private final SimpleObjectProperty<Double> lower;
    private final SimpleObjectProperty<Double> upper;

    public Symptom(String name) {
        this.name = new ReadOnlyStringWrapper(name);
        this.degree = new SimpleObjectProperty<>(round(new Random().nextDouble(), 2));
        this.lower = new SimpleObjectProperty<>(0.0);
        this.upper = new SimpleObjectProperty<>(0.0);
    }

    public Symptom(String name, double degree) {
        this.name = new ReadOnlyStringWrapper(name);
        this.degree = new SimpleObjectProperty<>(clamp(degree));
        this.lower = new SimpleObjectProperty<>(clamp(degree));
        this.upper = new SimpleObjectProperty<>(clamp(degree));
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

    public Double getDegree() {
        return degree.get();
    }

    public SimpleObjectProperty<Double> degreeProperty() {
        return degree;
    }

    public void setDegree(Double degree) {
        this.degree.set(clamp(degree));
    }

    public Double getLower() {
        return lower.get();
    }

    public SimpleObjectProperty<Double> lowerProperty() {
        return lower;
    }

    public void setLower(Double lower) {
        this.lower.set(clamp(lower));
    }

    public Double getUpper() {
        return upper.get();
    }

    public SimpleObjectProperty<Double> upperProperty() {
        return upper;
    }

    public void setUpper(Double upper) {
        this.upper.set(clamp(upper));
    }

    private static double clamp(double x) {
        return Math.max(0, Math.min(1, x));
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    @Override
    public String toString() {
        return getName() + "(" + getDegree() + ", " + getLower() + ", " + getUpper() + ")";
    }
}
