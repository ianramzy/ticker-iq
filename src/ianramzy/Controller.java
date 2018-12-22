//Class that handles the button methods

package ianramzy;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.event.*;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.IOException;

public class Controller extends Information {
    //Declare objects from the scene (see: "MainScene.fxml)
    @FXML public AreaChart<String, Double> areaChart;
    @FXML private WebView myWebView;
    @FXML private WebView myWebView2;
    @FXML private WebEngine engine;
    @FXML private WebEngine engine2;
    @FXML private TextField myTextField;
    @FXML public javafx.scene.control.Label nameLabel;
    @FXML public javafx.scene.control.Label priceLabel;
    @FXML public javafx.scene.control.Label volumeLabel;
    @FXML public javafx.scene.control.Label changeLabel;
    @FXML public javafx.scene.control.Label highLabel;
    @FXML public javafx.scene.control.Label lowLabel;
    @FXML TabPane tabPane;
    @FXML private CheckBox checkbox1;
    @FXML private NumberAxis yAxis;

    //First method called when graphing a stock
    public void startGraphing(ActionEvent event) {
        setSymbol(myTextField.getText().toUpperCase()); //gets and sets symbol from textfield
        new Downloader(getSymbol());                    //downloads file passing the stock name to the method
        PrepareDataForGraph.countLines();               //Counts lines in file downloaded
        if (getNumberOfLines() == -2) {                 //checks if file downloaded is blank i.e. not valid symbol input
            FxDialogs.showInformation("Invalid Stock", "' " + myTextField.getText().toUpperCase() + " '" + " is not a recognized symbol");
            defaultStock();                             //If not valid graph AAPl
        }
        PrepareDataForGraph.prepare();                  //Next stage in graphing
        graph();                                        //Final stage in graphing
    }

    //Default stock graphed on startup and if symbol is not valid
    public void defaultStock() {
        setRange(72);
        setSymbol("AAPL"); //Apple is the default stock we have chosen
        new Downloader(getSymbol());
        PrepareDataForGraph.countLines();
        PrepareDataForGraph.prepare();
        graph();
    }

    //Clears chart data and labels
    public void clearChart() {
        areaChart.getData().clear();
        nameLabel.setText("");
        changeLabel.setText("");
        volumeLabel.setText("");
        priceLabel.setText("");
    }

    //Updates labels, gets information from the "information" class it extends
    public void updateLabels() {
        nameLabel.setText(getSymbol());
        priceLabel.setText(getDailyPrice());
        changeLabel.setText(getDailyChange());
        highLabel.setText("High: " + Double.toString(getMaxValue()));
        lowLabel.setText("Low:  " + Double.toString(getMinValue()));
        try {
            float dChng = Float.parseFloat(getDailyChange().substring(0, getDailyChange().length() - 1)); //read in the daily change string except the last char (which is a %) then convert to float
            if (dChng > 0) {
                changeLabel.setTextFill(Color.GREEN);  //if positive change set label green
                changeLabel.setText("+" +changeLabel.getText());
            }
            if (dChng < 0) {
                changeLabel.setTextFill(Color.RED);  //if negative set label red
            }
        } catch (NumberFormatException e) {
            System.out.println("Daily change not available");
        }
        volumeLabel.setText("Volume: " + getDailyVolume());
    }

    //Final stage in graphing
    public void graph() {
        clearChart();
        Series<String, Double> series = new Series<>();
        if (isMax()) {  //Checks if graphing max range (i.e. total history of a stock)
            for (int i = 0; i < getRange() - 20; i += 20) {
                series.getData().add(new XYChart.Data<>(getDateArrayValue(getRange() - i), getPriceArrayValue(getRange() - i)));
            }
        }
        if (!isMax()) {  //If not max then is graph for 'range' which is how many days i.e 30 days
            for (int i = 0; i < getRange(); i++) {
                series.getData().add(new XYChart.Data<>(getDateArrayValue(getRange() - i), getPriceArrayValue(getRange() - i)));
            }
        }
        areaChart.getData().add(series);
        //Set series names based on range
        if (getRange() == 24) {
            series.setName(getSymbol() + "-1m");
        }
        if (getRange() == 72) {
            series.setName(getSymbol() + "-3m");
        }
        if (getRange() == 252) {
            series.setName(getSymbol() + "-1y");
        } else {
            series.setName(getSymbol() + "-Max");
        }
        PrepareDataForGraph.minValue();     //Determine max price value of a stock (in its range)
        PrepareDataForGraph.maxValue();     //Determine max price value of a stock (in its range)
        if (checkbox1.isSelected()) {       //if the auto range checkbox is selected
            yAxis.setAutoRanging(false);
            yAxis.setLowerBound(getMinValue());
            yAxis.setUpperBound(getMaxValue());
        } else {
            yAxis.setAutoRanging(true);
        }
        updateLabels();
        System.out.println("DONE GRAPHING");
        System.out.println("");
    }

    public void webBtn1(ActionEvent event) {
        engine.load("https://www.google.com");
    }

    public void webBtn2(ActionEvent event) {
        engine.load("https://finviz.com/futures.ashx");
    }

    public void webBtn3(ActionEvent event) {
        engine.load("https://ca.finance.yahoo.com/world-indices");
    }

    public void webBtn4(ActionEvent event) {
        engine.load("https://www.cnbc.com/");
    }

    public void btn1y(ActionEvent event) {
        setMax(false);
        setRange(252); //number of trading days in a year (average)
        startGraphing(event);
    }

    public void btn3m(ActionEvent event) {
        setMax(false);
        setRange(72); //number of trading days in 3 monthes (average)
        startGraphing(event);
    }

    public void btn1m(ActionEvent event) {
        setMax(false);
        setRange(24); //number of trading days in a month (average)
        startGraphing(event);
    }

    public void btnMax(ActionEvent event) {
        setMax(true);
        PrepareDataForGraph.countLines();
        setSymbol(myTextField.getText().toUpperCase());
        new Downloader(getSymbol());
        PrepareDataForGraph.countLines();
        if (getNumberOfLines() == -2) { //checks if file downloaded is blank i.e. not valid symbol input
            FxDialogs.showInformation("Invalid Stock", "'" + myTextField.getText().toUpperCase() + "'" + " is not a recognized symbol");
            defaultStock();
        }
        PrepareDataForGraph.prepare();
        setRange(getNumberOfLines() - 2); //max number of days available
        graph();
    }

    public void newsBtn(ActionEvent event) {
        engine.load("https://finance.yahoo.com/quote/" + getSymbol());
        tabPane.getSelectionModel().selectNext();
    }

    /**
     * When this method is called, it will change the Scene to
     * a TableView example
     */
    public void changeScreenButtonPushed(ActionEvent event) throws IOException {
        Parent tableViewParent = FXMLLoader.load(getClass().getResource("portfolio/PortfolioScene.fxml"));
        Scene tableViewScene = new Scene(tableViewParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(tableViewScene);
        window.show();
    }

    @FXML
    private void initialize() throws IOException { //Initializes the controller class. This method is automatically called after the fxml file has been loaded.
        engine = myWebView.getEngine();
        engine2 = myWebView2.getEngine();
        engine2.load("https://cdn4.iconfinder.com/data/icons/symbol-duo-common-3/32/wi-fi-ok-128.png"); //internet test image (show user if they are connected to internet)
        engine.load("https://www.google.com"); //default internet page
        defaultStock();
    }
}