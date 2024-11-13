package com.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import java.util.Random;

public class StockChartApp extends Application {

    @Override
    public void start(Stage stage) {
        stage.setTitle("FST Stock Price Chart");
        
        // Definiere die Achsen
        NumberAxis xAxis = new NumberAxis(1, 30, 1); // Zeige nur einen Monat
        xAxis.setLabel("Day of the Month");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Stock Price ($)");
        
        // Erstelle das Liniendiagramm
        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("FST Stock Price Development Over One Month");
        
        // Erstelle die Datenreihe
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("FST");
        
        // Initialer Kurswert
        double price = 150;
        double targetPrice = 400;
        Random random = new Random();
        
        // Simuliere einen realistischen Kursverlauf mit Schwankungen und einer sanften Entwicklung zum Zielwert
        for (double day = 1; day <= 30; day += 0.5) {
            // Kursbewegung: Zufällige Variation, um eine realistische Entwicklung mit Schwankungen zu simulieren
            double dailyChange = (random.nextDouble() - 0.5) * 30; // Variiert um ±15$
            price += dailyChange;
            
            // Sicherstellen, dass der Preis sich auf den Zielwert entwickelt
            double progress = day / 30.0; // Verhältnis des aktuellen Tages zur Gesamtzeit
            double upwardTrend = (targetPrice - 150) * progress;
            price = price * 0.7 + upwardTrend * 0.3; // Glättung der Anpassung an den Zielwert
            
            // Preis nicht unter einen Mindestwert fallen lassen
            if (price < 100) {
                price = 100 + random.nextDouble() * 20; // Begrenzung nach unten
            }
            
            // Hinzufügen des Datenpunkts
            series.getData().add(new XYChart.Data<>(day, price));
        }
        
        // Füge die Datenreihe dem Diagramm hinzu
        lineChart.getData().add(series);
        
        // Erstelle die Szene
        Scene scene = new Scene(lineChart, 1000, 800); // Größere Szene
        stage.setScene(scene);
        
        // Zeige das Fenster
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}




