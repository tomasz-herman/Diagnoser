package pl.edu.pw.mini.msi.diagnoser.utils;

import pl.edu.pw.mini.msi.diagnoser.models.Disease;
import pl.edu.pw.mini.msi.diagnoser.models.Symptom;

@FunctionalInterface
public interface Distance<T, R> {
    R distance(T from, T to);

    static Double disease_hamming(Disease from, Disease to) {
        double result = 0.0;
        for (Symptom f : from.getSymptoms().values()) {
            Symptom t = to.getSymptoms().get(f.getName());
            result +=
                    Math.abs(f.getUpper() - t.getUpper()) +
                    Math.abs(f.getLower() - t.getLower()) +
                    Math.abs(f.getLower() - f.getUpper() - t.getLower() + t.getUpper()) ;
        }
        return result / (2 * from.getSymptoms().size());
    }

}
