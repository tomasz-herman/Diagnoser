package pl.edu.pw.mini.msi.diagnoser.utils;

import javafx.util.StringConverter;

public class StringDoubleConverter extends StringConverter<Double> {
    @Override
    public String toString(Double object) {
        return object.toString();
    }

    @Override
    public Double fromString(String string) {
        try {
            return Double.parseDouble(string);
        } catch (Exception e) {
            return 0.0;
        }
    }
}
