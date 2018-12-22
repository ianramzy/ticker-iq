package ianramzy.portfolio;

import ianramzy.Downloader;
import ianramzy.FxDialogs;
import ianramzy.Information;
import ianramzy.PrepareDataForGraph;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.paint.Color;

import java.io.*;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TableViewController implements Initializable {
    @FXML
    private TableView<StockObj> tableView;
    @FXML
    private TableColumn<StockObj, String> symbolColumn;
    @FXML
    private TableColumn<StockObj, String> entryColumn;
    @FXML
    private TableColumn<StockObj, String> quantityColumn;
    @FXML
    private TableColumn<StockObj, String> priceColumn;
    @FXML
    private TableColumn<StockObj, String> PLColumn;
    @FXML
    private TableColumn<StockObj, String> PLPColumn;
    @FXML
    private TextField symbolTextField;
    @FXML
    private TextField entryTextField;
    @FXML
    private TextField quantityTextField;
    @FXML
    public AreaChart<String, Double> areaChart;
    @FXML
    public javafx.scene.control.Label nameLabel;
    @FXML
    public javafx.scene.control.Label priceLabel;
    @FXML
    public javafx.scene.control.Label volumeLabel;
    @FXML
    public javafx.scene.control.Label changeLabel;
    @FXML
    public javafx.scene.control.Label highLabel;
    @FXML
    public javafx.scene.control.Label lowLabel;
    @FXML
    private NumberAxis yAxis;
    ObservableList<StockObj> stocks = FXCollections.observableArrayList();

    public void graphPF() {
        clearChart();
        Series<String, Double> series = new Series<>();
        for (int i = 0; i < 72; i++) {
            series.getData().add(new XYChart.Data<>(Information.getDateArrayValue(72 - i), Information.getPriceArrayValue(72 - i)));
        }
        areaChart.getData().add(series);
        series.setName(Information.getSymbol() + "-3m");
        Information.setRange(72);
        PrepareDataForGraph.minValue();     //Determine max price value of a stock (in its range)
        PrepareDataForGraph.maxValue();     //Determine max price value of a stock (in its range)
        yAxis.setAutoRanging(false);
        yAxis.setLowerBound(Information.getMinValue());
        yAxis.setUpperBound(Information.getMaxValue());
        updateLabels();
        System.out.println("DONE GRAPHING PORTFOLIO");
        System.out.println("");
    }

    //Clears chart data and labels
    public void clearChart() {
        areaChart.getData().clear();
        nameLabel.setText("");
        changeLabel.setText("");
        volumeLabel.setText("");
        priceLabel.setText("");
    }

    //Updates labels, gets information from the "information" class
    public void updateLabels() {
        nameLabel.setText(Information.getSymbol());
        priceLabel.setText(Information.getDailyPrice());
        changeLabel.setText(Information.getDailyChange());
        highLabel.setText("High: " + Double.toString(Information.getMaxValue()));
        lowLabel.setText("Low:  " + Double.toString(Information.getMinValue()));
        try {
            float dChng = Float.parseFloat(Information.getDailyChange().substring(0, Information.getDailyChange().length() - 1)); //read in the daily change string except the last char (which is a %) then convert to float
            if (dChng > 0) {
                changeLabel.setTextFill(Color.GREEN);  //if positive change set label green
                changeLabel.setText("+" + changeLabel.getText());
            }
            if (dChng < 0) {
                changeLabel.setTextFill(Color.RED);  //if negative set label red
            }
        } catch (NumberFormatException e) {
            System.out.println("Daily change not available");
        }
        volumeLabel.setText("Volume: " + Information.getDailyVolume());
    }

    /**
     * When this method is called, it will change the Scene to
     * a TableView example
     */
    public void changeScreenButtonPushed(ActionEvent event) throws IOException {
        Parent tableViewParent = FXMLLoader.load(getClass().getResource("ianramzy/MainScene.fxml"));
        Scene tableViewScene = new Scene(tableViewParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(tableViewScene);
        window.show();
    }

    public void stockSelected() {
        System.out.println("Stock Selected");
        StockObj selectedStock = tableView.getSelectionModel().getSelectedItem();
        new Downloader(selectedStock.getSymbol());
        Information.setSymbol(selectedStock.getSymbol().toUpperCase());
        PrepareDataForGraph.countLines();
        PrepareDataForGraph.prepare();
        graphPF();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //set up the columns in the table
        symbolColumn.setCellValueFactory(new PropertyValueFactory<StockObj, String>("Symbol"));
        entryColumn.setCellValueFactory(new PropertyValueFactory<StockObj, String>("Entry"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<StockObj, String>("Quantity"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<StockObj, String>("Price"));
        PLColumn.setCellValueFactory(new PropertyValueFactory<StockObj, String>("PL"));
        PLPColumn.setCellValueFactory(new PropertyValueFactory<StockObj, String>("PLP"));
        //load data
        tableView.setItems(getStocks());
        //Update the table to allow for the fields to be editable
        tableView.setEditable(true);
        symbolColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        entryColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        quantityColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        priceColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        PLColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        PLPColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        //run in background thread
        new Thread(this::readCSV).start();
        updateCSV();
    }

    public void deleteButtonPushed() {
        ObservableList<StockObj> selectedRows, allPeople;
        allPeople = tableView.getItems();
        //this gives us the rows that were selected
        selectedRows = tableView.getSelectionModel().getSelectedItems();
        //loop over the selected rows and remove the StockObj objects from the table
        allPeople.removeAll(selectedRows);
        writeCsvFile();
    }

    public void newStockButtonPushed() {
        if (testInt(quantityTextField.getText()) && testFloat(entryTextField.getText())) {
            new Downloader(symbolTextField.getText());
            PrepareDataForGraph.countLines();               //Counts lines in file downloaded
            if (Information.getNumberOfLines() == -2) {     //checks if file downloaded is blank i.e. not valid symbol input
                FxDialogs.showInformation("Invalid Stock", "' " + symbolTextField.getText() + "' is not a recognized symbol");
            } else {
                Information.setSymbol(symbolTextField.getText().toUpperCase());
                PrepareDataForGraph.prepare();
                StockObj newStockObj = new StockObj((symbolTextField.getText()).toUpperCase(), entryTextField.getText(), quantityTextField.getText(), Information.getDailyPrice(), calcPL(quantityTextField.getText(), entryTextField.getText(), Information.getDailyPrice()), calcPLP(entryTextField.getText(), Information.getDailyPrice()));
                stocks.add(newStockObj);
                graphPF();
                writeCsvFile();
            }
        }
    }

    public String calcPL(String quantity, String entry, String price) {
        DecimalFormat df = new DecimalFormat("0.##");
        return String.valueOf(df.format((Float.parseFloat(price) - Float.parseFloat(entry)) * Float.parseFloat(quantity)));
    }

    public String calcPLP(String entry, String price) {
        DecimalFormat df = new DecimalFormat("0.##");
        return String.valueOf(df.format(((Float.parseFloat(price) - Float.parseFloat(entry)) * 100 / Float.parseFloat(entry)))) + "%";
    }

    public boolean testInt(String testNum) {
        try {
            Integer.parseInt(testNum);
        } catch (NumberFormatException e) {
            FxDialogs.showInformation("Input Error", "Invalid Quantity. Enter an Integer. i.e. 10");
            return false;
        }
        return true;
    }

    public boolean testFloat(String testNum) {
        try {
            Float.parseFloat(testNum);
        } catch (NumberFormatException e) {
            FxDialogs.showInformation("Input Error", "Invalid Entry Price. Enter an Float. i.e. 12.89.");
            return false;
        }
        return true;
    }

    /**
     * This method will return an ObservableList of People objects
     */
    public ObservableList<StockObj> getStocks() {
        return stocks;
    }

    private void readCSV() {
        stocks.removeAll(stocks);
        String CsvFile = System.getProperty("user.dir") + "/src/ianramzy/portfolio/Portfolio.csv";
        String FieldDelimiter = ",";
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(CsvFile));
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(FieldDelimiter, -1);
                StockObj newStock = new StockObj(fields[0], fields[1], fields[2], fields[3], fields[4], fields[5]);
                stocks.add(newStock);
            }
        } catch (IOException ex) {
            Logger.getLogger(TableViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void writeCsvFile() {
        String COMMA_DELIMITER = ",";
        String NEW_LINE_SEPARATOR = "\n";
        String FILE_HEADER = "Symbol,Entry,Quantity,Price,PL,PLP";
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(System.getProperty("user.dir") + "/src/ianramzy/portfolio/Portfolio.csv");
            fileWriter.append(FILE_HEADER.toString());
            fileWriter.append(NEW_LINE_SEPARATOR);
            for (StockObj stock : stocks) {
                fileWriter.append(String.valueOf(stock.getSymbol()));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(stock.getEntry()));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(stock.getQuantity()));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(stock.getPrice()));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(stock.getPL()));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(stock.getPLP()));
                fileWriter.append(NEW_LINE_SEPARATOR);
            }
            System.out.println("CSV file updated.");
        } catch (Exception e) {
            System.out.println("CsvFileWriter error");
            e.printStackTrace();
        } finally {
            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                System.out.println("Error while flushing/closing fileWriter");
                e.printStackTrace();
            }
        }
    }

    public void updateCSV() {
        String FieldDelimiter = ",";
        String NEW_LINE_SEPARATOR = "\n";
        String FILE_HEADER = "Symbol,Entry,Quantity,Price,PL,PLP";
        BufferedReader br;
        FileWriter fileWriter = null;
        Information.setStockFileLocation(System.getProperty("user.dir") + "/src/ianramzy/portfolio/Portfolio.csv");
        System.out.println("Updating file at location: " + Information.getStockFileLocation());
        PrepareDataForGraph.countLines();
        int lines = Information.getNumberOfLines();
        try {
            br = new BufferedReader(new FileReader(System.getProperty("user.dir") + "/src/ianramzy/portfolio/Portfolio.csv"));
            br.readLine();
            fileWriter = new FileWriter(System.getProperty("user.dir") + "/src/ianramzy/portfolio/Portfolio.csv");
            fileWriter.append(FILE_HEADER);
            fileWriter.append(NEW_LINE_SEPARATOR);
            for (int i = 0; i < lines + 1; i++) {
                String fullLine = br.readLine();
                System.out.println("Fullline: " + fullLine);
                String[] fields = fullLine.split(FieldDelimiter, -1);
                new Downloader(fields[0]);
                PrepareDataForGraph.countLines();
                Information.setSymbol(fields[0]);
                PrepareDataForGraph.prepare();
                fileWriter.append(String.valueOf(fields[0]));
                fileWriter.append(FieldDelimiter);
                fileWriter.append(String.valueOf(fields[1]));
                fileWriter.append(FieldDelimiter);
                fileWriter.append(String.valueOf(fields[2]));
                fileWriter.append(FieldDelimiter);
                fileWriter.append(String.valueOf(Information.getDailyPrice()));
                fileWriter.append(FieldDelimiter);
                fileWriter.append(String.valueOf(calcPL(fields[1], fields[2], Information.getDailyPrice())));
                fileWriter.append(FieldDelimiter);
                fileWriter.append(String.valueOf(calcPLP(fields[1], Information.getDailyPrice())));
                fileWriter.append(NEW_LINE_SEPARATOR);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Update Stock Read Error");
        } catch (Exception e) {
            System.out.println("CsvFileWriter error");
            e.printStackTrace();
        } finally {
            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                System.out.println("Error while flushing/closing fileWriter");
                e.printStackTrace();
            }
        }
        readCSV();
        System.out.println("All Done Updating");
    }
}