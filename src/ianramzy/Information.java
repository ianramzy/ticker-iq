//Class that has setters and getters for static variables that the other classes use
//Every class except for "Main" and "MainScene" extend this class

package ianramzy;

public class Information {
    static String stockFileLocation;
    static int numberOfLines;
    static double[] priceArray;
    static String[] dateArray;
    static String symbol;
    static int range;
    static String dailyChange;
    static String dailyVolume;
    static String dailyPrice;
    static double maxValue;
    static double minValue;
    static boolean max;

    public static boolean isMax() {
        return max;
    }

    public static void setMax(boolean max) {
        System.out.println("Max set as: " + max);
        Information.max = max;
    }

    public static double getMaxValue() {
        return maxValue;
    }

    public static void setMaxValue(double maxValue) {
        Information.maxValue = maxValue;
        System.out.println("Max value set as: " + maxValue);
    }

    public static double getMinValue() {
        return minValue;
    }

    public static void setMinValue(double minValue) {
        Information.minValue = minValue;
        System.out.println("Min value set as: " + minValue);
    }

    public static String getDailyChange() {
        return dailyChange;
    }

    public static void setDailyChange(String dailyChange) {
        System.out.println("Daily Change set to: " + dailyChange);
        Information.dailyChange = dailyChange;
    }

    public static String getDailyVolume() {
        return dailyVolume;
    }

    public static void setDailyVolume(String dailyVolume) {
        System.out.println("Daily Volume set to: " + dailyVolume);
        Information.dailyVolume = dailyVolume;
    }

    public static String getDailyPrice() {
        return dailyPrice;
    }

    public static void setDailyPrice(String dailyPrice) {
        System.out.println("Daily Price set to: " + dailyPrice);
        Information.dailyPrice = dailyPrice;
    }

    public static int getRange() {
        return range;
    }

    public static void setRange(int range) {
        System.out.println("Set range as: " + range);
        Information.range = range;
    }

    public static String getSymbol() {
        return symbol;
    }

    public static void setSymbol(String symbol) {
        System.out.println("symbol set as: " + symbol);
        Information.symbol = symbol;
    }

    public static void setPriceArrayLength(int Newlength) {
        priceArray = new double[Newlength];
        System.out.println("Price Array length set: " + Newlength);
    }

    public static void setDateArrayLength(int Newlength) {
        dateArray = new String[Newlength];
        System.out.println("Date Array length set: " + Newlength);
    }

    public static double getPriceArrayValue(int position) {
        return priceArray[position];
    }

    public static String getDateArrayValue(int position) {
        return dateArray[position];
    }

    public static void setPriceArrayValue(int position, double value) {
        priceArray[position] = value;
    }

    public static void setDateArrayValue(int position, String value) {
        dateArray[position] = value;
    }

    public static int getNumberOfLines() {
        return numberOfLines;
    }

    public static void setNumberOfLines(int newNumberOfLines) {
        numberOfLines = newNumberOfLines;
        System.out.println("Number of lines set: " + numberOfLines);
    }

    public static String getStockFileLocation() {
        return stockFileLocation;
    }

    public static void setStockFileLocation(String newStockFileLocation) {
        stockFileLocation = newStockFileLocation;
        System.out.println("Stock file location set: " + newStockFileLocation);
    }
}