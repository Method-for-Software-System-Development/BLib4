package gui.librarian.libraryReports;

import entities.report.BorrowingReport;
import entities.report.SubscriberStatusReport;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import logic.communication.ClientUI;
import logic.communication.SceneManager;
import logic.librarian.ReportsGenerator_Controller;
import logic.user.Subscriber_Controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class LibraryReports_Controller
{
    // FXML fields
    @FXML
    private Text userGreeting;
    @FXML
    private Button homePageButton;
    @FXML
    private ImageView homePageImageView;
    @FXML
    private Button librarianDashButton;
    @FXML
    private ImageView librarianDashImageView;
    @FXML
    private Button searchBooksButton;
    @FXML
    private ImageView searchBooksImageView;
    @FXML
    private Button newBorrowButton;
    @FXML
    private ImageView newBorrowImageView;
    @FXML
    private Button addSubscriberButton;
    @FXML
    private ImageView addSubscriberImageView;
    @FXML
    private Button logoutButton;
    @FXML
    private ImageView logoutImageView;
    @FXML
    private Button exitButton;
    @FXML
    private ImageView exitImageView;

    // Form FXML fields
    @FXML
    private ChoiceBox<String> reportChoiceBox;
    @FXML
    private ChoiceBox<String> monthChoiceBox;
    @FXML
    private ChoiceBox<String> yearChoiceBox;

    // Report FXML fields
    @FXML
    private VBox reportVBox;
    @FXML
    private Text reportTitle;
    @FXML
    private BarChart<String, Number> borrowingReportChart;
    @FXML
    private BarChart<String, Number> subscriberStatusReportChart;

    // Fields
    private Subscriber_Controller subscriberController;
    private ReportsGenerator_Controller reportsGeneratorController;

    /**
     * Initializes the LibraryReports_Controller.
     */
    @FXML
    public void initialize()
    {
        // Get the singleton instance of Subscriber_Controller
        subscriberController = Subscriber_Controller.getInstance();
        // Get the singleton instance of ReportsGenerator_Controller
        reportsGeneratorController = ReportsGenerator_Controller.getInstance();

        // Set the greeting message
        userGreeting.setText(getGreetingMessage() + " " + subscriberController.getLoggedLibrarian().getName() + " !");

        // Set the icons for the buttons
        homePageButton.setOnMouseEntered(event ->
        {
            homePageImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/home_24dp_525FE1.png")));
        });
        homePageButton.setOnMouseExited(event ->
        {
            homePageImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/home_24dp_FFFFFF.png")));
        });

        librarianDashButton.setOnMouseEntered(event ->
        {
            librarianDashImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/local_library_24dp_F86F03.png")));
        });
        librarianDashButton.setOnMouseExited(event ->
        {
            librarianDashImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/local_library_24dp_FFFFFF.png")));
        });

        searchBooksButton.setOnMouseEntered(event ->
        {
            searchBooksImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/search_24dp_525FE1.png")));
        });
        searchBooksButton.setOnMouseExited(event ->
        {
            searchBooksImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/search_24dp_FFFFFF.png")));
        });

        newBorrowButton.setOnMouseEntered(event ->
        {
            newBorrowImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/post_add_24dp_525FE1.png")));
        });
        newBorrowButton.setOnMouseExited(event ->
        {
            newBorrowImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/post_add_24dp_FFFFFF.png")));
        });

        addSubscriberButton.setOnMouseEntered(event ->
        {
            addSubscriberImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/person_add_24dp_525FE1.png")));
        });
        addSubscriberButton.setOnMouseExited(event ->
        {
            addSubscriberImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/person_add_24dp_FFFFFF.png")));
        });

        logoutButton.setOnMouseEntered(event ->
        {
            logoutImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/logout_24dp_525FE1.png")));
        });
        logoutButton.setOnMouseExited(event ->
        {
            logoutImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/logout_24dp_FFFFFF.png")));
        });

        exitButton.setOnMouseEntered(event ->
        {
            exitImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/close_24dp_525FE1.png")));
        });
        exitButton.setOnMouseExited(event ->
        {
            exitImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/close_24dp_FFFFFF.png")));
        });

        reportChoiceBox.setItems(FXCollections.observableArrayList(
                "Borrowing Times",
                "Subscribers Status"
        ));
        reportChoiceBox.setValue("Borrowing Times");

        monthChoiceBox.setItems(FXCollections.observableArrayList(
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        ));
        monthChoiceBox.setValue("January");

        yearChoiceBox.setItems(FXCollections.observableArrayList("2025", "2024"));
        // Set a default value if desired
        yearChoiceBox.setValue("2025");
    }

    /**
     * Returns a greeting message based on the current time of the day.
     *
     * @return A greeting message.
     */
    private String getGreetingMessage()
    {
        int hour = java.time.LocalTime.now().getHour();

        if (hour >= 5 && hour < 12)
        {
            return "Good Morning";
        }
        else if (hour >= 12 && hour < 17)
        {
            return "Good Afternoon";
        }
        else if (hour >= 17 && hour < 21)
        {
            return "Good Evening";
        }
        else
        {
            return "Good Night";
        }
    }

    /**
     * Handles the request to generate a report.
     */
    @FXML
    private void handleRequestReport()
    {
        // Clear the previous report
        reportVBox.setVisible(false);

        // Save Borrowing Report Chart Axis Labels
        String borrowingXAxisLabel = ((CategoryAxis) borrowingReportChart.getXAxis()).getLabel();
        String borrowingYAxisLabel = ((NumberAxis) borrowingReportChart.getYAxis()).getLabel();

        // Create a new Borrowing Report Chart
        reportVBox.getChildren().remove(borrowingReportChart);
        CategoryAxis newBorrowingXAxis = new CategoryAxis();
        newBorrowingXAxis.setLabel(borrowingXAxisLabel); // Restore X-axis label
        NumberAxis newBorrowingYAxis = new NumberAxis();
        newBorrowingYAxis.setLabel(borrowingYAxisLabel); // Restore Y-axis label
        borrowingReportChart = new BarChart<>(newBorrowingXAxis, newBorrowingYAxis);
        borrowingReportChart.setVisible(false);
        borrowingReportChart.setManaged(false);
        reportVBox.getChildren().add(borrowingReportChart);

        // Save Subscriber Status Report Chart Axis Labels
        String subscriberXAxisLabel = ((CategoryAxis) subscriberStatusReportChart.getXAxis()).getLabel();
        String subscriberYAxisLabel = ((NumberAxis) subscriberStatusReportChart.getYAxis()).getLabel();

        // Create a new Subscriber Status Report Chart
        reportVBox.getChildren().remove(subscriberStatusReportChart);
        CategoryAxis newSubscriberXAxis = new CategoryAxis();
        newSubscriberXAxis.setLabel(subscriberXAxisLabel); // Restore X-axis label
        NumberAxis newSubscriberYAxis = new NumberAxis();
        newSubscriberYAxis.setLabel(subscriberYAxisLabel); // Restore Y-axis label
        subscriberStatusReportChart = new BarChart<>(newSubscriberXAxis, newSubscriberYAxis);
        subscriberStatusReportChart.setVisible(false);
        subscriberStatusReportChart.setManaged(false);
        reportVBox.getChildren().add(subscriberStatusReportChart);

        String reportType = reportChoiceBox.getValue();
        if (reportType.equals("Borrowing Times"))
        {
            reportType = "borrowingReport";
        }
        else if (reportType.equals("Subscribers Status"))
        {
            reportType = "subscribersStatus";
        }
        String month = monthChoiceBox.getValue();
        String year = yearChoiceBox.getValue();

        // Convert string year to int and convert month name to int (1-12)
        int selectedYear = Integer.parseInt(year);
        int selectedMonth = getMonthNumber(month);

        // Get the current year and month
        LocalDate now = LocalDate.now();
        int currentYear = now.getYear();
        int currentMonth = now.getMonthValue();

        if (selectedYear > currentYear || (selectedYear == currentYear && selectedMonth > currentMonth))
        {
            showErrorAlert("Invalid Date", "You cannot generate a report for a future date. Please select a valid past month/year.");
        }
        else if (selectedYear == currentYear && selectedMonth == currentMonth)
        {
            showErrorAlert("Report Not Available Yet", "The monthly report for the current month will only be available on the 1st of next month at 00:00.");
        }
        else
        {
            reportsGeneratorController.setMonth(String.valueOf(selectedMonth));
            reportsGeneratorController.setYear(year);
            if (reportsGeneratorController.checkIfReportIsReady(reportType))
            {
                if (reportType.equals("borrowingReport"))
                {
                    reportVBox.setVisible(true);
                    borrowingReportChart.setVisible(true);
                    borrowingReportChart.setManaged(true);
                    borrowingReportChart.getData().setAll(generateBorrowingReportChart().getData());
                    borrowingReportChart.prefWidthProperty().bind(reportVBox.widthProperty());
                    borrowingReportChart.prefHeightProperty().bind(reportVBox.heightProperty());
                    reportTitle.setText("Borrowing Times Report " + reportsGeneratorController.getMonth() + "/" + reportsGeneratorController.getYear());
                }
                else if (reportType.equals("subscribersStatus"))
                {
                    reportVBox.setVisible(true);
                    subscriberStatusReportChart.setVisible(true);
                    subscriberStatusReportChart.setManaged(true);
                    subscriberStatusReportChart.getData().setAll(generateSubscriberStatusReportChart().getData());
                    subscriberStatusReportChart.prefWidthProperty().bind(reportVBox.widthProperty());
                    subscriberStatusReportChart.prefHeightProperty().bind(reportVBox.heightProperty());
                    reportTitle.setText("Subscribers Status Report " + reportsGeneratorController.getMonth() + "/" + reportsGeneratorController.getYear());
                }
            }
            else
            {
                showErrorAlert("Report Not Available", "The requested report is not exist in the system.");
            }
        }
    }

    /**
     * Converts a month name in English to its 1-based integer value.
     *
     * @param monthName The month name (e.g., "January", "February").
     * @return The integer month value (1 = January, 2 = February, etc.)
     */
    private int getMonthNumber(String monthName)
    {
        switch (monthName)
        {
            case "January":
                return 1;
            case "February":
                return 2;
            case "March":
                return 3;
            case "April":
                return 4;
            case "May":
                return 5;
            case "June":
                return 6;
            case "July":
                return 7;
            case "August":
                return 8;
            case "September":
                return 9;
            case "October":
                return 10;
            case "November":
                return 11;
            case "December":
                return 12;
            default:
                throw new IllegalArgumentException("Invalid month name: " + monthName);
        }
    }

    /**
     * Generates BarChart for borrowing reports.
     *
     * @return BarChart instance with the data.
     */
    public BarChart<String, Number> generateBorrowingReportChart()
    {
        // Axes
        CategoryAxis xAxis = new CategoryAxis();

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

        // load data
        //get the correct report
        BorrowingReport borrowingReport = reportsGeneratorController.getBorrowingReport();
        //add to graph the data for every book in the report
        Map<String, List<String>> borrowingData = borrowingReport.getBorrowingData();
        for (Map.Entry<String, List<String>> entry : borrowingData.entrySet())
        {
            String bookTitle = splitTitle(entry.getKey());

            List<String> dataOfBook = entry.getValue();
            totalBorrowTimeSeries.getData().add(new XYChart.Data<>(bookTitle, Integer.parseInt(dataOfBook.get(0))));
            lateReturnTimeSeries.getData().add(new XYChart.Data<>(bookTitle, Integer.parseInt(dataOfBook.get(1))));
        }
        // Add data to the chart
        barChart.getData().addAll(totalBorrowTimeSeries, lateReturnTimeSeries);

        return barChart;
    }

    /**
     * Splits a title into multiple lines to fit the chart.
     *
     * @param title The title to split.
     * @return The formatted title.
     */
    public String splitTitle(String title)
    {
        StringBuilder formattedTitle = new StringBuilder();
        String[] words = title.split(" ");  // Split the title into words based on spaces

        StringBuilder currentLine = new StringBuilder();  // Accumulate words for the current line

        for (String word : words)
        {
            // If adding this word exceeds the maxLength, start a new line
            if (currentLine.length() + word.length() + (currentLine.length() > 0 ? 1 : 0) > 8)
            {
                // Add the current line to the formattedTitle and start a new line
                formattedTitle.append(currentLine.toString()).append("\n");
                currentLine.setLength(0);  // Reset current line
            }

            // Add the word to the current line, with a space if it's not the first word
            if (currentLine.length() > 0)
            {
                currentLine.append(" ");
            }
            currentLine.append(word);
        }

        // Add the last line if it has any content
        if (currentLine.length() > 0)
        {
            formattedTitle.append(currentLine.toString());
        }

        return formattedTitle.toString();
    }

    /**
     * Generates a sample BarChart for subscriber status reports.
     *
     * @return BarChart instance with example data.
     */
    public BarChart<String, Number> generateSubscriberStatusReportChart()
    {
        // Axes
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Day of the Month");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Number of Subscribers");

        // Chart
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);

        // Series
        XYChart.Series<String, Number> numberOfSubscribersSeries = new XYChart.Series<>();
        numberOfSubscribersSeries.setName("Number of Subscribers");

        XYChart.Series<String, Number> activeSubscribersSeries = new XYChart.Series<>();
        activeSubscribersSeries.setName("Active Subscribers");

        XYChart.Series<String, Number> frozenSubscribersSeries = new XYChart.Series<>();
        frozenSubscribersSeries.setName("Frozen Subscribers");

        // load data
        //get the correct report
        SubscriberStatusReport subscriberStatusReport = reportsGeneratorController.getSubscriberStatusReport();
        //add to graph the data for every book in the report
        List<String[]> dailyActiveData = subscriberStatusReport.getUsersActivityStatus();

        for (int i = 0; i < dailyActiveData.size(); i++)
        {
            String date = dailyActiveData.get(i)[0];
            numberOfSubscribersSeries.getData().add(new XYChart.Data<>(date, Integer.parseInt(dailyActiveData.get(i)[1]) + Integer.parseInt(dailyActiveData.get(i)[2])));
            activeSubscribersSeries.getData().add(new XYChart.Data<>(date, Integer.parseInt(dailyActiveData.get(i)[1])));
            frozenSubscribersSeries.getData().add(new XYChart.Data<>(date, Integer.parseInt(dailyActiveData.get(i)[2])));
        }

        // Add data to the chart
        barChart.getData().addAll(numberOfSubscribersSeries, activeSubscribersSeries, frozenSubscribersSeries);

        return barChart;
    }

    /**
     * Displays an error alert with the specified title and message.
     *
     * @param title   the title of the error alert
     * @param message the message displayed in the error alert
     */
    private void showErrorAlert(String title, String message)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(SceneManager.getStage());
        alert.showAndWait();
    }

    /**
     * Displays an information alert with the specified title and message.
     *
     * @param title   the title of the information alert
     * @param message the message displayed in the information alert
     */
    private void showInformationAlert(String title, String message)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(SceneManager.getStage());
        alert.showAndWait();
    }

    /**
     * Logs out the current user and navigates to the Home Page.
     */
    @FXML
    private void logout()
    {
        subscriberController.attemptLogOut();
    }

    /**
     * Navigates to the Home Page.
     */
    @FXML
    private void goToHomePage()
    {
        SceneManager.switchScene("/gui/common/homePage/HomePage_UI.fxml", "BLib.4 - Braude Library Management");
    }

    /**
     * Navigates to the Librarian Dashboard.
     */
    @FXML
    private void goToLibrarianDash()
    {
        SceneManager.switchScene("/gui/librarian/librarianUI/LibrarianUI_UI.fxml", "BLib.4 - Braude Library Management");
    }

    /**
     * Navigates to the Search Books page.
     */
    @FXML
    private void goToSearch()
    {
        SceneManager.switchScene("/gui/common/search/Search_UI.fxml", "BLib.4 - Braude Library Management");
    }

    /**
     * Navigates to the New Borrow page.
     */
    @FXML
    private void goToNewBorrow()
    {
        SceneManager.switchScene("/gui/librarian/newBorrow/NewBorrow_UI.fxml", "BLib.4 - Braude Library Management");
    }

    /**
     * Navigates to the Add Subscriber page.
     */
    @FXML
    private void goToAddSubscriber()
    {
        SceneManager.switchScene("/gui/librarian/addSubscriber/AddSubscriber_UI.fxml", "BLib.4 - Braude Library Management");
    }

    /**
     * Exits the application.
     */
    @FXML
    private void exitApp()
    {
        ClientUI.chat.getClient().quit();
    }
}