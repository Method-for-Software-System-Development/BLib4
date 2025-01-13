package gui.librarian.LibraryReports_UI;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import static javafx.application.Application.launch;

public class LibraryReports_UI {







        /**
         * Generates a sample BarChart for borrowing reports.
         *
         * @return BarChart instance with example data.
         */
        public static BarChart<String, Number> generateBorrowingReportChart() {
            // Axes
            CategoryAxis xAxis = new CategoryAxis();
            xAxis.setLabel("Book Title");

            NumberAxis yAxis = new NumberAxis();
            yAxis.setLabel("Time (days)");

            // Chart
            BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
            barChart.setTitle("Borrowing Report");

            // Series
            XYChart.Series<String, Number> totalBorrowTimeSeries = new XYChart.Series<>();
            totalBorrowTimeSeries.setName("Total Borrow Time");

            XYChart.Series<String, Number> lateReturnTimeSeries = new XYChart.Series<>();
            lateReturnTimeSeries.setName("Late Return Time");

            // Example data
            totalBorrowTimeSeries.getData().add(new XYChart.Data<>("Book A", 120));
            totalBorrowTimeSeries.getData().add(new XYChart.Data<>("Book B", 90));

            lateReturnTimeSeries.getData().add(new XYChart.Data<>("Book A", 15));
            lateReturnTimeSeries.getData().add(new XYChart.Data<>("Book B", 10));

            // Add data to the chart
            barChart.getData().addAll(totalBorrowTimeSeries, lateReturnTimeSeries);

            return barChart;
        }

        /**
         * Generates a sample BarChart for subscriber status reports.
         *
         * @return BarChart instance with example data.
         */
        public static BarChart<String, Number> generateSubscriberStatusReportChart() {
            // Axes
            CategoryAxis xAxis = new CategoryAxis();
            xAxis.setLabel("Day of the Month");

            NumberAxis yAxis = new NumberAxis();
            yAxis.setLabel("Number of Subscribers");

            // Chart
            BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
            barChart.setTitle("Subscriber Status Report");

            // Series
            XYChart.Series<String, Number> activeSubscribersSeries = new XYChart.Series<>();
            activeSubscribersSeries.setName("Active Subscribers");

            XYChart.Series<String, Number> frozenSubscribersSeries = new XYChart.Series<>();
            frozenSubscribersSeries.setName("Frozen Subscribers");

            // Example data
            activeSubscribersSeries.getData().add(new XYChart.Data<>("Day 1", 120));
            activeSubscribersSeries.getData().add(new XYChart.Data<>("Day 2", 130));

            frozenSubscribersSeries.getData().add(new XYChart.Data<>("Day 1", 15));
            frozenSubscribersSeries.getData().add(new XYChart.Data<>("Day 2", 10));

            // Add data to the chart
            barChart.getData().addAll(activeSubscribersSeries, frozenSubscribersSeries);

            return barChart;
        }
}


