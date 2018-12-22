//Class that is used from "Downloader" class

package ianramzy;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

//Tutorial used www.codejava.net
public class DownloaderUtility {
    private static final int BUFFER_SIZE = 4096;

    public static void downloadFile(String fileURL, String saveDir, String Fname) throws IOException {
        URL url = new URL(fileURL);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        int responseCode;
        try {
            responseCode = httpConn.getResponseCode();
            System.out.println("HTTP response: " + responseCode);
            // always check HTTP response code first
        } catch (Exception e) {
            responseCode = 1;
        }
        if (responseCode == HttpURLConnection.HTTP_OK) {
            // opens input stream from the HTTP connection
            InputStream inputStream = httpConn.getInputStream();
            String saveFilePath = saveDir + File.separator + Fname;
            System.out.println(saveFilePath);
            // opens an output stream to save into file
            FileOutputStream outputStream = new FileOutputStream(saveFilePath);
            int bytesRead;
            byte[] buffer = new byte[BUFFER_SIZE];
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.close();
            inputStream.close();
            System.out.println("File downloaded at: " + Information.getStockFileLocation());
        } else {
            System.out.println("No file to download. Server replied HTTP code: " + responseCode);
        }
        httpConn.disconnect();
    }
}