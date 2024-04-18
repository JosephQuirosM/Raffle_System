package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class App extends Application {
    //public attributes
   static Stage stg;
    
    //public methods
    @Override
    public void start(Stage stage) throws Exception {
        stg = new Stage();
        stg.initStyle(StageStyle.UNDECORATED);
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/main.fxml"));
        stg.centerOnScreen();
        Scene scene  = new Scene(root);
        loadScene(scene);
        
    }

    public  static void loadScene(Scene scene){
        stg.setScene(scene);
        stg.show();
    }
    // main
    public static void main(String[] args) {
        launch(args);
    }
             
}
