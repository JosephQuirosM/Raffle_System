package controller;

import app.App;
import classes.DatabaseConnector;
import java.io.IOException;
import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import oracle.jdbc.OracleTypes;


public class openRaffleController implements Initializable {

    ArrayList <Button> auxList = new ArrayList<>();
    
    public static ArrayList<String> selectedNumbers = new ArrayList<>();
    
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
        
        fillLabels();
        fillButtonArray(mainController.actualRaffle.getNumbers());
        loadStateOfButtons();
    }    

    @FXML
    private void backToMenu(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/main.fxml"));
        Scene scene  = new Scene(root);
        App.loadScene(scene);
    }
    
    private void fillButtonArray(int number){
        int i;
        auxList.clear();
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
        
        int count = 1;
        Font font = Font.font("Arial Rounded MT Bold", 12);
        
         for (i = 0; i < number; i++) {
            Button button = new Button(String.valueOf(count));
            button.setFont(font);
            count++;
            button.setPrefSize(40, 20);
            button.setOnAction(this::actionOfNumber);
            int columna = i % 10;
            int fila = i / 10;
            gpButtonArray.add(button, columna, fila);
            auxList.add(button);
         }
         
    }
    
    private void actionOfNumber(ActionEvent event){
        Button selectedButton = (Button) event.getSource();
        selectedNumbers.add(selectedButton.getText());
        
        loadBuyNumberInterface();
    }
      
    private void fillLabels(){
        lblDescription.setText(mainController.actualRaffle.getDescription());
        lblPrize.setText(mainController.actualRaffle.getPrize());
        lblPrice.setText(Integer.toString(mainController.actualRaffle.getPrice()));
        lblDate.setText(mainController.actualRaffle.getDate());
    }
    
    private void loadStateOfButtons(){
        try (Connection conn = DatabaseConnector.getConnection()) {
            try (CallableStatement methodDB = conn.prepareCall("{call Obtener_Numeros_Rifa(?, ?)}")) {
                methodDB.setInt(1, mainController.actualRaffle.getId());
                methodDB.registerOutParameter(2, OracleTypes.CURSOR);
                methodDB.execute();

                ResultSet rs = (ResultSet) methodDB.getObject(2);
                fillButtonsState(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void fillButtonsState(ResultSet rs) throws SQLException {
        int state;
        int number;

        while (rs.next()) {
            number = Integer.parseInt(rs.getString("numero"));
            state = Integer.parseInt(rs.getString("estado"));
            loadStateEachButton(number, state);
        }
        
    }
    
    private void loadStateEachButton(int number, int state){
        if(state == 1){
            auxList.get(number - 1).setStyle("-fx-background-color: #00ff00;");
            return;
        }
        if(state == 2){
         auxList.get(number -1).setStyle("-fx-background-color: #fbff00;");
         return;
        }
        
        if(state == 3){
            auxList.get(number -1).setStyle("-fx-background-color: #ff0000;");
            return;
        }
  
    }
    
    private void loadBuyNumberInterface(){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/buyNumber.fxml"));
            Scene scene  = new Scene(root);
            App.loadScene(scene);
        } catch (IOException ex) {
            Logger.getLogger(openRaffleController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
