//Main method to run program from

package ianramzy;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("MainScene.fxml"));  //Loads SceneBuilder File
        primaryStage.setTitle("Ticker-iQ");
        primaryStage.setScene(new Scene(root, 1280, 720));
        primaryStage.show();
    }

    public static void main(String[] args) throws IOException {
        launch(args);
    }
}

// TODO: 8/27/18  Test
// TODO: 8/27/18 auto update stocks
// TODO: 8/27/18 Port to android
// TODO: 7/9/18 Portfolio colors
// TODO: 7/9/18 DRAWING?  
// TODO: 7/9/18 Math indicators