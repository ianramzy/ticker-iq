//Class that does some of the functions required before graphing is done

package ianramzy;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Arrays;
import java.io.File;

public class PrepareDataForGraph extends Information {
    public static void prepare() {
        setDateArrayLength(getNumberOfLines());   //Determines how many days the user wants to graph "range" and sets array lengths accordingly
        setPriceArrayLength(getNumberOfLines());
        try (BufferedReader br = new BufferedReader(new FileReader(getStockFileLocation()))) {
            String line;
            String cvsSplitBy = ",";    //Set ',' as the String that separated entries
            br.readLine(); //reads first line here (dont want it in the arrays)
            for (int q = 0; q < getNumberOfLines(); q++) {
                line = br.readLine();
                String[] temp = line.split(cvsSplitBy);  //Split each line (for every comma) into a temporary array
                setPriceArrayValue(q, Double.parseDouble(temp[5])); //The 6th value ([5]) is the close price of a stock
                setDateArrayValue(q, temp[1]);       //The 2nd value ([1]) is the date of that entry
                if (q == 0) {   //For the first line set:
                    setDailyPrice(temp[5]);   //Current daily price
                    setDailyVolume(temp[6]);  //Daily volume (how many times the stock has been traded)
                    setDailyChange(temp[8]);  //How much the stock has changed that day
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Tutorial used https://www.mkyong.com/java/how-to-get-the-total-number-of-lines-of-a-file-in-java/
    //Determine how many lines are in the file downloaded
    public static void countLines() {
        int linenumber = 0;
        try {
            File file = new File(getStockFileLocation());
            if (file.exists()) {
                FileReader fr = new FileReader(file);
                LineNumberReader lnr = new LineNumberReader(fr);
                while (lnr.readLine() != null) {
                    linenumber++;
                }
                lnr.close();
            } else {
                JOptionPane.showMessageDialog(null, "Count Lines() File Error");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        linenumber = linenumber - 2;  //2 is subtracted as the last line is blank, and the first is a header
        setNumberOfLines(linenumber);
    }

    //Determine minimum value of a stock price in a user specified range
    public static void minValue() {
        double tempArray[] = new double[getRange()];   //Make a new temporary array the length of range
        for (int i = 0; i < getRange(); i++) {
            tempArray[i] = getPriceArrayValue(i);
        }
        setMinValue(Arrays.stream(tempArray).min().getAsDouble());   //Find smallest value in it
    }

    public static void maxValue() {
        double tempArray[] = new double[getRange()];
        for (int i = 0; i < getRange(); i++) {
            tempArray[i] = getPriceArrayValue(i);
        }
        setMaxValue(Arrays.stream(tempArray).max().getAsDouble());
    }
}