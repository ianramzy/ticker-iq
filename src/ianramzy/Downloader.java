//Class that starts the download process of the stock data file

package ianramzy;

import java.io.IOException;

public class Downloader extends Information {
    public Downloader(String symbol) {
        String saveDir = System.getProperty("user.dir") + "/StockResources"; //Find where program is installed and use file directory
        String Fname = symbol.toUpperCase() + "_DATA.csv";       //Name downloaded file
        //URL where file is downloaded
        String siteURL = "http://markets.financialcontent.com/stocks/action/gethistoricaldata?Month=&Symbol=" + symbol + "&Range=300&Year=";
        String finalLocation = saveDir + "/" + Fname;
        setStockFileLocation(finalLocation);    //File directory location, (later used for reading from)
        try {
            DownloaderUtility.downloadFile(siteURL, saveDir, Fname);   //Passes information onto Downloader Utility
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}