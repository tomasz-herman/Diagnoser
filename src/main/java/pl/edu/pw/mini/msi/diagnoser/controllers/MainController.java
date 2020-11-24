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

    private static final String[] names = new String[] {
            "Dorota Przybylska", "Oksana Sobczak", "Wiktoria Kwiatkowska", "Klementyna Pietrzak", "Dominika Kaźmierczak", "Ida Błaszczyk", "Aneta Szymczak", "Nina Duda", "Halina Kwiatkowska", "Agata Kaźmierczak", "Izabela Makowska", "Zuzanna Lewandowska", "Oksana Zielińska", "Patrycja Zalewska", "Andżelika Głowacka", "Oktawia Adamska", "Paula Krawczyk", "Ilona Walczak", "Marcelina Kucharska", "Bogna Malinowska", "Marlena Wójcik", "Katarzyna Wróblewska", "Ewa Jaworska", "Cecylia Sokołowska", "Urszula Mróz", "Łucja Zalewska", "Czesława Adamska", "Cecylia Sikora", "Alana Stępień", "Eliza Makowska", "Patrycja Woźniak", "Edyta Mazurek", "Gabriela Kozłowska", "Klaudia Borkowska", "Bianka Kowalczyk", "Faustyna Wysocka", "Józefa Kowalska", "Amalia Urbańska", "Klara Kozłowska", "Anna Zielińska", "Stefania Cieślak", "Paula Przybylska", "Asia Przybylska", "Stanisława Błaszczyk", "Kaja Sokołowska", "Natalia Kubiak", "Marta Michalak", "Bogna Wojciechowska", "Bernadetta Dąbrowska", "Luiza Kucharska", "Patrycja Sobczak", "Asia Pawlak", "Antonina Szewczyk", "Elena Lis", "Berenika Krupa", "Franciszka Mazur", "Jagoda Bąk", "Nikola Kaźmierczak", "Florencja Pawlak", "Zuzanna Piotrowska", "Andrea Duda", "Alice Mróz", "Iga Malinowska", "Katarzyna Sawicka", "Magdalena Czerwińska", "Łucja Kubiak", "Matylda Gajewska", "Olga Mazur", "Elżbieta Kowalska", "Angelika Szulc", "Maria Marciniak", "Monika Mazurek", "Magdalena Szulc", "Oksana Jakubowska", "Edyta Szewczyk", "Konstancja Przybylska", "Dagmara Wójcik", "Kinga Sadowska", "Bogna Makowska", "Joanna Wójcik", "Daria Dąbrowska", "Gabriela Urbańska", "Felicja Malinowska", "Julia Jasińska", "Edyta Kucharska", "Halina Jakubowska", "Zuzanna Nowak", "Wanda Makowska", "Julianna Michalak", "Urszula Zielińska", "Asia Kucharska", "Arleta Kubiak", "Wioletta Kaźmierczak", "Dagmara Andrzejewska", "Lila Andrzejewska", "Franciszka Zakrzewska", "Olga Wasilewska", "Lara Kaźmierczak", "Halina Pietrzak", "Irena Sikora", "Gustaw Baranowski", "Mikołaj Chmielewski", "Rafał Borkowski", "Eustachy Górski", "Miron Wysocki", "Gustaw Wiśniewski", "Alex Kaczmarczyk", "Ignacy Chmielewski", "Paweł Jasiński", "Joachim Malinowski", "Milan Kołodziej", "Oktawian Kubiak", "Henryk Kucharski", "Natan Sobczak", "Alek Kamiński", "Kacper Stępień", "Bronisław Zalewski", "Damian Włodarczyk", "Kordian Górecki", "Natan Malinowski", "Jędrzej Witkowski", "Albert Krawczyk", "Alexander Cieślak", "Alojzy Marciniak", "Aleksy Tomaszewski", "Lucjan Pietrzak", "Emil Zakrzewska", "Henryk Wysocki", "Roman Chmielewski", "Karol Jankowski", "Ireneusz Gajewska", "Franciszek Wysocki", "Kajetan Krawczyk", "Daniel Górski", "Natan Mazur", "Mirosław Baranowski", "Arkadiusz Krupa", "Amir Woźniak", "Aureliusz Błaszczyk", "Adrian Krawczyk", "Jędrzej Sikora", "Heronim Adamska", "Alexander Stępień", "Roman Mazurek", "Cezary Malinowski", "Joachim Borkowski", "Juliusz Rutkowski", "Mieszko Borkowski", "Martin Dąbrowski", "Jerzy Lis", "Czesław Zawadzki", "Michał Mazurek", "Alfred Czarnecki", "Kajetan Andrzejewski", "Cyprian Brzeziński", "Amadeusz Zakrzewska", "Heronim Kaczmarczyk", "Łukasz Kucharski", "Damian Pietrzak", "Ireneusz Głowacka", "Mirosław Górski", "Marcin Szymczak", "Florian Rutkowski", "Dominik Przybylski", "Natan Kozłowski", "Franciszek Krupa", "Patryk Krajewska", "Adrian Andrzejewski", "Alex Krawczyk", "Robert Szymczak", "Alexander Kołodziej", "Fryderyk Czerwiński", "Ireneusz Przybylski", "Igor Duda", "Antoni Sobczak", "Cezary Rutkowski", "Piotr Dąbrowski", "Kazimierz Sawicki", "Natan Zakrzewska", "Cyprian Szewczyk", "Krzysztof Szulc", "Krystian Wasilewska", "Robert Szymczak", "Oktawian Sawicki", "Klaudiusz Szymański", "Marian Chmielewski", "Olaf Lewandowski", "Igor Wróblewski", "Alfred Woźniak", "Karol Baranowski", "Oktawian Sadowska", "Alek Witkowski", "Klaudiusz Górski", "Dariusz Kwiatkowski", "Ksawery Dąbrowski", "Przemysław Stępień", "Jakub Sikorska", "Dorian Ostrowski", "Diego Kozłowski"
    };

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
        knowledgeBase.addPatient(new Patient(getRandomName()));
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

    private static final Random random = new Random();
    private String getRandomName() {
        return names[random.nextInt(names.length)];
    }
}
