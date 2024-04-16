package controller;

import app.App;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Font;


public class openRaffleController implements Initializable {

    @FXML
    private AnchorPane btnBack;
    @FXML
    private GridPane gpButtonArray;
    @FXML
    private Label lblDescription;
    @FXML
    private Label lblPrice;
    @FXML
    private Label lblDate;
    @FXML
    private Label lblPrize;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        lblDescription.setText(mainController.actualRaffle.getDescription());
        lblPrize.setText(mainController.actualRaffle.getPrize());
        lblPrice.setText(Integer.toString(mainController.actualRaffle.getPrice()));
        lblDate.setText(mainController.actualRaffle.getDate());
        fillButtonArray(mainController.actualRaffle.getNumbers());
        
    }    

    @FXML
    private void backToMenu(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/main.fxml"));
        Scene scene  = new Scene(root);
        App.loadScene(scene);
    }
    
    private void fillButtonArray(int number){
        int i;
        
        gpButtonArray.getChildren().clear();
        gpButtonArray.getColumnConstraints().clear();
        gpButtonArray.getRowConstraints().clear();
        
        gpButtonArray.setHgap(10);
        gpButtonArray.setVgap(10);
        
        for ( i =0; i < 10; i++ ) {
            ColumnConstraints column = new ColumnConstraints();
            column.setPercentWidth(10);
            gpButtonArray.getColumnConstraints().add(column);
        }     
        
        int numRows = (number + 9) / 10;
        
        for (i = 0; i < numRows; i++) {
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setPercentHeight(100 / numRows);
            gpButtonArray.getRowConstraints().add(rowConstraints);
        }
        
        int count = 0;
        Font font = Font.font("Arial Rounded MT Bold", 12);
        
         for (i = 0; i < number; i++) {
            Button button = new Button(String.valueOf(count));
            button.setFont(font);
            count++;
            button.setPrefSize(40, 20);
            button.setOnAction(this::printNumber);
            int columna = i % 10;
            int fila = i / 10;
            gpButtonArray.add(button, columna, fila);
         }
         
    }
    
    private void printNumber(ActionEvent event){
        Button selectedButton = (Button) event.getSource();
        
        System.out.println(selectedButton.getText());
    }
                                                                                                                                                                                    
}
