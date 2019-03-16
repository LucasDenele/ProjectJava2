package fr.isen.java2.Services;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneSwitcher {
    public void uploadNewScene(Stage newStage, String fileName, int width, int height) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource(fileName));
        newStage.setScene(new Scene(root, width, height));
        newStage.setResizable(false);
        newStage.show();
    }
}
